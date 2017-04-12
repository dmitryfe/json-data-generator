/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.json.generator.types.dates;

import net.acesinc.data.json.generator.types.TypeHandler;

import java.util.Date;

/**
 *
 * @author andrewserff
 */
public abstract class NowBaseType extends TypeHandler {

    //note, this can be a negative number, so you are subtracting

    private long timeToAdd = 0;
    private String dateTimeFormat = "yyyy/MM/dd HH:mm:ss.SS";
    // TODO: move from here
    public static final Date GEN_START_TIME = new Date();
    private boolean fromGenStart = false;
    
    @Override
    public void setLaunchArguments(String[] launchArguments) {
        super.setLaunchArguments(launchArguments);
        timeToAdd = 0;
        dateTimeFormat = "yyyy/MM/dd HH:mm:ss.SS";

        for(String arg : launchArguments){
            if (arg.contains("_")){
                timeToAdd = getTimeOffset(arg);
            }

            if (arg.contains("GEN-START-TIME")) {
                fromGenStart = true;
            }

            if (arg.contains(":")){
                dateTimeFormat = arg;
            }
        }
    }
    
    public static long getTimeOffset(String input) {
        long timeOffset = 0;
        String arg = stripQuotes(input);
        String timeAmount = arg;
        boolean isNegative = false;
        if (arg.startsWith("-")) {
            //it's a negative number
            isNegative = true;
            timeAmount = arg.substring(1, arg.length());
        }
        
        String[] pieces = timeAmount.split("_");
        //first part is the number
        int time = Integer.parseInt(pieces[0]);
        //second part is the unit
        String unit = pieces[1];
        
        long multiplier = 1;
        switch (unit) {
            case "y": {
                multiplier = 1000 * 60 * 60 * 24 * 365;
                break;
            }
            case "d": {
                multiplier = 1000 * 60 * 60 * 24;
                break;
            }
            case "h": {
                multiplier = 1000 * 60 * 60;
                break;
            }
            case "m": {
                multiplier = 1000 * 60;
                break;
            }
        }
        
        timeOffset = time * multiplier;
        
        if (isNegative) {
            timeOffset = timeOffset * -1;
        }
        return timeOffset;
    }

    public Date getNextDate() {
        if (timeToAdd == 0) {
            return fromGenStart ? GEN_START_TIME : new Date();
        } else {
            return fromGenStart ? new Date(GEN_START_TIME.getTime() + timeToAdd) : new Date(new Date().getTime() + timeToAdd);
        }
    }

    public String getDateFormatString(){
        return dateTimeFormat;
    }
    
}
