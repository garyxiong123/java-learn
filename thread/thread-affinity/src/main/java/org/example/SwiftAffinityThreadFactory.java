package org.example;


import net.openhft.affinity.AffinityLock;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ThreadFactory;

//@Log4j2
public class SwiftAffinityThreadFactory<T extends Thread> implements ThreadFactory {
    private final String name;
    private int id = 1;
    private final Queue<Integer> cpus;
    private Class<T> clazz;

    public SwiftAffinityThreadFactory(String name, Queue<Integer> cpus, Class<T> clazz) {
        this.name = name;
        this.cpus = cpus;
        this.clazz = clazz;
    }


    //    @SneakyThrows
    @NotNull
    @Override
    public synchronized Thread newThread(@NotNull final Runnable r) {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        // set a readable name
        Optional<StackTraceElement> e = Arrays.stream(stacktrace).filter(s -> !s.getClassName().contains(this.getClass().getName()) && s.getClassName().contains("com.binance")).findFirst();
        StackTraceElement element;
        if (e.isPresent()) {
            element = e.get();
        } else {
            element = stacktrace[0];
        }
        String fileName = element.getFileName();
        String upstreamMethodName = fileName.substring(0, fileName.length() - 5) + "_" + element.getMethodName();

        String name2 = name + "_" + id + "_" + upstreamMethodName;
        id++;
        T t = null;
        try {
            t = clazz.getConstructor(Runnable.class, String.class).newInstance(new Runnable() {
                @Override
                public void run() {
                    AffinityLock ignored = acquireLock();
                    if (ignored!=null){
                        System.out.println("\n name=" + name2 + "isBound " + ignored.isBound());
                    }

                    try {
                        r.run();
                    } catch (Exception e) {
                        //                    log.error("Terminate app due to error: {} \n", e.getMessage(), e);
                        System.exit(-1);
                    }
                }
            }, name2);
        } catch (InstantiationException instantiationException) {
            instantiationException.printStackTrace();
        } catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        } catch (InvocationTargetException invocationTargetException) {
            invocationTargetException.printStackTrace();
        } catch (NoSuchMethodException noSuchMethodException) {
            noSuchMethodException.printStackTrace();
        }
        return t;
    }

    private synchronized AffinityLock acquireLock() {
        final Integer cpu = cpus.poll();
        if (cpu == null) {
//            log.error("No enough CPU to bind, skip");
            return null;
        }
//        log.info("Bind to cpu {}", cpu.intValue());
        AffinityLock al = AffinityLock.acquireLock(cpu.intValue());
        return al;
    }
}
