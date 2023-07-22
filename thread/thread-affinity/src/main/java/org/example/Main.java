package org.example;

import net.openhft.affinity.AffinityLock;
import net.openhft.affinity.AffinityThreadFactory;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static net.openhft.affinity.AffinityStrategies.*;

public class Main {
    static Queue<Integer> cpus=new ArrayDeque<>();
    static {
        cpus.add(0);
        cpus.add(1);
        cpus.add(2);
        cpus.add(3);
        cpus.add(4);
        cpus.add(5);
        cpus.add(6);
        cpus.add(7);
        cpus.add(8);
        cpus.add(9);
        cpus.add(10);
        cpus.add(11);
        cpus.add(12);
    }
    private static final ExecutorService ES = Executors.newFixedThreadPool(20,
            new SwiftAffinityThreadFactory("bg", cpus,Thread.class));


    private static final ExecutorService ES1 = Executors.newFixedThreadPool(20);

    public static void main(String[] args) throws InterruptedException {

        for (int i = 1; i < 30; i++) {
            ES.submit(new Callable<Void>() {
                @Override
                public Void call() throws InterruptedException {
                    while (true){
                        long startTime = System.currentTimeMillis();
                    }
//                    Thread.sleep(1000*100);
//                    System.out.println("Thread.currentThread().getName is" + Thread.currentThread().getId());

//                    return null;
                }
            });
        }
        Thread.sleep(1000);

        for (int i = 1; i < 30; i++) {
            ES1.submit(new Callable<Void>() {
                @Override
                public Void call() throws InterruptedException {
                    System.out.println("Thread.currentThread().getName is");
//                    Thread.sleep(1000*1);
                    while (true){
                        long startTime = System.currentTimeMillis();
                    }
//                    System.out.println("ES1 Thread.currentThread().getName is" + Thread.currentThread().getId());

//                    return null;
                }
            });
        }
        try {
            Thread.sleep(1000*100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\nThe assignment of CPUs is\n" + AffinityLock.dumpLocks());

        System.out.println("\nRuntime.getRuntime().availableProcessors() is\n" + Runtime.getRuntime().availableProcessors());


        ES.shutdown();
        ES1.shutdown();

        try {
            ES.awaitTermination(1, TimeUnit.SECONDS);
//            ES1.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
