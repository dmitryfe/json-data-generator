/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.json.generator.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author andrewserff
 */
public class DomainIncArrayType extends TypeHandler {
    public static final String TYPE_NAME = "domainIncArr";
    public static final String TYPE_DISPLAY_NAME = "Domain Incremental Array";

    private String prefixName;
    private int listSize;
    private Long startIndex;

    public DomainIncArrayType() {}


    @Override
    public synchronized void setLaunchArguments(String[] launchArguments) {
        super.setLaunchArguments(launchArguments);
        if (launchArguments.length < 2) {
            throw new IllegalArgumentException("You must specify a prefix for the domain name and array length");
        }
        prefixName = launchArguments[0];
        listSize = Integer.parseInt(launchArguments[1]);

        startIndex = launchArguments.length == 3 ? Long.parseLong(launchArguments[2]) : 1L;

    }
    
    @Override
    public synchronized Object getNextRandomValue() {
        Long count = startIndex;
        List<Object> listOfStrings = new ArrayList<>();

        for(int i=0;i < listSize; i++){
            listOfStrings.add(prefixName+"-"+ String.format("%06d", count+i) +".domain.ru.");
        }
        return listOfStrings;
    }
            
    @Override
    public String getName() {
        return TYPE_NAME;
    }

}
