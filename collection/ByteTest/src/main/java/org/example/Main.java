package org.example;

public class Main {

    public static String data = "sssssssddddsssssssddddsssssssddddsssssssddddsssssssddddsssssssddddsssssssddddsssssssddddsssssssddddsssssssddddsssssssddddsssssssdsssssssddddsssssssddddsssssssddddsssssssddddsssssssddddsssssssddddsssssssdddddddsssssssddddsssssssdddd";

    public static void main(String[] args) {

        byteTest();
        normalTest();
    }

    private static void normalTest() {

    }

    private static void byteTest() {

        while (true) {
            long start = System.nanoTime();
            byte[] bytes = data.getBytes();
            long end = System.nanoTime();
            System.out.println(end - start);
        }


    }
}