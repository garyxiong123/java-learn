package org.example;

import net.openhft.affinity.Affinity;
import net.openhft.affinity.AffinityLock;

public class Bind2CoreMore {
    public static void main(String[] args) {
        // Set thread affinity to a specific CPU core
        int cpuCore = 0; // Specify the CPU core to set affinity to
        System.out.println("\nThe assignment of CPUs is\n" + AffinityLock.dumpLocks());
        System.out.println("\nRuntime.getRuntime().availableProcessors() is\n" + Runtime.getRuntime().availableProcessors());

        Affinity.setAffinity(1);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread.currentThread().getName is" + Thread.currentThread().getId());
            }
        });

        System.out.println("\nThe assignment of CPUs is\n" + AffinityLock.dumpLocks());

        System.out.println("\nRuntime.getRuntime().availableProcessors() is\n" + Runtime.getRuntime().availableProcessors());
        // Reset thread affinity to any CPU core
        Affinity.setAffinity(AffinityLock.BASE_AFFINITY);

    }
}
