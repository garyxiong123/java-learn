package org.example;

import net.openhft.affinity.Affinity;
import net.openhft.affinity.AffinityLock;

import java.util.Random;


public class Bind2CoreSwitch {
    public static void main(String[] args) {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (AffinityLock al = AffinityLock.acquireLock(1)) {


                    System.out.println("Thread.currentThread().getName is" + Thread.currentThread().getId());
                    while (true) {

                        System.out.println(AffinityLock.dumpLocks());
                        System.out.println(al.isBound());
                        System.out.println(al.isAllocated());
                        System.out.println(al.cpuId());
                        al.resetAffinity(true);

                    }
                }
            }
        });

        thread.start();
    }

    public static int calcRandom(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    static int calcRandomFrom0To9() {
        Random random = new Random(10);
        return random.nextInt(10);
    }
}
