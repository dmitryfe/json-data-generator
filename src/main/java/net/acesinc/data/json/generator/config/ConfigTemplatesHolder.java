package net.acesinc.data.json.generator.config;

import net.acesinc.data.json.generator.workflow.WorkflowStep;

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
            if(!f.isHidden()){
                try {
                    templatesMap.put(f.getName().toUpperCase().substring(0,f.getName().indexOf(".")).toUpperCase(),JSONConfigReader.readConfig(f, WorkflowStep.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        if (templatesMap.containsKey(name.toUpperCase())){
            return templatesMap.get(name.toUpperCase());
        } else throw new RuntimeException("Template " + name + " not found in /config_templates folder. Add it.");
    }
}
