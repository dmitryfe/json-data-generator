/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.json.generator.types;

import net.acesinc.data.json.generator.types.TypeHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author andrewserff
 */
public class LongIncrementalType extends TypeHandler {

    public static final String TYPE_NAME = "longIncremental";
    public static final String TYPE_DISPLAY_NAME = "Long Incremental";

    private List<String> typedValues;
    private static Map<Integer,Integer> hashIndexMap = new ConcurrentHashMap<>();
    private int hash;

    @Override
    public void setLaunchArguments(String[] launchArguments) {
        super.setLaunchArguments(launchArguments);
        typedValues = new ArrayList<>();
        for (String s : launchArguments) {
            typedValues.add(stripQuotes(s));
        }

        hash = getArrHash(typedValues);

        if (!hashIndexMap.containsKey(hash)) {
            hashIndexMap.put(hash, 0);
        }

    }

    @Override
    public Long getNextRandomValue() {
        int index = hashIndexMap.get(hash);
        return Long.valueOf(typedValues.get(hashIndexMap.put(hash, (index + 1) % typedValues.size())));
    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }

    private static int getArrHash(List<String> list){
        final int prime = 31;
        int result = 1;
        for(String st : list){
            result = result * prime + st.hashCode();
        }
        return result;
    }

    
}
