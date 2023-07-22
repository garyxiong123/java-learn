package org.example;

import net.openhft.affinity.AffinityLock;

public class Bind2CoreOne {
    public static void main(String[] args) {

//        用法很简单，在需要亲和的代码上加上acquireLock即可，它会自动分配一个空闲核心


        try (AffinityLock al = AffinityLock.acquireLock(3)) {
            // do some work while locked to a CPU.
            System.out.println(al.cpuId());
            while (true) {
                System.out.println(al.cpuId());


                System.out.println(AffinityLock.dumpLocks());
                System.out.println(al.isBound());

                System.out.println(AffinityLock.cpuLayout());
            }
        }


        //如果要指定一个cpu也是可以的，我这里指定cpu5
        //如果启动了两个java程序，将其分配到同一cpu核心也是可以的


        //2、限制线程在多个cpu核心上运行

//        首先可以通过getAffinity获取进程的关联性掩码，查看当前线程能够在哪几个cpu核心运行

//        BitSet bitSet1 = Affinity.getAffinity();
//        System.out.println(bitSet1);
//
//        bitSet1.set(4);
//        bitSet1.set(5);
//        bitSet1.set(6);
//        bitSet1.set(7);
//        Affinity.setAffinity(bitSet1);


//        可以自己指定mask掩码，比如这里的掩码值为oct(240)=bin(1111 0000)，就是把当前线程限制到了cpu4、5、6、7核心上运行，也可以说是运行线程在这四个cpu核心迁移（能够被操作系统调度），关于掩码计算在源码分析会详细说明

    }
}
