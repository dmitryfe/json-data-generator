package net.acesinc.data.json.generator.config;

import net.acesinc.data.json.generator.content.FileResource;
import net.acesinc.data.json.generator.workflow.WorkflowStep;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by dfeshenk on 27/02/2017.
 */
public class ConfigTemplatesHolder {

    private final HashMap<String,WorkflowStep> templatesMap = new HashMap<>();

    private ConfigTemplatesHolder() {
        final File confDir = new File(System.getProperty("user.dir") + "/config_templates/");

        for(File f : confDir.listFiles()){
            try {
                templatesMap.put(f.getName().toUpperCase().substring(0,f.getName().indexOf(".")),JSONConfigReader.readConfig(f, WorkflowStep.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Holder {
        static final ConfigTemplatesHolder INSTANCE = new ConfigTemplatesHolder();
    }


    public static ConfigTemplatesHolder getInstance() {
        return ConfigTemplatesHolder.Holder.INSTANCE;
    }

    public WorkflowStep getByName(String name){

        // todo: add negative
        return templatesMap.get(name.toUpperCase());
    }
}
