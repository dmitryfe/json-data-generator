/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.json.generator.log;

import au.com.bytecode.opencsv.CSVWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.supercsv.io.*;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.quote.AlwaysQuoteMode;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;


/**
 *
 * @author andrewserff
 */

@SuppressWarnings("Duplicates")
public class CSVFileLogger implements EventLogger {


    private static final Logger log = LogManager.getLogger(CSVFileLogger.class);


    private final String[] whoIsHeader = new String[] { "domainName","registrarName","contactEmail","whoisServer","nameServers","createdDate","updatedDate","expiresDate","standardRegCreatedDate","standardRegUpdatedDate","standardRegExpiresDate","status","RegistryData_rawText","WhoisRecord_rawText","Audit_auditUpdatedDate","registrant_rawText","registrant_email","registrant_name","registrant_organization","registrant_street1","registrant_street2","registrant_street3","registrant_street4","registrant_city","registrant_state","registrant_postalCode","registrant_country","registrant_fax","registrant_faxExt","registrant_telephone","registrant_telephoneExt","administrativeContact_rawText","administrativeContact_email","administrativeContact_name","administrativeContact_organization","administrativeContact_street1","administrativeContact_street2","administrativeContact_street3","administrativeContact_street4","administrativeContact_city","administrativeContact_state","administrativeContact_postalCode","administrativeContact_country","administrativeContact_fax","administrativeContact_faxExt","administrativeContact_telephone","administrativeContact_telephoneExt","billingContact_rawText","billingContact_email","billingContact_name","billingContact_organization","billingContact_street1","billingContact_street2","billingContact_street3","billingContact_street4","billingContact_city","billingContact_state","billingContact_postalCode","billingContact_country","billingContact_fax","billingContact_faxExt","billingContact_telephone","billingContact_telephoneExt","technicalContact_rawText","technicalContact_email","technicalContact_name","technicalContact_organization","technicalContact_street1","technicalContact_street2","technicalContact_street3","technicalContact_street4","technicalContact_city","technicalContact_state","technicalContact_postalCode","technicalContact_country","technicalContact_fax","technicalContact_faxExt","technicalContact_telephone","technicalContact_telephoneExt","zoneContact_rawText","zoneContact_email","zoneContact_name","zoneContact_organization","zoneContact_street1","zoneContact_street2","zoneContact_street3","zoneContact_street4","zoneContact_city","zoneContact_state","zoneContact_postalCode","zoneContact_country","zoneContact_fax","zoneContact_faxExt","zoneContact_telephone","zoneContact_telephoneExt","registrarIANAID" };
    private final CsvPreference ALWAYS_QUOTE =
            new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE).useQuoteMode(new AlwaysQuoteMode()).build();

    private String outputDirectory;
    private String format;
    private boolean gzip;
    private File outputFile;
    ICsvMapWriter mapWriter = null;


    //private Map<>

    public CSVFileLogger(Map<String, Object> props) {}

    @Override
    public void logEvent(String event, Map<String, Object> producerConfig) {

        gzip = producerConfig.containsKey("gzip") && (boolean) producerConfig.get("gzip");

        // TODO: format should be mandatory!
        format = producerConfig.containsKey("format") ? producerConfig.get("format").toString() : "csv";
        outputDirectory = System.getProperty("user.dir") + "/out/" + producerConfig.get("outPath");
        outputFile = new File(outputDirectory);
        logEvent(event);
    }

    private void logEvent(String event) {
        flushToCSV(event);
    }

    @Override
    public void shutdown() {

    }


    private void flushToCSV(String event) {

        try {

            if(!outputFile.exists()){
                CSVWriter writer = new CSVWriter(new FileWriter(outputFile, true));
                writer.writeNext(whoIsHeader);
                writer.close();
            }
            CSVWriter writer = new CSVWriter(new FileWriter(outputFile, true));

            HashMap result = new ObjectMapper().readValue(event, HashMap.class);

            List<String> nextLine = new ArrayList<>();

            for(String field : whoIsHeader){
                nextLine.add(result.get(field).toString());
            }

            String[] array = nextLine.toArray(new String[0]);

            writer.writeNext(array);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
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
