package com.tower.nanan.poi;

import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 目前函数又个缺陷，便是starElement函数中发现新行的策略有问题
 */
public class ExcelRead {

    /**
     * 代表Excel中必须有值的起始列(A、B、C....AA、AB...)
     */
    private static final String indexC4Data = "A";

    /**
     * 存储所有行的值
     */
    public List<List<IndexValue>> dataList = new ArrayList<List<IndexValue>>();
    /*
     * 临时存储当前行的值
     */
    private List<IndexValue> rowData;

    private final int startRow;
    private final int endRow;
    private int currentRow = 0;

    private final String filename;


    public ExcelRead(String filename, int startRow) throws Exception {
        //System.out.println("ExcelRead构造方法");
        if (StringUtils.isBlank(filename))
            throw new Exception("文件名不能空");
        this.filename = filename;
        this.startRow = startRow;
        this.endRow = getEndRow(filename);
        //System.out.println("最大行"+this.endRow );
        processFirstSheet();
    }

    /**
     * 指定获取第一个sheet
     *
     * @param
     * @throws Exception
     */
    private void processFirstSheet() throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();

        XMLReader parser = fetchSheetParser(sst);

        // To look up the Sheet Name / Sheet Order / rID,
        // you need to process the core Workbook stream.
        // Normally it's of the form rId# or rSheet#
        InputStream sheet1 = r.getSheet("rId1");
        InputSource sheetSource = new InputSource(sheet1);
        parser.parse(sheetSource);
        sheet1.close();
        pkg.close();
    }

    private XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        ContentHandler handler = new PagingHandler(sst);
        parser.setContentHandler(handler);
        return parser;
    }

    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private class PagingHandler extends DefaultHandler {
        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private String index = null;

        private PagingHandler(SharedStringsTable sst) {
            this.sst = sst;
        }

        /**
         * 每个单元格开始时的处理
         */
        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            // c => cell
            if (name.equals("c")) {
                // Print the cell reference
                // System.out.print(attributes.getValue("r") + " - ");

                index = attributes.getValue("r");
                //  System.out.println(index);
                if (index.contains("N")) {
                    //  System.out.println("##" + attributes + "##");
                }

                // 这是一个新行
                if (Pattern.compile("^"+indexC4Data+"[0-9]+$").matcher(index).find()) {

                    // 存储上一行数据
                    if (rowData != null && isAccess() && !rowData.isEmpty()) {
                        dataList.add(rowData);
                    }
                    rowData = new ArrayList<IndexValue>();
                    ;// 新行要先清除上一行的数据
                    currentRow++;// 当前行+1
                    // System.out.println(currentRow);
                }
                if (isAccess()) {
                    // Figure out if the value is an index in the SST
                    String cellType = attributes.getValue("t");
                    if (cellType != null && cellType.equals("s")) {
                        nextIsString = true;
                    } else {
                        nextIsString = false;
                    }
                }

            }
            // Clear contents cache
            lastContents = "";
        }

        /**
         * 每个单元格结束时的处理
         */
        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            if (isAccess()) {
                // Process the last contents as required.
                // Do now, as characters() may be called more than once
                if (nextIsString) {
                    int idx = Integer.parseInt(lastContents);
                    lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                    nextIsString = false;
                }

                // v => contents of a cell
                // Output after we've seen the string contents
                if (name.equals("v")) {
                    // System.out.println(lastContents);

                    rowData.add(new IndexValue(index, lastContents));

                }
            }

        }

        /**
         * 目前流的方式值支持  Excel单元格是文本  格式；日期、数字、公式不支持
         */
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (isAccess()) {
                lastContents += new String(ch, start, length);
            }

        }

        /**
         * 如果文档结束后，发现读取的末尾行正处在当前行中，存储下这行
         * （存在这样一种情况，当待读取的末尾行正好是文档最后一行时，最后一行无法存到集合中，
         * 因为最后一行没有下一行了，所以不为启动starElement()方法， 当然我们可以通过指定最大列来处理，但不想那么做，扩展性不好）
         */
        @Override
        public void endDocument() throws SAXException {
            if (rowData != null && isAccess() && !rowData.isEmpty()) {
                dataList.add(rowData);
                // System.out.println("--end");
            }

        }

    }

    private boolean isAccess() {
        if (currentRow >= startRow && currentRow <= endRow) {
            return true;
        }
        return false;
    }

    private class IndexValue {
        String v_index;
        String v_value;

        public IndexValue(String v_index, String v_value) {
            super();
            this.v_index = v_index;
            this.v_value = v_value;
        }

        @Override
        public String toString() {
            return "IndexValue [v_index=" + v_index + ", v_value=" + v_value + "]";
        }

        /**
         * 去掉数字部分（行信息），直接比较英文部分（列信息），计算前后两个值相距多少空列
         * @param p
         * @return
         */
        public int getLevel(IndexValue p) {

			/*char[] other = p.v_index.replaceAll("[0-9]", "").toCharArray();
			char[] self = this.v_index.replaceAll("[0-9]", "").toCharArray();
			if (other.length != self.length)
				return -1;
			for (int i = 0; i < other.length; i++) {
				if (i == other.length - 1) {
					return self[i] - other[i];
				} else {
					if (self[i] != other[i]) {
						return -1;
					}
				}
			}
			return -1;*/

            String other = p.v_index.replaceAll("[0-9]", "");
            String self = this.v_index.replaceAll("[0-9]", "");
            return fromNumberSystem26(self)-fromNumberSystem26(other);

        }
    }

    /**
     * 获取真实的数据（处理空格）
     *
     * @return
     * @throws Exception
     */
    public List<List<String>> getMyDataList() throws Exception {

        List<List<String>> myDataList = new ArrayList<List<String>>();
        if (dataList == null || dataList.size() <= 0)
            return myDataList;
        /*
         * 是否是最后一行的数据
         */
        boolean islastRow = false;
        //System.out.println("dataList:"+dataList.size());
        for (int i = 0; i < dataList.size(); i++) {
            List<IndexValue> i_list = dataList.get(i);
            List<String> row = new ArrayList<String>();
            int j = 0;

            for (; j < i_list.size() - 1; j++) {
                // 获取当前值,并存储
                IndexValue current = i_list.get(j);
                //去掉空格
                String tempV = current.v_value!=null?current.v_value.trim():current.v_value;
                row.add(tempV);
                // 预存下一个
                IndexValue next = i_list.get(j + 1);
                // 获取差值
                int level = next.getLevel(current);
				/*if(i==2214){
					System.out.println("--"+i);
				}*/
                if (level <= 0){
                    System.err.println("---!!!到达最后一行，行号："+(i+1)+";level:"+level+"[超出处理范围]");
                    islastRow = true;
                    break;
                }
                //将差值补充为null，
                for (int k = 0; k < level - 1; k++) {
                    row.add(null);
                }
            }
            /*
             * 每行的最后一个值，留在最后插入
             * 但最后一行除外
             */
            if(!islastRow){
                row.add(i_list.get(j).v_value);
            }
            myDataList.add(row);

        }
        System.out.println("读取表格完成");
        return myDataList;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("d:/2.xlsx");
        //System.out.println(new ExcelRead(file.getPath(), 1, 50).getMyDataList());
    }

    public static int getEndRow(String filename) throws IOException {
        FileInputStream inp = new FileInputStream(filename);
        XSSFWorkbook wb = new XSSFWorkbook(inp);
        XSSFSheet sheet = wb.getSheetAt(0); //
        int rowNum=sheet.getLastRowNum();//获得总行数
        return rowNum+1;
    }


    public static int fromNumberSystem26(String s){
        if (StringUtils.isBlank(s)) return 0;
        s = s.toUpperCase();
        int n = 0;
        char[] arr = s.toCharArray();
        for (int i = arr.length - 1, j = 1; i >= 0; i--, j *= 26){
            char c = arr[i];
            if (c < 'A' || c > 'Z') return 0;
            //A的ASCII值为65
            n += ((int)c - 64) * j;
        }
        return n;
    }


}
