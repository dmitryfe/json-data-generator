/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.json.generator.types;

import java.util.*;


/**
 *
 * @author andrewserff
 */
public class IpGeneratorIncType extends TypeHandler {


    public static final String TYPE_NAME = "ipv4IncRange";
    public static final String TYPE_DISPLAY_NAME = "ipv4IncRange";
    String startIP, endIP;


    @Override
    public synchronized void setLaunchArguments(String[] launchArguments) {
        super.setLaunchArguments(launchArguments);
        startIP = launchArguments[0];
        endIP = launchArguments[1];
    }



    @Override
    public synchronized List<Object> getNextRandomValue() {
        long start = ipToLong(startIP);
        long end = ipToLong(endIP)+1;

        List<Object> listOfStrings = new ArrayList<>();

        for(int i= 0; i < (end - start); i++){
            listOfStrings.add(longToIp(i+start));
        }

        return listOfStrings;
    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }

    public static void main(String[] args) {
        System.out.println(longToIp(Long.valueOf("1", 16)));
    }

    /**
     * Convert an IP address to a hex string
     *
     * @param ipAddress Input IP address
     *
     * @return The IP address in hex form
     */
    public static String toHex(String ipAddress) {
        return Long.toHexString(IpGeneratorIncType.ipToLong(ipAddress));
    }

    /**
     * Convert an IP address to a number
     *
     * @param ipAddress Input IP address
     *
     * @return The IP address as a number
     */
    public static long ipToLong(String ipAddress) {
        long result = 0;
        String[] atoms = ipAddress.split("\\.");

        for (int i = 3; i >= 0; i--) {
            result |= (Long.parseLong(atoms[3 - i]) << (i * 8));
        }

        return result & 0xFFFFFFFF;
    }

    public static String longToIp(long ip) {
        StringBuilder sb = new StringBuilder(15);

        for (int i = 0; i < 4; i++) {
            sb.insert(0, Long.toString(ip & 0xff));

            if (i < 3) {
                sb.insert(0, '.');
            }

            ip >>= 8;
        }

        return sb.toString();
    }


}
