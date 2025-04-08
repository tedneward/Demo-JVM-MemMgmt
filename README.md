# Demo-JVM-MemMgmt
A collection of demos for JVM memory management and GC.

Build the project.

## Project: CleanerAPI
This project demonstrates the use of the JDK `java.lang.ref.Cleaner` class to register objects for deallocation cleanup (finalization).

To run the SimpleCleaner example: Inside of `app/build.gradle.kts`, in the `application` block, uncomment `mainClass = "com.newardassociates.demo.SimpleCleaner"` and then comment out the `mainClass = "com.newardassociates.demo.App"` line two lines later. (There can only be one... mainClass.) Once the mainClass points to the demo you want to run, `./gradlew run` should run the one specified.

**Demos:**

* run the SimpleCleaner as-is: `./gradlew run`
* run the App (which uses ResourceHolder) as-is: `./gradlew run`
* run either of these (`./gradlew run`) demos with the call to `System.gc()` commented out; on most JDKs, the exiting VM will *not* trigger any Cleaner execution, just as JVM exit doesn't trigger finalizer blocks.

## Project: 

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

