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
public class ListRandomType extends TypeHandler {

    //TODO

    public static final String TYPE_NAME = "listRandom";
    public static final String TYPE_DISPLAY_NAME = "Random entity from a list";

    @Override
    public synchronized void setLaunchArguments(String[] launchArguments) {
        super.setLaunchArguments(launchArguments);
    }



    @Override
    public String getNextRandomValue() {
        return SinkholedResources.getInstance().getRandomSinkholeMail();

    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }

}
