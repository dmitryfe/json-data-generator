package net.acesinc.data.json.generator.context;

import org.apache.commons.io.IOUtils;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.io.IOException;
import java.util.List;

/**
 * Created by dfeshenk on 16/02/2017.
 */
public class SinkholedResources {

    //TODO: make generic list by its path

    private final String sinkholeFolderPath = "common/";
    private  List<String> sinkholeNameservers;
    private  List<String> sinkholeMails;

    private SinkholedResources() {
        try {
            sinkholeNameservers = IOUtils.readLines(getClass().getClassLoader().getResourceAsStream(sinkholeFolderPath + "sinkhole_nameservers.csv"));
            sinkholeMails = IOUtils.readLines(getClass().getClassLoader().getResourceAsStream(sinkholeFolderPath + "sinkhole_mails.csv"));
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
