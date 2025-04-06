# Demo-JVM-MemMgmt
A collection of demos for JVM memory management and GC.

Build the project.

## Demo: CleanerAPI
This project demonstrates the use of the JDK `java.lang.ref.Cleaner` class to register objects for deallocation cleanup (finalization).



## Demo: Generate an OutOfMemoryError
`java -ms100m -mx100m -cp app/build/libs/app.jar:utilities/build/libs/utilities.jar org.example.app.App`

## TODO

* Allocator: allocate memory infinitely
    * ... into a non-rootset reference (so as to demonstrate the JVM never crashes)
    * ... into a rootset reference (so as to cause the JVM to crash)
* Finalizer: allocate finalizer objects infinitely
    * Run: as normal, showing finalizers executing
    * Run: with finalizers=disabled
* Lazarus: finalizer object that resurrects itself
* Cleaner: allocate/register Cleaner-hosted objects infinitely
* References:
    * WeakReferences
    * SoftReferences
    * PhantomReferences

