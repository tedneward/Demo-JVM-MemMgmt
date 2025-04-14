/*
 * A collection of demos around reference objects. Comment/uncomment
 * the calls in main() to highlight the demo desired.
 */
package com.newardassociates.demo;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class App {
    /**
     * This function just allocates a ton of chunks of memory in an
     * attempt to force the GC to kick in. Note that for a large
     * enough number of CHUNKS, you'll force an OutOfMemoryError
     * and terminate the JVM.
     */
    public static List<Object> createMemoryPressure() {
        System.out.println("Creating memory pressure");

        // You may need to toy with this number below depending on your JVM
        // and -Xms/-Xmx settings; on my machine, for -Xms25m -Xmx25m, 40
        // is sufficient to trigger GC but not terminate the VM.
        int CHUNKS = 40;
        List<Object> unused = new ArrayList<Object>();
        for (int i=0; i<CHUNKS; i++) { unused.add(new byte[100_000]); }
        return unused;
    }

    /**
     * Demonstrate running out of memory
     */
    public static void runMemory() {
        List<Object> captured = new LinkedList<Object>();
        while (true) {
            captured.addAll(createMemoryPressure());
        }
    }

    /**
     * Demonstrates how SoftReferences release under memory pressure.
     */
    public static void softRefs() {
        System.out.println("Starting SoftRefs demo");

        Map<Integer, SoftReference<LargeObject>> cache = new HashMap<>();
        List<Integer> ids = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        for (Integer id : ids) {
            cache.put(id, new SoftReference<LargeObject>(new LargeObject(id)));
        }

        // Is the soft reference still good?
        for (Integer id : ids) {
            System.out.print("Checking id " + id + ":");
            if (Objects.nonNull(cache.get(id).get())) {
                System.out.println("good! --> " + (cache.get(id).get()));
            }
            else {
                System.out.println("released");
            }
        }

        createMemoryPressure();

        // Is the soft reference still good?
        for (Integer id : ids) {
            System.out.print("Checking id " + id + ":");
            if (Objects.nonNull(cache.get(id).get())) {
                System.out.println("good! --> " + (cache.get(id).get()));
            }
            else {
                System.out.println("released");
            }
        }
    }

    public static void softRefsWithQueue() {
        System.out.println("Starting SoftRefs with ReferenceQueue demo");

        Map<Integer, SoftReference<LargeObject>> cache = new HashMap<>();
        final ReferenceQueue<LargeObject> rq = new ReferenceQueue<>();
        List<Integer> ids = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        for (Integer id : ids) {
            cache.put(id, new SoftReference<LargeObject>(new LargeObject(id), rq));
        }

        // Let's spin up a thread to watch for SoftRefs getting cleared
        Thread t = new Thread( () -> {
            System.out.println("Starting cache-clearing-awareness thread");
            try {
                while(true) {
                    Reference<? extends LargeObject> ref = rq.remove();
                    System.out.println("Hey, just so you know, LargeObject " + ref.get() + " just went away");
                }        
            }
            catch (InterruptedException intEx) {
                System.out.println("Interrupted; terminating cache-clearing-awareness thread");
            }
        });
        t.setDaemon(true);
        t.start();

        // Sleep for a second just to let the awareness thread to be sure to get a chance to run
        try { Thread.sleep(1000); } catch (InterruptedException ignored) { }

        // Is the soft reference still good?
        for (Integer id : ids) {
            System.out.print("Checking id " + id + ":");
            if (Objects.nonNull(cache.get(id).get())) {
                System.out.println("good! --> " + (cache.get(id).get()));
            }
            else {
                System.out.println("released");
            }
        }

        createMemoryPressure();

        // Is the soft reference still good?
        for (Integer id : ids) {
            System.out.print("Checking id " + id + ":");
            if (Objects.nonNull(cache.get(id).get())) {
                System.out.println("good! --> " + (cache.get(id).get()));
            }
            else {
                System.out.println("released");
            }
        }
    }

    public static void weakRefs() {
        System.out.println("Starting WeakRefs demo");

        Map<Integer, WeakReference<LargeObject>> cache = new HashMap<>();
        List<Integer> ids = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        for (Integer id : ids) {
            cache.put(id, new WeakReference<LargeObject>(new LargeObject(id)));
        }

        // Is the soft reference still good?
        for (Integer id : ids) {
            System.out.print("Checking id " + id + ":");
            if (Objects.nonNull(cache.get(id).get())) {
                System.out.println("good! --> " + (cache.get(id).get()));
            }
            else {
                System.out.println("released");
            }
        }

        createMemoryPressure();

        // Is the soft reference still good?
        for (Integer id : ids) {
            System.out.print("Checking id " + id + ":");
            if (Objects.nonNull(cache.get(id).get())) {
                System.out.println("good! --> " + (cache.get(id).get()));
            }
            else {
                System.out.println("released");
            }
        }
    }

    public static void weakRefsWithQueue() {
        System.out.println("Starting WeakRefs with ReferenceQueue demo");

        Map<Integer, WeakReference<LargeObject>> cache = new HashMap<>();
        final ReferenceQueue<LargeObject> rq = new ReferenceQueue<>();
        List<Integer> ids = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        for (Integer id : ids) {
            cache.put(id, new WeakReference<LargeObject>(new LargeObject(id), rq));
        }

        // Let's spin up a thread to watch for SoftRefs getting cleared
        Thread t = new Thread( () -> {
            System.out.println("Starting cache-clearing-awareness thread");
            try {
                while(true) {
                    Reference<? extends LargeObject> ref = rq.remove();
                    System.out.println("Hey, just so you know, LargeObject " + ref.get() + " just went away");
                }        
            }
            catch (InterruptedException intEx) {
                System.out.println("Interrupted; terminating cache-clearing-awareness thread");
            }
        });
        t.setDaemon(true);
        t.start();

        // Sleep for a second just to let the awareness thread to be sure to get a chance to run
        try { Thread.sleep(1000); } catch (InterruptedException ignored) { }

        // Is the soft reference still good?
        for (Integer id : ids) {
            System.out.print("Checking id " + id + ":");
            if (Objects.nonNull(cache.get(id).get())) {
                System.out.println("good! --> " + (cache.get(id).get()));
            }
            else {
                System.out.println("released");
            }
        }

        createMemoryPressure();

        // Is the soft reference still good?
        for (Integer id : ids) {
            System.out.print("Checking id " + id + ":");
            if (Objects.nonNull(cache.get(id).get())) {
                System.out.println("good! --> " + (cache.get(id).get()));
            }
            else {
                System.out.println("released");
            }
        }
    }

    public static void weakVsSoft() {
        Object softObj = new Object();
        Object weakObj = new Object();
        
        // Create a SoftReference and a WeakReference
        SoftReference<Object> softRef = new SoftReference<>(softObj);
        WeakReference<Object> weakRef = new WeakReference<>(weakObj);
        
        // Clear strong references
        softObj = null;
        weakObj = null;
        
        System.out.println("Before GC - SoftReference: " + softRef.get());
        System.out.println("Before GC - WeakReference: " + weakRef.get());
        
        // Force garbage collection
        // Comment this out, just to see if there's a difference
        System.gc();
        
        System.out.println("After GC - SoftReference: " + softRef.get());
        System.out.println("After GC - WeakReference: " + weakRef.get());
    }

    /*
    // PhantomReferences only really make sense when used in
    // conjunction with a ReferenceQueue, so there's really
    // nothing to show here.
    public static void phantomRefs() {
    }
    */

    public static void phantomRefsWithQueue() {
        System.out.println("Starting PhantomRefs demo");

        ReferenceQueue<LargeObject> referenceQueue = new ReferenceQueue<>();
        List<LargeObject.PhRef> references = new ArrayList<>();

        // Allocate our big/expensive objects that require specific cleanup
        List<LargeObject> largeObjects = new ArrayList<>();
        final HashSet<Integer> ids = new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        for (Integer id : ids) {
            LargeObject largeObject = new LargeObject(id);

            largeObjects.add(largeObject);

            // register the PhRef with a RefQueue so we can get notified to clean up
            references.add(new LargeObject.PhRef(largeObject, referenceQueue));
        }

        // drop our strong reference; eligible for garbage collection
        largeObjects = null;
        
        // notify garbage collection
        System.gc();

        // waiting for garbage collection; sometimes this may take a second
        while (! ids.isEmpty()) {
            for (LargeObject.PhRef reference : references) {
                if (reference.isEnqueued()) {
                    System.out.println("Reference " + reference.getId() + " enqueued");
                    ids.remove(reference.getId());
                }
            }
        }

        // read the queue and clear references
        Reference<?> referenceFromQueue;
        while (Objects.nonNull((referenceFromQueue = referenceQueue.poll()))) {
            ((LargeObject.PhRef)referenceFromQueue).cleanup();

            // remove permanently (object will not be enqueued)
            referenceFromQueue.clear();
        }
    }

    public static void main(String[] args) {
        runMemory();
        //softRefs();
        //softRefsWithQueue();
        //weakRefs();
        //weakRefsWithQueue();
        //weakVsSoft();
        //phantomRefsWithQueue();
    }
}
