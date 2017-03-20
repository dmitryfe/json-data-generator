/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.json.generator.types;

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
    private Map<String, Long> namedCounterMap;

    public DomainIncrementalType() {
        namedCounterMap = new ConcurrentHashMap<>();
    }

    @Override
    public void setLaunchArguments(String[] launchArguments) {
        super.setLaunchArguments(launchArguments);
        if (launchArguments.length != 1) {
            throw new IllegalArgumentException("You must specify a prefix for the domain name");
        }
        prefixName = launchArguments[0];
        namedCounterMap.putIfAbsent(prefixName, 1L);
    }
    
    @Override
    public String getNextRandomValue() {
        Long count = namedCounterMap.get(prefixName);
        namedCounterMap.put(prefixName, count + 1);
        return prefixName+"-"+ String.format("%06d", count) +".domain.ru.";
    }
            
    @Override
    public String getName() {
        return TYPE_NAME;
    }

}
