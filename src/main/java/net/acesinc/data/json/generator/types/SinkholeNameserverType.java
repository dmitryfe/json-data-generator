/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.json.generator.types;

import net.acesinc.data.json.generator.context.SinkholedResources;

/**
 *
 * @author andrewserff
 */
public class SinkholeNameserverType extends TypeHandler {

    public static final String TYPE_NAME = "sinkholeNS";
    public static final String TYPE_DISPLAY_NAME = "Sinkhole Name Server";

    @Override
    public String getNextRandomValue() {
        return SinkholedResources.getInstance().getRandomSinkholeNameserver();

    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }

}
