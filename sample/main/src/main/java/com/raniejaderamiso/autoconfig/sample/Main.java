package com.raniejaderamiso.autoconfig.sample;

/**
 * @author Ranie Jade Ramiso
 */
public class Main {
    public static void main(String[] args) {
        ApplicationConfig config = new ApplicationConfig$$AutoConfig();

        System.out.println("int: " + config.getInteger());
        System.out.println("double: " + config.getDouble());
        System.out.println("long: " + config.getLong());
        System.out.println("char: " + config.getChar());
        System.out.println("boolean: " + config.getBoolean());
        System.out.println("String: " + config.getString());
    }
}
