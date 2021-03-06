/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.json.generator.types;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author andrewserff
 */
public class DomainIncrementalType extends TypeHandler {
    public static final String TYPE_NAME = "domainIncremental";
    public static final String TYPE_DISPLAY_NAME = "Domain Incremental";

    private String prefixName;
    private static Map<String, Long> maxIndexMap;
    private long minIndex;
    private static Map<String, Long> namedCounterMap;

    public DomainIncrementalType() {
        namedCounterMap = new ConcurrentHashMap<>();
        maxIndexMap = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void setLaunchArguments(String[] launchArguments) {
        super.setLaunchArguments(launchArguments);
        if (launchArguments.length == 0)
            throw new IllegalArgumentException("You must specify a prefix for the domain name");

        minIndex = launchArguments.length > 1 ? Long.parseLong(launchArguments[1]) : 1L;
        long maxIndex = launchArguments.length > 2 ? Long.parseLong(launchArguments[2]) : -1;

        prefixName = launchArguments[0]+"$$$"+minIndex+"_"+maxIndex;
        namedCounterMap.putIfAbsent(prefixName, minIndex);
        maxIndexMap.putIfAbsent(prefixName,maxIndex);
    }
    
    @Override
    public synchronized Object getNextRandomValue() {
        Long count = namedCounterMap.get(prefixName);
        if(maxIndexMap.get(prefixName) > 0 && count > maxIndexMap.get(prefixName)) {
            count = minIndex;
            namedCounterMap.put(prefixName, minIndex);
        }
        namedCounterMap.put(prefixName, count + 1);
        if(prefixName.toLowerCase().contains("primary")){
            return prefixName.substring(0,prefixName.indexOf("$$$")) +"-"+ String.format("%06d", count) +".ru.";
        }
        else{
            return prefixName.substring(0,prefixName.indexOf("$$$")) +"-"+ String.format("%06d", count) +".domain.ru.";
        }

    }
            
    @Override
    public String getName() {
        return TYPE_NAME;
    }

}
