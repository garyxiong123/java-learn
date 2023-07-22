package org.example;

import net.openhft.affinity.AffinityLock;

public class Bind2CoreRelease {
    public static void main(String[] args) {


        try (AffinityLock al = AffinityLock.acquireLock(false)) {
            // do some work while locked to a CPU.
            System.out.println(al.cpuId());


            while (true) {
                System.out.println(al.cpuId());
                System.out.println(al.isBound());


                System.out.println(AffinityLock.dumpLocks());
                System.out.println("release lock");
                al.release();
            }


        }


    }

}
