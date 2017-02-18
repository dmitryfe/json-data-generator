/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.json.generator.log;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author andrewserff
 */

@SuppressWarnings("Duplicates")
public class JsonFileLogger implements EventLogger {


    private static final Logger log = LogManager.getLogger(JsonFileLogger.class);

    private File outputDirectory;
    private String format;
    private boolean gzip;

    public JsonFileLogger(Map<String, Object> props) {}

    @Override
    public void logEvent(String event, Map<String, Object> producerConfig) {
        gzip = producerConfig.containsKey("gzip") && (boolean) producerConfig.get("gzip");

        // TODO: format should be mandatory!
        format = producerConfig.containsKey("format") ? producerConfig.get("format").toString() : "json";
        String outDir = System.getProperty("user.dir") + "/out/" + producerConfig.get("outPath");
        outputDirectory = new File(outDir);
        logEvent(event);
    }

    private void logEvent(String event) {
        if (!gzip) flushToJSON(event);
        else {
            try {
                flushToJsonGzip(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void shutdown() {
        //we don't need to shut anything down
    }

    private void flushToJSON(String event){
        try {
            FileUtils.writeStringToFile(outputDirectory, event + "\n", "UTF-8",true);
        } catch (IOException ioe) {
            log.error("Unable to write event to the file ", outputDirectory);
        }
    }

    private void flushToJsonGzip(String event) throws IOException {
        FileOutputStream output = new FileOutputStream(outputDirectory);
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
