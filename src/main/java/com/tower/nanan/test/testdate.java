package com.tower.nanan.test;

import com.tower.nanan.utils.MyUtils;

public class testdate {
    public static void main(String[] args) {
        for (int i=1 ; i<44015;i++){
            System.out.print(i+"  ");
            String s = MyUtils.getaccountPeriodFromExcelDate(String.valueOf(i));
            System.out.println(s);
        }
    }
}
