/**
 * author: loserStar
 * date: 2018年5月29日下午9:48:47
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * author: loserStar
 * date: 2019年12月27日上午9:28:35
 * remarks:对路径输出时候检测如果没有该目录就创建
 * 需优化的地方：读取excel时候要能准确报出是哪个sheet哪行哪列的什么问题
 */
public class LoserStarExcelUtils {
	
	
	/**
	 * 得到poi的excel的workbook对象
	 * @param filePath 文件路径
	 * @return
	 * @throws IOException
	 */
	public static Workbook getWorkbook(String filePath) throws IOException {
		if (filePath.endsWith("xlsx")) {
			return getWorkbook(new File(filePath));
		} else if (filePath.endsWith("xls")) {
			return getWorkbook(new File(filePath));
		} else {
			new Exception("上传文件必须后缀必须为xls或xlsx！");
		}
		return null;
	}
	/**
	 * 得到poi的excel的workbook对象
	 * @param file 文件对象
	 * @return
	 * @throws IOException
	 */
	public static Workbook getWorkbook(File file) throws IOException {
		if (file.getAbsolutePath().endsWith("xlsx")) {
			return getWorkbook(new FileInputStream(file), "xlsx");
		} else if (file.getAbsolutePath().endsWith("xls")) {
			return getWorkbook(new FileInputStream(file), "xls");
		} else {
			new Exception("上传文件必须后缀必须为xls或xlsx！");
		}
		return null;
	}
	/**
	 * 得到poi的excel的workbook对象
	 * @param in 输入流对象
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static Workbook getWorkbook(InputStream in,String fileName) throws IOException {
		POIFSFileSystem fs = null;
		Workbook workbook = null;
		if (fileName.endsWith("xlsx")) {
			workbook = new XSSFWorkbook(in);
		} else if (fileName.endsWith("xls")) {
			fs = new POIFSFileSystem(in);
			workbook = new HSSFWorkbook(fs);
		} else {
			new Exception("上传文件必须后缀必须为xls或xlsx！");
		}
		return workbook;
	}
	
	/**
	 * 创建一个workbook和一个sheet
	 * @param sheetName sheet的名称
	 * @param isNew 是否创建新版本的excel。true：2007版本及以上的excel（xlsx）,false：97-2003版本的excel（xls）
	 * @return
	 */
	public static Workbook createWorkbook(String sheetName,boolean isNew) {
		Workbook workbook =null;
		if (isNew) {
			workbook = new XSSFWorkbook();
		}else {
			workbook = new HSSFWorkbook();
		}
		workbook.createSheet();
		if (sheetName!=null&&!sheetName.equals("")) {
			workbook.setSheetName(0, sheetName);
		}
		return workbook;
	}
	
	/**
	 * 把一个poi的excel的workbook对象输出
	 * 如果是删除到web里使用response的outputstream，相当于下载文件，要指定文件名的话，加入如下代码：
	 * HttpServletResponse response = getResponse();
			String fileName = LoserStarDateUtils.format(new Date(), "yyyyMMddHHmmss")+".xlsx";
			response.addHeader("Content-Disposition","attachment;filename=" + new String(java.net.URLEncoder.encode(fileName, "UTF-8")));
			response.setContentType("application/octet-stream");
	 * @param workbook excel对象
	 * @param outputStream 输出流
	 * @throws Exception 
	 */
	public static void writeWorkbook(Workbook workbook,OutputStream outputStream) throws Exception {
		if (outputStream==null) {
			throw new Exception("outputStream不能为null");
		}
		workbook.write(outputStream);
		outputStream.flush();
		outputStream.close();
	}
	/**
	 * 把一个poi的excel的workbook对象输出
	 * @param workbook excel对象
	 * @param file 文件对象
	 * @throws Exception
	 */
	public static void writeWorkbook(Workbook workbook,File file) throws Exception {
		if (file==null) {
			throw new Exception("file不能为null");
		}
		writeWorkbook(workbook, new FileOutputStream(file));
	}
	/**
	 * 把一个poi的excel的workbook对象输出
	 * @param workbook excel对象
	 * @param filePath 文件路径
	 * @throws Exception
	 */
	public static void writeWorkbook(Workbook workbook,String filePath) throws Exception {
		if (filePath==null||filePath.equals("")) {
			throw new Exception("filePath不能为空");
		}
		
		//提取目录
		int i1 = filePath.lastIndexOf("\\");
		int i2 = filePath.lastIndexOf("/");
		int index = 0;
		if (i1>i2) {
			index = i1;
		}else{
			index = i2;
		}
		String dir = filePath.substring(0, index+1);
		//如果目录不存在就创建目录
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		writeWorkbook(workbook, new File(filePath));
	}
	
	
	/**
	 * @param workbook poi文件poi对象
	 * @param hideRowIndex 隐藏行的index，此行的值当做key
	 * @param startRowIndex 数据开始行的index
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, List<Map<String, String>>> readWorkbookToMap(Workbook workbook, int hideRowIndex, int startRowIndex)
			throws Exception {
		Map<String, List<Map<String, String>>> excelMap= new HashMap<String, List<Map<String, String>>>();
		CellStyle cellStyle = null;
		if (workbook==null) {
			throw new Exception("workbook不能为null！");
		}
		int sheetsCounts = workbook.getNumberOfSheets();
		for (int i = 0; i < sheetsCounts; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			String[] cellNames;
			Row fisrtRow = sheet.getRow(hideRowIndex);

			if (null == fisrtRow) {
				continue;
			}

			int curCellNum = fisrtRow.getLastCellNum();
			cellNames = new String[curCellNum];
			for (int m = 0; m < curCellNum; m++) {
				Cell cell = fisrtRow.getCell(m);
				// 设置该列的样式是字符串
//				cell.setCellType(CellType.STRING);
				// 取得该列的字符串值
				cellNames[m] = cell.getStringCellValue();
			}

			int rowNum = sheet.getLastRowNum();	
			for (int j = startRowIndex; j <= rowNum; j++) {
				// 一行数据对于一个Map
				Map<String, String> rowMap = new HashMap<String, String>();
				// 取得某一行
				Row row = sheet.getRow(j);
				int cellNum = row.getLastCellNum();
				// 遍历每一列
				System.out.println("------row:"+j);
				for (int k = 0; k < cellNum; k++) {
					System.out.println("-------col:"+k);
					Cell cell = row.getCell(k);
					// 保存该单元格的数据到该行中
					String cellValue = "";
					if (cell!=null) {
						cell.setCellStyle(cellStyle);
//						cell.setCellType(CellType.STRING);
						cellValue = cell.getStringCellValue();
					}
					rowMap.put(cellNames[k], cellValue);
				}
				// 保存该行的数据到该表的List中
				list.add(rowMap);
			}
			// 将该sheet表的表名为key，List转为json后的字符串为Value进行存储
			excelMap.put(sheet.getSheetName(), list);
		}
		return excelMap;
	}
	
	
	/**
	 * 把一个List<LinkedHashMap<String, Object>>对象写入到一个workbook的对象中（操作第一个的sheet）
	 * @param workbook workbook对象，不能为null，如果是新new的workbook没有sheet的话会自动创建一个sheet
	 * @param list 内容
	 * @param startRowIndex 从第几行开始写的索引
	 * @param startColumnIndex 从第几列开始写的索引
	 * @return 返回workbook对象
	 * @throws Exception
	 */
	public static Workbook processWorkbookWriteListMap(Workbook workbook ,List<LinkedHashMap<String, Object>> list,int startRowIndex,int startColumnIndex) throws Exception {
		return processWorkbookWriteListMap(workbook,list,startRowIndex,startColumnIndex,null);
	}
	
	/**
	 * 把一个List<LinkedHashMap<String, Object>>对象写入到一个workbook的对象中（操作第一个的sheet）
	 * @param workbook workbook对象，不能为null，如果是新new的workbook没有sheet的话会自动创建一个sheet
	 * @param list 内容
	 * @param startRowIndex 从第几行开始写的索引
	 * @param startColumnIndex 从第几列开始写的索引
	 * @param columnsWidth 每一列的宽度，从索引0开始（字符）
	 * @return 返回workbook对象
	 * @throws Exception
	 */
	public static Workbook processWorkbookWriteListMap(Workbook workbook ,List<LinkedHashMap<String, Object>> list,int startRowIndex,int startColumnIndex,List<Integer> columnsWidthList) throws Exception {
		Sheet sheet = null;
		if (workbook!=null) {
			int sheetCount = workbook.getNumberOfSheets();
			if (sheetCount<=0) {
				sheet = workbook.createSheet();
			}else {
				sheet = workbook.getSheetAt(0);
			}
			if (columnsWidthList!=null&&columnsWidthList.size()>0) {
				for(int i=0;i<columnsWidthList.size();i++) {
					int width = columnsWidthList.size()*256;
					sheet.setColumnWidth(i,width );
				}
			}
		}else {
			throw new Exception("workbook不能为null!");
		}
		for (int i =0; i < list.size(); i++) {//遍历行
			Row row = sheet.createRow(startRowIndex+i);//从第几行开始写
			Map<String, Object> map = list.get(i);
			int j=startColumnIndex;
			for (Entry<String, Object> entry : map.entrySet()) { 
				String key = entry.getKey();
				Object value = entry.getValue();
				Cell cell = row.createCell(j);
/*				CellStyle cellStyle=workbook.createCellStyle();       
				cellStyle.setWrapText(true);
				cell.setCellStyle(cellStyle);*/
				String valueStr = String.valueOf(value);
				cell.setCellValue(valueStr);
				j++;
			}
		}
		return workbook;
	}
	
	
	/**
	 * poi读取excel中date类型的处理办法，如果日期读出来是一些数字，这个数字是什么呢？是以1900年为原点，到你的这个日期，之间经过的天数，所以用1900年的日期+天数就得到实际的日期
	 * poi3.15中的CellType只支持number和string两种类型，根本区分不出date，所以只能人工判断去转换了
	 * @param dateNumber excel中拿到的数字
	 * @return
	 */
	public static Date dateNumberToDate(int dateNumber) {

		//原始方式
		Calendar calendar = new GregorianCalendar(1900,0,-1);
		calendar.add(Calendar.DAY_OF_MONTH,dateNumber);
		//自己封装的方式
//		Date date1900 = LoserStarDateUtils.fromString("1900-01--1", "yyyy-MM-dd");
//		Date date = LoserStarDateUtils.addDay(date1900, newsdate);
		
		return calendar.getTime();
	}
	
	public static void main(String[] args) {
		try {
			/**
			 * 三个步骤
			 * 1.调用getWorkbook拿到workbook对象
			 * 2.调用各种方法操作workbook对象
			 * 3.调用writeWorkbook输出workbook
			 */
			//生成测试数据
			List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String,Object>>();
			for (int i = 0; i < 20; i++) {
				LinkedHashMap< String, Object> map = new LinkedHashMap<String, Object>();
				map.put("id", i+"行"+"0列");
				map.put("id2",  i+"行"+"1列");
				map.put("id3",  i+"行"+"2列");
				map.put("id4",  i+"行"+"3列");
				map.put("id5",  i+"行"+"4列");
				map.put("id6",  i+"行"+"5列");
				list.add(map);
			}
			
			File sourceFile = new File("C:\\export.xlsx");
			File newFile = new File("C:\\export2.xlsx");
//			Workbook workbook = getWorkbook(sourceFile);//读取源文件当做模板输出
			Workbook workbook =createWorkbook("loserStar测试嘎嘎", true);//生成新文件(包括sheet了)
			workbook = processWorkbookWriteListMap(workbook, list, 19, 0);
			writeWorkbook(workbook, new FileOutputStream(newFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}