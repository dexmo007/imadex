package com.dexmohq.imadex;

import java.util.Base64;
import java.util.HashSet;

public class Test {

    public static void main(String[] args) {
        final HashSet<String> set = new HashSet<>();
        final String orig = new String("hi");
        set.add(orig);
        final String newString = new String("hi");
        set.add(newString);
        System.out.println(set.iterator().next() == orig);
        System.out.println(set.iterator().next() == newString);
    }
}
