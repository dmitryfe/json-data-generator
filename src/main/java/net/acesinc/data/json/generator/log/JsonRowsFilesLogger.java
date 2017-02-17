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
public class JsonRowsFilesLogger implements EventLogger {


    private static final Logger log = LogManager.getLogger(JsonRowsFilesLogger.class);
    public static final String OUTPUT_DIRECTORY_PROP_NAME = "output.directory";
    public static final String FILE_PREFIX_PROP_NAME = "file.prefix";
    public static final String FILE_EXTENSION_PROP_NAME = "file.extension";

    private File outputDirectory;
    private String filePrefix;
    private String fileExtension;

    public JsonRowsFilesLogger(Map<String, Object> props) throws IOException {
        String outputDir = (String) props.get(OUTPUT_DIRECTORY_PROP_NAME);
        outputDirectory = new File(outputDir);
        if (!outputDirectory.exists()) {
            if (!outputDirectory.mkdir()) {
                if (!outputDirectory.mkdirs()) {
                    throw new IOException("Output directory does not exist and we are unable to create it");
                }
            }
        }
        filePrefix = (String) props.get(FILE_PREFIX_PROP_NAME);
        fileExtension = (String) props.get(FILE_EXTENSION_PROP_NAME);
        outputDirectory = new File("/Users/dfeshenk/work/akamai/json-data-generator/test/json-data-generator-1.2.2-SNAPSHOT/conf/output.json");

    }

    @Override
    public void logEvent(String event, Map<String, Object> producerConfig) {

        // TODO: check thread safety


        outputDirectory = new File("/Users/dfeshenk/work/akamai/json-data-generator/test/json-data-generator-1.2.2-SNAPSHOT/conf/" + producerConfig.get("file") );

        // First, serialise event

        logEvent(event);
    }

    private void logEvent(String event) {
        try {


            //Second, flush to proper format


            //File f = outputDirectory;

            FileUtils.writeStringToFile(outputDirectory, event + "\n", "UTF-8",true);
        } catch (IOException ioe) {
            log.error("Unable to create temp file");
        }

    }

    @Override
    public void shutdown() {
        //we don't need to shut anything down
    }


    public void flushToGz() throws IOException {
        BufferedWriter writer = null;
        try {
            GZIPOutputStream zip = new GZIPOutputStream(
                    new FileOutputStream(new File("tmp.zip")));

            writer = new BufferedWriter(
                    new OutputStreamWriter(zip, "UTF-8"));

            String[] data = new String[] { "this", "is", "some",
                    "data", "in", "a", "list" };

            for (String line : data) {
                writer.append(line);
                writer.newLine();
            }
        } finally {
            if (writer != null)
                writer.close();
        }
    }


}
