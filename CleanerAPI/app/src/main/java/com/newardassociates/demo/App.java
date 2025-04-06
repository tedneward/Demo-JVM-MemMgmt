package com.newardassociates.demo;

public class App {
    public static void main(String[] args) {
        // The hygenic happy-path
        try (ResourceHolder resourceHolder = new ResourceHolder()) {
            resourceHolder.doSomething();
            System.out.println("Goodbye");
        }

        // The "oh crap I forgot" path
        ResourceHolder resourceHolder = new ResourceHolder();
        resourceHolder.doSomething();
        resourceHolder = null;

        System.gc(); // to facilitate the running of the cleaning action
    }
}
