package org.example;

import java.util.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class EnginSetTest {


    public static void main(String[] args) {
        // Press Ctrl+1 with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.

        Set<Order> set = new TreeSet<>(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return o1.getPrice().compareTo(o2.getPrice());
            }
        });

        NavigableMap<Long, Order> upTreeMap = new TreeMap<>();
        NavigableMap<Long, Order> navigableMap1 = new TreeMap<>(Collections.reverseOrder());


        HashMap<Long, Order> orderMap = new HashMap<Long, Order>();
        Order order1 = newOrder(1L, 1L);
        Order order2 = newOrder(2L, 2L);
        Order order3 = newOrder(3L, 3L);
        set.add(order1);
        set.add(order2);
        set.add(order3);
        System.out.println(set);
        order1.setPrice(5L);

        System.out.println(set);

        System.out.println("-----重新计算爆仓价格");



        System.out.println(upTreeMap.lowerEntry(2L));

    }

    private static Order newOrder(long id, long price) {
        return new Order(id, price);
    }

}