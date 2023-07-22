package org.example;

import sun.misc.Unsafe;

public class Main {

    /**
     * 底层的源码中，经常可见Unsafe，Unsafe类是java用于内存管理的类，但操作不当，可能导致内存泄漏等问题，官方亦不推荐过多使用Unsafe，故而名为Unsafe。
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Hello world!");

        Unsafe unsafe = Unsafe.getUnsafe();
        unsafe.getInt(1);
        unsafe.getInt(1);
        long l = unsafe.allocateMemory(33);

        unsafe.notify();
        unsafe.notifyAll();

    }
}