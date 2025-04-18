/*
 * This example shows the simplest possible use of the Cleaner API.
 * Note that it is not the recommended way to do this, for a variety
 * of reasons!
 */
package com.newardassociates.demo;


// {{## BEGIN simple-cleaner ##}}
import java.lang.ref.Cleaner;

public class SimpleCleaner {
    private static final Cleaner CLEANER = Cleaner.create();
    public SimpleCleaner() {
        CLEANER.register(this, () -> System.out.println("I'm doing some clean up"));
    }

    public static void main(String... args) {
        SimpleCleaner app = new SimpleCleaner();
        app = null;

        System.gc(); // On most systems, this should trigger cleanup.
    }
}
// {{## END simple-cleaner ##}}
