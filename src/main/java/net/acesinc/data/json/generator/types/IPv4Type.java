/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.json.generator.types;

import java.util.Random;

/**
 *
 * @author andrewserff
 */
public class IPv4Type extends TypeHandler {

    // Random IPs range < 251.0.0.0

    public static final String TYPE_NAME = "ipv4";
    public static final String TYPE_DISPLAY_NAME = "Ipv4";

    @Override
    public Object getNextRandomValue() {
        Random random = new Random();
        return random.nextInt(251) + "." + random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256);

        //return InetAddresses.fromInteger(random.nextInt()).getHostAddress();

    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }

}
