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


@SuppressWarnings("Duplicates")
public class JsonFileLogger implements EventLogger {


    private static final Logger log = LogManager.getLogger(JsonFileLogger.class);
    private final String NAME = "JSON";

    private File outputDirectory;

    public JsonFileLogger() {}

    @Override
    public void logEvent(String event, Map<String, Object> producerConfig) {
        String outDir = System.getProperty("user.dir") + "/out/" + producerConfig.get("outPath");
        outputDirectory = new File(outDir);
        logEvent(event);
    }

    private void logEvent(String event) {
        flushToJSON(event);
    }

    @Override
    public void shutdown() {
        //we don't need to shut anything down
    }

    @Override
    public String getName() {
        return NAME;
    }

    private void flushToJSON(String event){
        try {
            FileUtils.writeStringToFile(outputDirectory, event + "\n", "UTF-8",true);
        } catch (IOException ioe) {
            log.error("Unable to write event to the file ", outputDirectory);
        }
    }

    private void flushToJsonGzip(String event) throws IOException {
        try (FileOutputStream output = new FileOutputStream(outputDirectory)) {
            try (Writer writer = new OutputStreamWriter(new GZIPOutputStream(output), "UTF-8")) {
                writer.append(event).append("\n");
            }
        }
    }

}
