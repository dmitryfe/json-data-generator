/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.json.generator.log;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPOutputStream;


@SuppressWarnings("Duplicates")
public class CSVFileLogger implements EventLogger {


    private static final Logger log = LogManager.getLogger(CSVFileLogger.class);
    private final String NAME = "CSV";

    private Map<String,String[]> headers = new HashMap<>();
    private String outputDirPath;
    private File outputFile;

    public CSVFileLogger() {}

    @Override
    public void logEvent(String event, Map<String, Object> producerConfig) {

        outputDirPath = System.getProperty("user.dir") + "/out/" + producerConfig.get("outPath");
        outputFile = new File(outputDirPath);

        File outputDirectory = new File(outputDirPath.substring(0,outputDirPath.lastIndexOf("/")));

        if (!outputDirectory.exists()) {
            log.info("Creating path " + outputDirectory.getAbsolutePath());
            outputDirectory.mkdirs();
        }

        logEvent(event);
    }

    private void logEvent(String event) {
        flushToCSV(event);
    }

    @Override
    public void shutdown() {}

    @Override
    public String getName() {
        return NAME;
    }


    private void flushToCSV(String event) {

        try {

            if(!headers.containsKey(outputDirPath)){
                setHeader(event);
            }

            CSVWriter writer = new CSVWriter(new FileWriter(outputFile, true));
            LinkedHashMap result = new ObjectMapper().readValue(event, LinkedHashMap.class);

            List<String> nextLine = new LinkedList<>();

            for(String field : headers.get(outputDirPath)){
                nextLine.add(result.get(field).toString());
            }

            String[] array = nextLine.toArray(new String[0]);
            writer.writeNext(array);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void setHeader(String event) {
        if(!headers.containsKey(outputDirPath)){
            if(!outputFile.exists()){
                try {
                    // Take header from JSON
                    String[] newHeader = getHeader(event);
                    log.info("Going to set new header for the file: " + outputFile.getAbsolutePath() + "\nHeader: " + Arrays.toString(newHeader));
                    headers.put(outputDirPath,newHeader);

                    CSVWriter writer = new CSVWriter(new FileWriter(outputFile, true));
                    writer.writeNext(newHeader);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Take from file
                String[] newHeader = getHeaderFromFile();
                log.info("Reading the header from file: " + outputFile.getAbsolutePath() + "\nHeader: " + Arrays.toString(newHeader));
                headers.put(outputDirPath,newHeader);

            }
        }
    }

    private String[] getHeader(String event){
        try {
            LinkedHashMap result = new ObjectMapper().readValue(event, LinkedHashMap.class);
            Set<String> keys = result.keySet();
            return keys.toArray(new String[keys.size()]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    private String[] getHeaderFromFile(){
        try {
            CSVReader reader = new CSVReader(new FileReader(outputFile));
            return reader.readNext();

        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Header not found in file " + outputFile);

    }


    private void flushToJsonGzip(String event) throws IOException {
        FileOutputStream output = new FileOutputStream(outputDirPath);
        try {
            Writer writer = new OutputStreamWriter(new GZIPOutputStream(output), "UTF-8");
            try {
                writer.append(event).append("\n");
            } finally {
                writer.close();
            }
        } finally {
            output.close();
        }
    }

}
