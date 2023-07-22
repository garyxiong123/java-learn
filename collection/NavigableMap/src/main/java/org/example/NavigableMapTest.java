package org.example;

import java.util.Collections;
import java.util.NavigableMap;
import java.util.TreeMap;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class NavigableMapTest {

    public static void main(String[] args) {
        // Press Ctrl+1 with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.


        NavigableMap<Long, String> navigableMap = new TreeMap<>();
        NavigableMap<Long, String> navigableMap1 = new TreeMap<>(Collections.reverseOrder());
        navigableMap.put(2L, "2");
        navigableMap.put(1L, "1");
        navigableMap.put(3L, "3");
        navigableMap.put(4L, "4");


        navigableMap1.put(1L, "1");
        navigableMap1.put(2L, "2");
        navigableMap1.put(3L, "3");
        navigableMap1.put(4L, "4");

        System.out.println(navigableMap.lowerEntry(2L));

        System.out.println(navigableMap.lowerEntry(3L));

        System.out.println(navigableMap.higherKey(3L));
        System.out.println(navigableMap.higherEntry(3L));

        System.out.println(navigableMap1);
    }
}