package com.newardassociates.demo;

import java.lang.ref.Cleaner;
import java.lang.ref.Cleaner.Cleanable;

// {{## BEGIN external-resource ##}}
class ExternalResource {
    public static long resourceIDs = 0L;

    public long instanceID = -1L;
    private ExternalResource(long id) { this.instanceID = id; }

    public static ExternalResource allocate() {
        ExternalResource r = new ExternalResource(++resourceIDs); 
        System.out.println("ExternalResource: Allocated resource " + r.instanceID);
        return r;
    }
    public static void deallocate(ExternalResource res) {
        System.out.println("ExternalResource: Deallocating resource " + res.instanceID);
    }
}
// {{## END external-resource ##}}

// {{## BEGIN resource-wrapper-preamble ##}}
/**
 * ResourceHolder is a Java-friendly wrapper around an external resource
 * (aka ExternalResource).
 */
public class ResourceHolder implements AutoCloseable {
    public ResourceHolder() {
        this.externalResource = ExternalResource.allocate();
        cleanable = CLEANER.register(this, new CleaningAction(this.externalResource));
    }
    private ExternalResource externalResource; // The raw resource

    public void doSomething() {
        System.out.println("Doing something cool with " + externalResource.instanceID);
    }
    // {{## END resource-wrapper-preamble ##}}

    // {{## BEGIN resource-wrapper-cleanup ##}}
    private static final Cleaner CLEANER = Cleaner.create();
    private final Cleaner.Cleanable cleanable; // So we can aggressively clean()

    // This is our Runnable that ensures no references to the
    // ExternalResource are strongly reachable (and thus keep
    // it from being cleaned up).
    static class CleaningAction implements Runnable {
        private ExternalResource externalResource;

        public CleaningAction(ExternalResource externalResource) {
            this.externalResource = externalResource;
        }

        @Override public void run() {
            ExternalResource.deallocate(externalResource);
            externalResource = null;
        }
    }
    // {{## END resource-wrapper-cleanup ##}}

    // {{## BEGIN resource-wrapper-autoclose ##}}
    @Override public void close() {
        System.out.println("AutoCloseable::close()");
        cleanable.clean();
    }
    // {{## END resource-wrapper-autoclose ##}}
}
