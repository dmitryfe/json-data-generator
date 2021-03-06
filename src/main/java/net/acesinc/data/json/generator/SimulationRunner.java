/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.json.generator;

import java.io.IOException;
import java.util.*;

import net.acesinc.data.json.generator.config.*;
import net.acesinc.data.json.generator.log.EventLogger;
import net.acesinc.data.json.generator.workflow.Workflow;
import net.acesinc.data.json.generator.workflow.WorkflowStep;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author andrewserff
 */
public class SimulationRunner {

    private static final Logger log = LogManager.getLogger(SimulationRunner.class);
    private SimulationConfig config;
    private List<EventGenerator> eventGenerators;
    private List<Thread> eventGenThreads;
    private boolean running;
    private List<EventLogger> eventLoggers;

    public SimulationRunner(SimulationConfig config, List<EventLogger> loggers) {
        this.config = config;
        this.eventLoggers = loggers;
        eventGenerators = new ArrayList<>();
        eventGenThreads = new ArrayList<>();
        
        setupSimulation();
    }

    private void setupSimulation() {
        running = false;

        // Values from default templates
        for (WorkflowConfig workflowConfig : config.getWorkflows()) {
            try {
                log.info("Loading config file: " + workflowConfig.getWorkflowFilename());
                Workflow w = JSONConfigReader.readConfig(this.getClass().getClassLoader().getResourceAsStream(workflowConfig.getWorkflowFilename()), Workflow.class);

                for (WorkflowStep simConfigStep : w.getSteps()) {
                    if (!simConfigStep.getProducerConfig().containsKey("template")) break;
                    String template = (String) simConfigStep.getProducerConfig().get("template");

                    WorkflowStep templateStep = ConfigTemplatesHolder.getInstance().getByName(template);

                    for(String key : templateStep.getConfig().get(0).keySet()){
                        if(simConfigStep.getConfig().get(0).containsKey(key))
                            templateStep.getConfig().get(0).put(key,simConfigStep.getConfig().get(0).get(key));
                    }
                    simConfigStep.getConfig().get(0).clear();
                    simConfigStep.getConfig().get(0).putAll(templateStep.getConfig().get(0));
                }

                final EventGenerator gen = new EventGenerator(w, workflowConfig.getWorkflowName(), eventLoggers);
                log.info("Adding EventGenerator for [ " + workflowConfig.getWorkflowName() + "," + workflowConfig.getWorkflowFilename() + " ]");
                eventGenerators.add(gen);
                eventGenThreads.add(new Thread(gen));
            } catch (IOException ex) {
                log.error("Error reading config: " + workflowConfig.getWorkflowName(), ex);
            }
        }
    }

    public void startSimulation() {
        log.info("Starting Simulation");
               
        if (eventGenThreads.size() > 0) {
            for (Thread t : eventGenThreads) {
                t.start();
            }
            running = true;
        }
    }

    public void stopSimulation() {
        log.info("Stopping Simulation");
        for (Thread t : eventGenThreads) {
            t.interrupt();
        }
        for (EventLogger l : eventLoggers) {
            l.shutdown();
        }
        running = false;
    }

    public boolean isRunning() {
        if (eventGenThreads.size() > 0) {
            boolean isAlive=false;
            for (Thread t : eventGenThreads) {
                isAlive |= t.isAlive();
            }
            running = isAlive;
        }
        return running;
    }

}
