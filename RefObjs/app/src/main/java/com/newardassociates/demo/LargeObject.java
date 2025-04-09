package com.newardassociates.demo;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * Representing a large or expensive object that we need notifications around
 * when it is no longer referenced. We put an "id" here for demo purposes.
 */
public class LargeObject {
    public int id;

    public LargeObject(int id) { this.id = id; }

    /**
     * Nested class, mostly for cohesion and convenience; 
     * nested static class to avoid the outer reference.
     * Note that we need all the necessary cleanup information
     * (e.g., the id) in this reference, so that we can do the
     * necessary cleanup, since the referent will be null when
     * the ref-q is signaled.
     */
    public static class PhRef extends PhantomReference<LargeObject> {
        int id;

        /**
         * Creates a new phantom reference that refers to the given object and
         * is registered with the given queue.
         *
         * @param referent the LargeObject associated with this PhRef
         * @param q        the queue with which the reference is to be registered
         */
        public PhRef(LargeObject referent, ReferenceQueue<? super LargeObject> q) {
            super(referent, q);
            id = referent.id;
        }

        public int getId() { return id; }

        public void cleanup() { System.out.println("Cleaning up the stuff for LargeObject " + id); }

        @Override public String toString() { return "LargeObjectReference { id=" + id + " }"; }
    }
}
