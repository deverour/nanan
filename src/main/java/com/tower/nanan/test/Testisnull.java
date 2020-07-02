package com.tower.nanan.test;

import com.tower.nanan.entity.ElectricQueryBean;

import java.util.HashSet;
import java.util.Set;

public class Testisnull {

    public static void main(String[] args) {
        Set set = new HashSet();
        set.add("a");
        set.add("b");
        set.add("a");
        System.out.println(set);
    }
}
