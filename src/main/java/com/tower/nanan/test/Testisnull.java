package com.tower.nanan.test;

import com.tower.nanan.entity.ElectricQueryBean;

public class Testisnull {

    public static void main(String[] args) {
        ElectricQueryBean electricQueryBean = new ElectricQueryBean();
        System.out.println(electricQueryBean.getCustomers()==null);
        //System.out.println(electricQueryBean.getCustomers().isEmpty());
        System.out.println(electricQueryBean);
    }
}
