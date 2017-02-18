/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.json.generator.types.dates;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 *
 * @author andrewserff
 */
public class FarSightTimestampType extends NowBaseType {

    public static final String TYPE_NAME = "farsightTS";
    public static final String TYPE_DISPLAY_NAME = "FarSight timestamp format";

    @Override
    public String getNextRandomValue() {
        SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");
        DF.setTimeZone(TimeZone.getTimeZone("UTC"));
        return DF.format(getNextDate());
    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }
}
