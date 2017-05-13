/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.json.generator.types;

import net.acesinc.data.json.generator.log.CSVFileLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author andrewserff
 */
public class StringIncrementalType extends TypeHandler {

    private static final Logger log = LogManager.getLogger(StringIncrementalType.class);

    public static final String TYPE_NAME = "stringIncremental";
    public static final String TYPE_DISPLAY_NAME = "String Incremental";

    private List<String> typedValues;
    private static Map<Long,Integer> hashIndexMap = new ConcurrentHashMap<>();
    private long hash;

    @Override
    public synchronized void setLaunchArguments(String[] launchArguments) {
        super.setLaunchArguments(launchArguments);
        typedValues = new ArrayList<>();
        for (String s : launchArguments) {
            typedValues.add(stripQuotes(s));
        }

        hash = getArrHash(typedValues);

        hashIndexMap.putIfAbsent(hash, 0);

        log.trace("set hash: " + hash);
        log.trace("hashIndexMap0: " + Collections.singletonList(hashIndexMap));

    }

    @Override
    public synchronized String getNextRandomValue() {

        int index = hashIndexMap.get(hash);
        log.trace("index: " + index);
        log.trace("hash: " + hash);

        int nextIdx = (index + 1) % typedValues.size();
        log.trace("arr size: " + typedValues.size());
        log.trace("next index: " + nextIdx);
        int prevIdx = hashIndexMap.put(hash, nextIdx);

        String b = typedValues.get(prevIdx);


        log.trace("prevIdx: " + prevIdx);
        log.trace("DOMAIN: " + b);
        log.trace("hashIndexMap: " + Collections.singletonList(hashIndexMap));

        return b;
    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }

    private synchronized static long getArrHash(List<String> list){
        final int prime = 31;
        int result = 1;
        for(String st : list){
            result = result * prime + st.hashCode();
        }
        return result;
    }

    
}
