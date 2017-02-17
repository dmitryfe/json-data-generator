/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.json.generator.types;

import java.text.SimpleDateFormat;

/**
 *
 * @author andrewserff
 */
public class DateTimeType extends NowBaseType {

    public static final String TYPE_NAME = "dateTime";
    public static final String TYPE_DISPLAY_NAME = "Date Time";

    @Override
    public String getNextRandomValue() {
        SimpleDateFormat DF = new SimpleDateFormat(super.getDateFormatString());
        return DF.format(getNextDate());
    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }
}
