/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.json.generator.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.acesinc.data.json.generator.context.AvroResources;
import org.apache.avro.Schema;
import org.apache.avro.file.*;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("Duplicates")
public class AvroFileLogger implements EventLogger {
    private static final Logger log = LogManager.getLogger(AvroFileLogger.class);
    private final String NAME = "AVRO";
    private Map<String,Schema> schemasMap = new HashMap<>();
    private final int DEFLATE_COMPRESSION_LEVEL = -1;

    private File outputFile;

    public AvroFileLogger() {}

    @Override
    public void logEvent(String event, Map<String, Object> producerConfig) {
        synchronized (this){
            String outDir = System.getProperty("user.dir") + "/out/" + producerConfig.get("outPath").toString().replaceAll("\\$\\{ts}", getTsString(event));

            if(!producerConfig.containsKey("schema")) throw new RuntimeException("'schema' property must be defined in producerConfig for Avro type files generation");
            String schemaName = producerConfig.get("schema").toString();

            File outputDirectory = new File(outDir.substring(0,outDir.lastIndexOf("/")));
            outputFile = new File(outDir);
            if (!outputDirectory.exists()) {
                log.info("Creating path "+ outputDirectory.getAbsolutePath());
                outputDirectory.mkdirs();
            }

            if(!schemasMap.containsKey(schemaName)) setNewSchema(schemaName);
            logEvent(event,schemaName);
        }

    }

    private synchronized void logEvent(String event,String schemaName) {
        flushToAvro(event,schemaName,setNewWriter(schemasMap.get(schemaName)));
    }

    private void setNewSchema(String schemaName){
        Schema schema = null;
        try {
            schema = new Schema.Parser().parse(AvroResources.getInstance().getShemaByName(schemaName.toUpperCase()));
            schemasMap.put(schemaName,schema);
        } catch (IOException e) {
            log.error("Unable to parse schema ", schema);
            e.printStackTrace();
        }
    }

    private DataFileWriter setNewWriter(Schema schema){
        try {
            DataFileWriter dataFileWriter = new  DataFileWriter<>(new GenericDatumWriter<>(schema));
            dataFileWriter.setCodec(CodecFactory.deflateCodec(DEFLATE_COMPRESSION_LEVEL));

            if(outputFile.exists()) {
                return dataFileWriter.appendTo(outputFile);
            } else{
                return dataFileWriter.create(schema, outputFile);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void shutdown() {
        //we don't need to shut anything down
    }

    @Override
    public String getName() {
        return NAME;
    }

    private void flushToAvro(String event, String schemaName,DataFileWriter dataFileWriter){
        try {

            DatumReader<GenericRecord> reader = new GenericDatumReader<>(schemasMap.get(schemaName));
            GenericRecord datum = reader.read(null, DecoderFactory.get().jsonDecoder(schemasMap.get(schemaName), event));

            dataFileWriter.append(datum);
            dataFileWriter.close();

         } catch (IOException ioe) {
            log.error("Unable to write event to the file ", outputFile.getAbsolutePath());
        }
    }

    private String getTsString(String event){
        try {
            LinkedHashMap result = new ObjectMapper().readValue(event, LinkedHashMap.class);
            return result.get("ts").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Failed to get ts from event");
    }


}
