package com.tower.nanan.poi;

import com.tower.nanan.entity.ExcelColumns;
import com.tower.nanan.pojo.*;
import com.tower.nanan.utils.MyUtils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelWrite {
    final static int PAGE_COUNTS = 1000000;

    public static InputStream WriteElectics(List<Electric> list){
        int pages= list.size()/PAGE_COUNTS+1;
        System.out.println("共： "+pages+"页");
        ArrayList<String> sheetName = new ArrayList<String>();
        for (int page=1;page<=pages;page++){
            sheetName.add("sheet"+page);
        }

        SXSSFWorkbook wb=new SXSSFWorkbook();
        ArrayList<String> namelist = ExcelColumns.getElectricTitle();

        long ta =0;
        long tb =0;
        ta = System.currentTimeMillis();
        for (int page=1;page<=pages;page++){
            wb.createSheet(sheetName.get(page-1));
            SXSSFSheet sheet = wb.getSheetAt(page-1);
            int rowindex = 0;
            SXSSFRow row = sheet.createRow(rowindex);
            int colindex = 0;
            for (String str:namelist){
                SXSSFCell cell = row.createCell(colindex);
                cell.setCellValue(str);
                colindex++;
            }
            rowindex++;

            Cell cell;
            int start=(page-1)*(PAGE_COUNTS-1);
            int end=page*(PAGE_COUNTS-1)-1;
            if(page==pages){
                end=list.size()-1;
            }
            for (int listindex=start;listindex<=end;listindex++){
                row = sheet.createRow(rowindex);
                List<String> electricList = MyUtils.getList(list.get(listindex));
                for (int i=0;i<electricList.size();i++){
                    String str =electricList.get(i);
                    cell = row.createCell(i);
                    cell.setCellValue(str);
                }
                rowindex++;
            }
        }

        tb = System.currentTimeMillis();
        System.out.println("总耗时："+(tb-ta));
        InputStream in = null;
        try{
            //临时缓冲区
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            //创建临时文件
            wb.write(baout);
            byte [] bookByteAry = baout.toByteArray();
            in = new ByteArrayInputStream(bookByteAry);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return in;
    }

    /*public static InputStream WriteRebackStats(List<RebackStatWithCustomer> list) {
        int pages= list.size()/PAGE_COUNTS+1;
        System.out.println("共： "+pages+"页");
        ArrayList<String> sheetName = new ArrayList<String>();
        for (int page=1;page<=pages;page++){
            sheetName.add("sheet"+page);
        }

        SXSSFWorkbook wb=new SXSSFWorkbook();
        ArrayList<String> namelist = ExcelColumns.getRebackStatTitle();

        long ta =0;
        long tb =0;
        ta = System.currentTimeMillis();
        for (int page=1;page<=pages;page++){
            wb.createSheet(sheetName.get(page-1));
            SXSSFSheet sheet = wb.getSheetAt(page-1);
            int rowindex = 0;
            SXSSFRow row = sheet.createRow(rowindex);
            int colindex = 0;
            for (String str:namelist){
                SXSSFCell cell = row.createCell(colindex);
                cell.setCellValue(str);
                colindex++;
            }
            rowindex++;

            Cell cell;
            int start=(page-1)*(PAGE_COUNTS-1);
            int end=page*(PAGE_COUNTS-1)-1;
            if(page==pages){
                end=list.size()-1;
            }
            for (int listindex=start;listindex<=end;listindex++){
                row = sheet.createRow(rowindex);
                List<String> rebackStatList = MyUtils.getList(list.get(listindex));
                for (int i=0;i<rebackStatList.size();i++){
                    String str =rebackStatList.get(i);
                    cell = row.createCell(i);
                    cell.setCellValue(str);
                }
                rowindex++;
            }
        }

        tb = System.currentTimeMillis();
        System.out.println("总耗时："+(tb-ta));
        InputStream in = null;
        try{
            //临时缓冲区
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            //创建临时文件
            wb.write(baout);
            byte [] bookByteAry = baout.toByteArray();
            in = new ByteArrayInputStream(bookByteAry);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return in;
    }*/

    public static InputStream WriteVerifies(List<Verify> list) {
        int pages= list.size()/PAGE_COUNTS+1;
        System.out.println("共： "+pages+"页");
        ArrayList<String> sheetName = new ArrayList<String>();
        for (int page=1;page<=pages;page++){
            sheetName.add("sheet"+page);
        }

        SXSSFWorkbook wb=new SXSSFWorkbook();
        ArrayList<String> namelist = ExcelColumns.getVerifyTitle();

        long ta =0;
        long tb =0;
        ta = System.currentTimeMillis();
        for (int page=1;page<=pages;page++){
            wb.createSheet(sheetName.get(page-1));
            SXSSFSheet sheet = wb.getSheetAt(page-1);
            int rowindex = 0;
            SXSSFRow row = sheet.createRow(rowindex);
            int colindex = 0;
            for (String str:namelist){
                SXSSFCell cell = row.createCell(colindex);
                cell.setCellValue(str);
                colindex++;
            }
            rowindex++;

            Cell cell;
            int start=(page-1)*(PAGE_COUNTS-1);
            int end=page*(PAGE_COUNTS-1)-1;
            if(page==pages){
                end=list.size()-1;
            }
            for (int listindex=start;listindex<=end;listindex++){
                row = sheet.createRow(rowindex);
                List<String> verifyList = MyUtils.getList(list.get(listindex));
                for (int i=0;i<verifyList.size();i++){
                    String str =verifyList.get(i);
                    cell = row.createCell(i);
                    cell.setCellValue(str);
                }
                rowindex++;
            }
        }

        tb = System.currentTimeMillis();
        System.out.println("总耗时："+(tb-ta));
        InputStream in = null;
        try{
            //临时缓冲区
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            //创建临时文件
            wb.write(baout);
            byte [] bookByteAry = baout.toByteArray();
            in = new ByteArrayInputStream(bookByteAry);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return in;
    }

    public static InputStream WriteRebacks(List<Reback> list) {
        int pages= list.size()/PAGE_COUNTS+1;
        System.out.println("共： "+pages+"页");
        ArrayList<String> sheetName = new ArrayList<String>();
        for (int page=1;page<=pages;page++){
            sheetName.add("sheet"+page);
        }

        SXSSFWorkbook wb=new SXSSFWorkbook();
        ArrayList<String> namelist = ExcelColumns.getRebackTitle();

        long ta =0;
        long tb =0;
        ta = System.currentTimeMillis();
        for (int page=1;page<=pages;page++){
            wb.createSheet(sheetName.get(page-1));
            SXSSFSheet sheet = wb.getSheetAt(page-1);
            int rowindex = 0;
            SXSSFRow row = sheet.createRow(rowindex);
            int colindex = 0;
            for (String str:namelist){
                SXSSFCell cell = row.createCell(colindex);
                cell.setCellValue(str);
                colindex++;
            }
            rowindex++;

            Cell cell;
            int start=(page-1)*(PAGE_COUNTS-1);
            int end=page*(PAGE_COUNTS-1)-1;
            if(page==pages){
                end=list.size()-1;
            }
            for (int listindex=start;listindex<=end;listindex++){
                row = sheet.createRow(rowindex);
                List<String> electricList = MyUtils.getList(list.get(listindex));
                for (int i=0;i<electricList.size();i++){
                    String str =electricList.get(i);
                    cell = row.createCell(i);
                    cell.setCellValue(str);
                }
                rowindex++;
            }
        }

        tb = System.currentTimeMillis();
        System.out.println("总耗时："+(tb-ta));
        InputStream in = null;
        try{
            //临时缓冲区
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            //创建临时文件
            wb.write(baout);
            byte [] bookByteAry = baout.toByteArray();
            in = new ByteArrayInputStream(bookByteAry);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return in;
    }

    public static InputStream WritePercentages(List<Percentage> list) {
        int pages= list.size()/PAGE_COUNTS+1;
        System.out.println("共： "+pages+"页");
        ArrayList<String> sheetName = new ArrayList<String>();
        for (int page=1;page<=pages;page++){
            sheetName.add("sheet"+page);
        }

        SXSSFWorkbook wb=new SXSSFWorkbook();
        ArrayList<String> namelist = ExcelColumns.getPercentageTitle();

        long ta =0;
        long tb =0;
        ta = System.currentTimeMillis();
        for (int page=1;page<=pages;page++){
            wb.createSheet(sheetName.get(page-1));
            SXSSFSheet sheet = wb.getSheetAt(page-1);
            int rowindex = 0;
            SXSSFRow row = sheet.createRow(rowindex);
            int colindex = 0;
            for (String str:namelist){
                SXSSFCell cell = row.createCell(colindex);
                cell.setCellValue(str);
                colindex++;
            }
            rowindex++;

            Cell cell;
            int start=(page-1)*(PAGE_COUNTS-1);
            int end=page*(PAGE_COUNTS-1)-1;
            if(page==pages){
                end=list.size()-1;
            }
            for (int listindex=start;listindex<=end;listindex++){
                row = sheet.createRow(rowindex);
                List<String> electricList = MyUtils.getList(list.get(listindex));
                for (int i=0;i<electricList.size();i++){
                    String str =electricList.get(i);
                    cell = row.createCell(i);
                    cell.setCellValue(str);
                }
                rowindex++;
            }
        }

        tb = System.currentTimeMillis();
        System.out.println("总耗时："+(tb-ta));
        InputStream in = null;
        try{
            //临时缓冲区
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            //创建临时文件
            wb.write(baout);
            byte [] bookByteAry = baout.toByteArray();
            in = new ByteArrayInputStream(bookByteAry);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return in;

    }

    public static InputStream WriteRebackStatWithCustomer(List<RebackStatWithCustomer> list) {
        int pages= list.size()/PAGE_COUNTS+1;
        System.out.println("共： "+pages+"页");
        ArrayList<String> sheetName = new ArrayList<String>();
        for (int page=1;page<=pages;page++){
            sheetName.add("sheet"+page);
        }

        SXSSFWorkbook wb=new SXSSFWorkbook();
        ArrayList<String> namelist = ExcelColumns.getRebackStatWithCustomerTitle();

        long ta =0;
        long tb =0;
        ta = System.currentTimeMillis();
        for (int page=1;page<=pages;page++){
            wb.createSheet(sheetName.get(page-1));
            SXSSFSheet sheet = wb.getSheetAt(page-1);
            int rowindex = 0;
            SXSSFRow row = sheet.createRow(rowindex);
            int colindex = 0;
            for (String str:namelist){
                SXSSFCell cell = row.createCell(colindex);
                cell.setCellValue(str);
                colindex++;
            }
            rowindex++;

            Cell cell;
            int start=(page-1)*(PAGE_COUNTS-1);
            int end=page*(PAGE_COUNTS-1)-1;
            if(page==pages){
                end=list.size()-1;
            }
            for (int listindex=start;listindex<=end;listindex++){
                row = sheet.createRow(rowindex);
                List<String> electricList = MyUtils.getList(list.get(listindex));
                for (int i=0;i<electricList.size();i++){
                    String str =electricList.get(i);
                    cell = row.createCell(i);
                    cell.setCellValue(str);
                }
                rowindex++;
            }
        }

        tb = System.currentTimeMillis();
        System.out.println("总耗时："+(tb-ta));
        InputStream in = null;
        try{
            //临时缓冲区
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            //创建临时文件
            wb.write(baout);
            byte [] bookByteAry = baout.toByteArray();
            in = new ByteArrayInputStream(bookByteAry);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return in;

    }

    public static InputStream WriteRebackStatWithSite(List<RebackStatWithSite> list) {
        int pages= list.size()/PAGE_COUNTS+1;
        System.out.println("共： "+pages+"页");
        ArrayList<String> sheetName = new ArrayList<String>();
        for (int page=1;page<=pages;page++){
            sheetName.add("sheet"+page);
        }

        SXSSFWorkbook wb=new SXSSFWorkbook();
        ArrayList<String> namelist = ExcelColumns.getRebackStatWithSiteTitle();

        long ta =0;
        long tb =0;
        ta = System.currentTimeMillis();
        for (int page=1;page<=pages;page++){
            wb.createSheet(sheetName.get(page-1));
            SXSSFSheet sheet = wb.getSheetAt(page-1);
            int rowindex = 0;
            SXSSFRow row = sheet.createRow(rowindex);
            int colindex = 0;
            for (String str:namelist){
                SXSSFCell cell = row.createCell(colindex);
                cell.setCellValue(str);
                colindex++;
            }
            rowindex++;

            Cell cell;
            int start=(page-1)*(PAGE_COUNTS-1);
            int end=page*(PAGE_COUNTS-1)-1;
            if(page==pages){
                end=list.size()-1;
            }
            for (int listindex=start;listindex<=end;listindex++){
                row = sheet.createRow(rowindex);
                List<String> electricList = MyUtils.getList(list.get(listindex));
                for (int i=0;i<electricList.size();i++){
                    String str =electricList.get(i);
                    cell = row.createCell(i);
                    cell.setCellValue(str);
                }
                rowindex++;
            }
        }

        tb = System.currentTimeMillis();
        System.out.println("总耗时："+(tb-ta));
        InputStream in = null;
        try{
            //临时缓冲区
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            //创建临时文件
            wb.write(baout);
            byte [] bookByteAry = baout.toByteArray();
            in = new ByteArrayInputStream(bookByteAry);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return in;
    }

    public static InputStream WriteRebackStats(List<RebackStat> list) {
        int pages= list.size()/PAGE_COUNTS+1;
        System.out.println("共： "+pages+"页");
        ArrayList<String> sheetName = new ArrayList<String>();
        for (int page=1;page<=pages;page++){
            sheetName.add("sheet"+page);
        }

        SXSSFWorkbook wb=new SXSSFWorkbook();
        ArrayList<String> namelist = ExcelColumns.getRebackStatTitle();

        long ta =0;
        long tb =0;
        ta = System.currentTimeMillis();
        for (int page=1;page<=pages;page++){
            wb.createSheet(sheetName.get(page-1));
            SXSSFSheet sheet = wb.getSheetAt(page-1);
            int rowindex = 0;
            SXSSFRow row = sheet.createRow(rowindex);
            int colindex = 0;
            for (String str:namelist){
                SXSSFCell cell = row.createCell(colindex);
                cell.setCellValue(str);
                colindex++;
            }
            rowindex++;

            Cell cell;
            int start=(page-1)*(PAGE_COUNTS-1);
            int end=page*(PAGE_COUNTS-1)-1;
            if(page==pages){
                end=list.size()-1;
            }
            for (int listindex=start;listindex<=end;listindex++){
                row = sheet.createRow(rowindex);
                List<String> electricList = MyUtils.getList(list.get(listindex));
                for (int i=0;i<electricList.size();i++){
                    String str =electricList.get(i);
                    cell = row.createCell(i);
                    cell.setCellValue(str);
                }
                rowindex++;
            }
        }

        tb = System.currentTimeMillis();
        System.out.println("总耗时："+(tb-ta));
        InputStream in = null;
        try{
            //临时缓冲区
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            //创建临时文件
            wb.write(baout);
            byte [] bookByteAry = baout.toByteArray();
            in = new ByteArrayInputStream(bookByteAry);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return in;

    }

    public static InputStream WriteRebackStatWithReport(List<RebackStatWithReport> list) {
        int pages= list.size()/PAGE_COUNTS+1;
        System.out.println("共： "+pages+"页");
        ArrayList<String> sheetName = new ArrayList<String>();
        for (int page=1;page<=pages;page++){
            sheetName.add("sheet"+page);
        }

        SXSSFWorkbook wb=new SXSSFWorkbook();
        ArrayList<String> namelist = ExcelColumns.getRebackStatWithReportTitle();

        long ta =0;
        long tb =0;
        ta = System.currentTimeMillis();
        for (int page=1;page<=pages;page++){
            wb.createSheet(sheetName.get(page-1));
            SXSSFSheet sheet = wb.getSheetAt(page-1);
            int rowindex = 0;
            SXSSFRow row = sheet.createRow(rowindex);
            int colindex = 0;
            for (String str:namelist){
                SXSSFCell cell = row.createCell(colindex);
                cell.setCellValue(str);
                colindex++;
            }
            rowindex++;

            Cell cell;
            int start=(page-1)*(PAGE_COUNTS-1);
            int end=page*(PAGE_COUNTS-1)-1;
            if(page==pages){
                end=list.size()-1;
            }
            for (int listindex=start;listindex<=end;listindex++){
                row = sheet.createRow(rowindex);
                List<String> electricList = MyUtils.getList(list.get(listindex));
                for (int i=0;i<electricList.size();i++){
                    String str =electricList.get(i);
                    cell = row.createCell(i);
                    cell.setCellValue(str);
                }
                rowindex++;
            }
        }

        tb = System.currentTimeMillis();
        System.out.println("总耗时："+(tb-ta));
        InputStream in = null;
        try{
            //临时缓冲区
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            //创建临时文件
            wb.write(baout);
            byte [] bookByteAry = baout.toByteArray();
            in = new ByteArrayInputStream(bookByteAry);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return in;
    }
}
