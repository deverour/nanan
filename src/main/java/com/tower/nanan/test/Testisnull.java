package com.tower.nanan.test;

import com.tower.nanan.entity.ElectricQueryBean;
import com.tower.nanan.utils.MyUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.tower.nanan.utils.MyUtils.daysBetween;

public class Testisnull {

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy/MM/dd");
        Date endDate = formatter.parse("1900/1/3");
        Date startDate = formatter.parse("1900/1/1");
        int day = daysBetween(startDate, endDate);
        System.out.println("day>>"+day);

    }
}
