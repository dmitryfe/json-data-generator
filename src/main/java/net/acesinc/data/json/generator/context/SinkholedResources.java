package net.acesinc.data.json.generator.context;

import net.acesinc.data.json.generator.log.FileLogger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.apache.commons.io.IOUtils.toInputStream;

/**
 * Created by dfeshenk on 16/02/2017.
 */
public class SinkholedResources {

    //TODO: make generic list by its path

    private final String sinkholeFolder = "/cluster_conf/";
    final String sinkholeFolderPath = System.getProperty("user.dir") + sinkholeFolder;
    private  List<String> sinkholeNameservers;
    private  List<String> sinkholeMails;
    private static final Logger log = LogManager.getLogger(SinkholedResources.class);

    private SinkholedResources() {
        try {
            log.info("Loading Sinkholed resources from " + sinkholeFolderPath);
            sinkholeNameservers = IOUtils.readLines(toInputStream(sinkholeFolderPath + "sinkhole_nameservers.csv"));
            sinkholeMails = IOUtils.readLines(toInputStream(sinkholeFolderPath + "sinkhole_mails.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Holder {
        static final SinkholedResources INSTANCE = new SinkholedResources();
    }


    public static SinkholedResources getInstance() {
        return Holder.INSTANCE;
    }

    public List<String> getSinkholeNameservers(){
        return sinkholeNameservers;
    }

    public List<String> getSinkholeMails(){
        return sinkholeMails;
    }

    public String getRandomSinkholeNameserver(){
        RandomDataGenerator rand = new RandomDataGenerator();
        return sinkholeNameservers.get(rand.getRandomGenerator().nextInt(sinkholeNameservers.size()));
    }

    public String getRandomSinkholeMail(){
        RandomDataGenerator rand = new RandomDataGenerator();
        return sinkholeMails.get(rand.getRandomGenerator().nextInt(sinkholeMails.size()));
    }

}
