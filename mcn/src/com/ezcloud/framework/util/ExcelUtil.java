package com.ezcloud.framework.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;
import com.ezcloud.utility.FileUtil;
/**
 * 
 * 
 * Excel工具类
 * @author JianBoTong
 *
 */
public class ExcelUtil {

	
	 private static Logger logger = Logger.getLogger(ExcelUtil.class);
	    
	 public static List<Map<String ,Object>> parseExcel(String filePath) throws FileNotFoundException, IOException
	 {
	    	List<Map<String ,Object>>sheetList =new ArrayList<Map<String,Object>>();
	    	List<Object> sheetData =null;
	    	HSSFWorkbook  workbook = new HSSFWorkbook(new FileInputStream(filePath));
	    	HSSFSheet sheet=null;
	    	Map <String ,Object>sheetMap=null;
	    	int sheetNum = workbook.getNumberOfSheets();
	    	System.out.println("\n");
	    	System.out.println("\n=============sheet num====>>"+sheetNum);
	    	for(int i=0; i< sheetNum; i++)
	    	{
	    		sheet = workbook.getSheetAt(i);
	    		String sheetName =sheet.getSheetName();
	    		sheetData =getDatasInSheet(workbook,i);
	    		sheetMap =new HashMap<String ,Object>();
	    		sheetMap.put("index", String.valueOf(i));
	    		sheetMap.put("name", sheetName);
	    		sheetMap.put("data", sheetData);
	    		sheetMap.put("rows", sheetData.size());
	    		sheetList.add(sheetMap);
	    	}
	    	return sheetList;
	    }
	    

	    /**
	     * 获得表中的数据
	     * 
	     * @param sheetNumber
	     *            表格索引(EXCEL 是多表文档,所以需要输入表索引号)
	     * @return 由LIST构成的行和表
	     * @throws FileNotFoundException
	     * @throws IOException
	     * 
	     */
	    public static List<Object> getDatasInSheet(HSSFWorkbook workbook,int sheetNumber)
	            throws FileNotFoundException, IOException {
	        List<Object> result = new ArrayList<Object>();
	        // 获得指定的表
	        HSSFSheet sheet = workbook.getSheetAt(sheetNumber);
	        // 获得数据总行数
	        int rowCount = sheet.getLastRowNum();
	        logger.info("found excel rows count: " + rowCount);
	        if (rowCount < 1) {
	            return result;
	        }

	        // 逐行读取数据
	        for (int rowIndex = 0; rowIndex <= rowCount; rowIndex++) {
	            // 获得行对象
	            HSSFRow row = sheet.getRow(rowIndex);
	            if (row != null) {
	                List<Object> rowData = new ArrayList<Object>();
	                // 获得本行中单元格的个数
	                int columnCount = row.getLastCellNum();
	                // 获得本行中各单元格中的数据
	                for (short columnIndex = 0; columnIndex < columnCount; columnIndex++) {
	                    @SuppressWarnings("deprecation")
						HSSFCell cell = row.getCell(columnIndex);
	                    // 获得指定单元格中数据
	                    Object cellStr = getCellString(cell);
	                    rowData.add(cellStr);
	                }
	                result.add(rowData);
	            }
	        }
	        return result;
	    }
	    
	    /**
	     * 获得单元格中的内容
	     * 
	     * @param cell
	     * @return
	     */
	    protected static Object getCellString(HSSFCell cell) {
	        Object result = null;
	        if (cell != null) {
	            int cellType = cell.getCellType();
	            switch (cellType) {
	            case HSSFCell.CELL_TYPE_STRING:
	                result = cell.getRichStringCellValue().getString();
	                break;
	            case HSSFCell.CELL_TYPE_NUMERIC:
	            {
	            	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//	                result = cell.getNumericCellValue();
	            	 result = cell.getRichStringCellValue().getString();
	                break;
	            }
	            	
	            case HSSFCell.CELL_TYPE_FORMULA:
	            {
	            	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//	            	result = cell.getNumericCellValue();
	            	result = cell.getRichStringCellValue().getString();
	                break;
	            }
	            case HSSFCell.CELL_TYPE_ERROR:
	                result = null;
	                break;
	            case HSSFCell.CELL_TYPE_BOOLEAN:
	                result = cell.getBooleanCellValue();
	                break;
	            case HSSFCell.CELL_TYPE_BLANK:
	                result = null;
	                break;
	            }
	        }
	        return result;
	    }

	    /**
	     * 
	     * @param titleDs 中文标题列表
	     * @param keyDs   数据字段key列表
	     * @param dataDs  数据列表
	     * @param out_path 文件保存路径
	     * @throws IOException
	     */
	    public static void writeExcel(DataSet titleDs,DataSet keyDs,DataSet dataDs,
	    		String out_path,String fileName,String sheetName,int rowHeight) throws IOException
	    {
	        String exportPath = out_path+"/"+fileName;
	        FileUtil.mkdir(out_path);
	        OutputStream out = new FileOutputStream(new File(exportPath));  
	  
	        // 声明一个工作薄  
	        HSSFWorkbook workbook = new HSSFWorkbook();  
	        // 生成一个表格  
	        if(StringUtils.isEmptyOrNull(sheetName))
	        {
	        	sheetName ="SHEET_"+DateUtil.getCurrentDateTime().replaceAll(":", "").replaceAll("-", "").replaceAll(" ", "");
	        }
	        HSSFSheet sheet = workbook.createSheet(sheetName);  
	        // 设置表格默认列宽度为15个字节  
	        sheet.setDefaultColumnWidth(15);  
	        // 设置标题  
	        HSSFCellStyle titleStyle = workbook.createCellStyle();  
	        // 居中显示  
	        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
	       
	        // 标题字体  
	        HSSFFont titleFont = workbook.createFont();  
	        // 字体大小  
//	        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
	        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
	        titleStyle.setFont(titleFont);  
	  
	        HSSFCellStyle contentStyle = workbook.createCellStyle();  
	        contentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);  
	        HSSFFont contentFont = workbook.createFont();  
	        contentFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
	        contentStyle.setFont(contentFont);  
	  
	        // 产生表格标题行  
	        HSSFRow row = sheet.createRow(0);
	        for (int i = 0; i < titleDs.size(); i++) 
	        {  
	            HSSFCell cell = row.createCell(i);  
	            HSSFRichTextString text = new HSSFRichTextString(String.valueOf(titleDs.get(i)));  
	            cell.setCellValue(text);  
	            cell.setCellStyle(titleStyle);  
	        }  
	        int rowCount = 1;  
	        for (int i = 0; i < dataDs.size(); i++, rowCount++) 
	        {  
	            HSSFRow dataRow = sheet.createRow(rowCount);  
	            if(rowHeight > 0)
	            {
	            	dataRow.setHeight((short)rowHeight);
	            }
	            Row data_row = (Row)dataDs.get(i);  
	            for (int j = 0; j < keyDs.size(); j++) 
	            {  
	            	HSSFCell cell = dataRow.createCell(j);
	            	String value =data_row.getString(String.valueOf(keyDs.get(j)));
	            	if(StringUtils.isEmptyOrNull(value))
	            	{
	            		value ="";
	            	}
	            	cell.setCellValue(value);  
		            cell.setCellStyle(contentStyle);  
	            }
	        }  
	        // 自动调整列宽
	        for (int i = 0; i < titleDs.size(); i++) 
	        {  
	        	sheet.autoSizeColumn((short)i);
	        	int colWidth =sheet.getColumnWidth(i);
	        	if(colWidth>15000)
	        	{
	        		colWidth =50*256;
	        		sheet.setColumnWidth(i, 50*256);
	        	}
	        }  
	        workbook.write(out);  
	        out.close();
	    }  

	  //自定义的方法,插入某个图片到指定索引的位置
	    public static void insertImage(HSSFWorkbook wb,HSSFPatriarch pa,byte[] data,int row,int column,int index)
	    {
//	    	System.out.println("插入图片===>>row:"+row+" column:"+column+" index:"+index);
	       int x1=0;
	       int y1=0;
	       int x2=x1+255;
	       int y2=255;
	       HSSFClientAnchor anchor = new HSSFClientAnchor(x1,y1,x2,y2,(short)column,row,(short)column,row);
	        anchor.setAnchorType(2);
	       pa.createPicture(anchor , wb.addPicture(data,HSSFWorkbook.PICTURE_TYPE_JPEG));
	    }
	    
	    //从图片里面得到字节数组
	    public static  byte[] getImageData(BufferedImage bi)
	    {
	       try{
	            ByteArrayOutputStream bout=new ByteArrayOutputStream();
	            ImageIO.write(bi,"PNG",bout);
	           return bout.toByteArray();
	        }catch(Exception exe){
	            exe.printStackTrace();
	           return null;
	        }
	    }
	    
	    /**
	     * 
	     * @param col_index 指定的列，列下标从0开始
	     * @param begin_row_index 指定的行，表示从哪一行开始替换，下标从0开始
	     * @throws IOException 
	     * @throws FileNotFoundException 
	     */
	    public static void replaceForColumnFromImgPathToImage(int col_index,int begin_row_index,String filePath) 
	    		throws FileNotFoundException, IOException
	    {
	    	HSSFWorkbook  workbook = new HSSFWorkbook(new FileInputStream(filePath));
	    	HSSFSheet sheet=null;
	    	int sheetNum = workbook.getNumberOfSheets();
	    	//声明一个画图的顶级管理器
	    	 HSSFPatriarch patriarch = null;
	    	for(int i=0; i< sheetNum; i++)
	    	{
	    		sheet = workbook.getSheetAt(i);
	    		patriarch = sheet.createDrawingPatriarch();
	    		// 获得指定的表
		        sheet = workbook.getSheetAt(i);
		        // 获得数据总行数
		        int rowCount = sheet.getLastRowNum();
		        // 逐行读取数据
		        for (int rowIndex = 0,img_index=1; rowIndex <= rowCount; rowIndex++) {
		            // 获得行对象
		            HSSFRow row = sheet.getRow(rowIndex);
		            if (rowIndex >= begin_row_index && row != null) {
		                List<Object> rowData = new ArrayList<Object>();
		                // 获得本行中单元格的个数
		                int columnCount = row.getLastCellNum();
		                // 获得本行中各单元格中的数据
		                for (short columnIndex = 0; columnIndex < columnCount; columnIndex++) {
		                	if(columnIndex == col_index)
		                	{
		                		HSSFCell cell = row.getCell(columnIndex);
		                		// 获得指定单元格中数据
		                		Object cellStr = getCellString(cell);
		                		rowData.add(cellStr);
		                		String img_path =cellStr.toString();
		                		if(! StringUtils.isEmptyOrNull(img_path))
		                		{
		                			File file=new File(img_path);
		                			if(file.exists() && file.isFile())
		                			{
		                				URL imgUrl = new URL("file:///" + file.getPath()); 
		                				insertImage(workbook,patriarch,getImageData(ImageIO.read(imgUrl)),rowIndex,columnIndex,img_index);
		                				img_index ++;
		                			}
		                		}
		                	}
		                }
		            }
		        }
	    	}
	    	FileOutputStream fout=new FileOutputStream(filePath);
	    	//输出到文件
	    	workbook.write(fout);
	    	fout.close();
	    }
	    
//	    @SuppressWarnings("unchecked")
//		public static void main(String[] args) throws Exception {
//	    	String filePath="/users/JianBoTong/Desktop/test.xls";
//	       List<Map<String,Object>>sheetlist =ExcelUtil.parseExcel(filePath);
//	       String index ="";
//	       String sheetName="";
//	       List<Object> sheetData=null;
//	        for (int i = 0; i < sheetlist.size(); i++) {// 显示数据
//	            Map<String,Object> map=  (Map<String,Object>)sheetlist.get(i);
//	            index=(String)map.get("index");
//	            sheetName=(String)map.get("name");
//	            sheetData=(List<Object>)map.get("data");
//	            System.out.println("=============sheet:"+index+" sheet name:"+sheetName);
//	            for (short n = 0; n < sheetData.size(); n++) {
//	            	List<Object> rowData =(List<Object>)sheetData.get(n);
////	            	System.out.println("row----------------------->> "+n);
//	            	for(int m=0;m<rowData.size();m++)
//	            	{
//	            		Object value = rowData.get(m);
//	 	                String data = String.valueOf(value);
//	 	                System.out.print(data + "\t");
//	            	}
//	            	System.out.println();
//	            }
//	            System.out.println();
//	        }
//	    }
	    
	    @SuppressWarnings("unchecked")
//		public static void main(String[] args) throws Exception 
//	    {
//	    	DataSet titleDs =new DataSet();
//	    	for(int i=0;i<10;i++)
//	    	{
//	    		titleDs.add("title"+i);
//	    	}
//	    	DataSet keyDs =new DataSet();
//	    	for(int i=0;i<10;i++)
//	    	{
//	    		keyDs.add("title"+i);
//	    	}
//	    	DataSet dataDs =new DataSet();
//	    	for(int i=0;i<10;i++)
//	    	{
//	    		Row row =new Row();
//	    		row.put("title0", "1");
//	    		row.put("title1", "1");
//	    		row.put("title2", "1");
//	    		row.put("title3", "1");
//	    		row.put("title4", "1");
//	    		row.put("title5", "1");
//	    		row.put("title6", "1");
//	    		row.put("title7", "1");
//	    		row.put("title8", "1");
//	    		row.put("title9", "1");
//	    		dataDs.add(row);
//	    	}
//	    	
////	    	String out_path="/Users/TongJianbo/work/测试.xls";
//	    	String out_path="/usr/local/tomcat/tomcat7/webapps/mcn/resources/export_excel/10007";
//	    	String fileName ="2015企业考勤汇总表.xls";
//	    	String sheetName="测试表";
//	    	ExcelUtil.writeExcel(titleDs, keyDs, dataDs, out_path,fileName, sheetName);
//	    }

	    
	    public static void main(String[] args) throws FileNotFoundException, IOException {
			String excel_path ="C:\\Users\\Administrator\\Downloads\\用户打卡表.xls";
			ExcelUtil.replaceForColumnFromImgPathToImage(6, 1, excel_path);
		}
}
