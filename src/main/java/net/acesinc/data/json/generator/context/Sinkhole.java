package net.acesinc.data.json.generator.context;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by dfeshenk on 16/02/2017.
 */
public class Sinkhole {

    private final String sinkholeFolderPath = "sinkhole/";
    private  List<String> sinkholeNameservers;
    private  List<String> sinkholeMails;

    private Sinkhole() {
        try {
            sinkholeNameservers = IOUtils.readLines(getClass().getClassLoader().getResourceAsStream(sinkholeFolderPath + "sinkhole_nameservers.csv"));
            sinkholeMails = IOUtils.readLines(getClass().getClassLoader().getResourceAsStream(sinkholeFolderPath + "sinkhole_mails.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static class Holder {
        static final Sinkhole INSTANCE = new Sinkhole();
    }

    public static Sinkhole getInstance() {
        return Holder.INSTANCE;
    }

    public List<String> getSinkholeNameservers(){
        return sinkholeNameservers;
    }

    public List<String> getSinkholeDomains(){
        return sinkholeMails;
    }





}
