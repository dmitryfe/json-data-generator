package net.acesinc.data.json.generator.context;

import net.acesinc.data.json.generator.config.JSONConfigReader;
import net.acesinc.data.json.generator.workflow.WorkflowStep;
import org.apache.commons.io.IOUtils;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dfeshenk on 16/02/2017.
 */
public class AvroResources {

    //TODO: make generic list by its path

    private final String avroSchemasDir = "/avro/";
    private HashMap<String,File> schemasList = new HashMap<>();

    private AvroResources() {
        final File confDir = new File(System.getProperty("user.dir") + avroSchemasDir);

        if(confDir.listFiles()!=null){
            for(File f : confDir.listFiles()){
                schemasList.put(f.getName().toUpperCase().substring(0,f.getName().indexOf(".")).toUpperCase(),f);
            }
        }
    }

    private static class Holder {
        static final AvroResources INSTANCE = new AvroResources();
    }


    public static AvroResources getInstance() {
        return Holder.INSTANCE;
    }

    public File getShemaByName(String name){
        return schemasList.get(name.toUpperCase());
    }


}
