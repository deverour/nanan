package com.tower.nanan.entity;

import java.util.HashSet;

public class Group {
    public static final HashSet<String> regionSet = new HashSet<String>() {
        {
            add("巴南");
            add("南岸");
            add("綦江");
            add("江津");

        }
    };

    public static final HashSet<String> ShareCustomerSet = new HashSet<String>() {
        {
            add("移动");
            add("联通");
            add("电信");
            add("移动+联通");
            add("移动+电信");
            add("联通+电信");
            add("联通+移动");
            add("电信+移动");
            add("电信+联通");
            add("移动+联通+电信");
            add("移动+电信+联通");
            add("联通+移动+电信");
            add("联通+电信+移动");
            add("电信+移动+联通");
            add("电信+联通+移动");

        }
    };
    public static final HashSet<String> CustomerSet = new HashSet<String>() {
        {
            add("移动");
            add("联通");
            add("电信");
        }
    };
}
