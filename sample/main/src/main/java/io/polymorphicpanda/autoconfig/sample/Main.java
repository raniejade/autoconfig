package io.polymorphicpanda.autoconfig.sample;

import java.util.Arrays;

import io.polymorphicpanda.autoconfig.AutoConfigLoader;

/**
 * @author Ranie Jade Ramiso
 */
public class Main {
    public static void main(String[] args) {
        ApplicationConfig config = AutoConfigLoader.load(ApplicationConfig.class);
        Arrays.asList().stream().forEach(o -> System.out.println(o.toString()));

        System.out.println("int: " + config.getInteger());
        System.out.println("double: " + config.getDouble());
        System.out.println("long: " + config.getLong());
        System.out.println("char: " + config.getChar());
        System.out.println("boolean: " + config.getBoolean());
        System.out.println("String: " + config.getString());
        System.out.println("Something: " + config.getSomething());
        System.out.println("SomethingElse: " + config.getSomethingElse());
    }
}
