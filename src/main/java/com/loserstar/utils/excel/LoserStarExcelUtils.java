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
import java.io.FileNotFoundException;
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

import com.loserstar.utils.file.LoserStarFileUtil;
import com.loserstar.utils.json.LoserStarJsonUtil;

/**
 * author: loserStar 
 * date: 2018年5月29日下午9:48:47 
 * remarks: excel操作相关
 */
public class LoserStarExcelUtils {
	
	/**
	 * @param filePath excel文件路径
	 * @param hideRowIndex 隐藏行的index，此行的值当做key
	 * @param startRowIndex 数据开始行的index
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, List<Map<String, String>>> readExcelToMap(String filePath, int hideRowIndex, int startRowIndex)
			throws Exception {
		if (filePath.endsWith("xlsx")) {
			return readExcelToMap(new FileInputStream(filePath),"xlsx",hideRowIndex,startRowIndex);
		} else if (filePath.endsWith("xls")) {
			return readExcelToMap(new FileInputStream(filePath),"xlsx",hideRowIndex,startRowIndex);
		} else {
			new Exception("上传文件必须后缀必须为xls或xlsx！");
		}
		return null;
	}
	
	/**
	 * @param file excel文件对象
	 * @param hideRowIndex 隐藏行的index，此行的值当做key
	 * @param startRowIndex 数据开始行的index
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, List<Map<String, String>>> readExcelToMap(File file, int hideRowIndex, int startRowIndex)
			throws Exception {
		if (file.getName().endsWith("xlsx")) {
			return readExcelToMap(new FileInputStream(file),"xlsx",hideRowIndex,startRowIndex);
		} else if (file.getName().endsWith("xls")) {
			return readExcelToMap(new FileInputStream(file),"xlsx",hideRowIndex,startRowIndex);
		} else {
			new Exception("上传文件必须后缀必须为xls或xlsx！");
		}
		return null;
	}
	
	/**
	 * @param in excel文件输入流对象
	 * @param fileName 文件名或后缀名
	 * @param hideRowIndex 隐藏行的index，此行的值当做key
	 * @param startRowIndex 数据开始行的index
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, List<Map<String, String>>> readExcelToMap(InputStream in,String fileName, int hideRowIndex, int startRowIndex)
			throws Exception {
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
		return readExcelToMap(workbook, hideRowIndex, startRowIndex);
	}
	
	/**
	 * @param workbook poi文件poi对象
	 * @param hideRowIndex 隐藏行的index，此行的值当做key
	 * @param startRowIndex 数据开始行的index
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, List<Map<String, String>>> readExcelToMap(Workbook workbook, int hideRowIndex, int startRowIndex)
			throws Exception {
		Map<String, List<Map<String, String>>> excelMap= new HashMap<String, List<Map<String, String>>>();
		POIFSFileSystem fs = null;
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
	 * 读取excel，转为json字符串
	 * @param filePath 文件路径
	 * @param hideRowIndex 隐藏的行索引
	 * @param startRowIndex 开始读取数据的行索引
	 * @return
	 * @throws Exception
	 */
	public static String readExcelToJson(String filePath, int hideRowIndex, int startRowIndex) throws Exception {
		Map<String, List<Map<String, String>>>  excelMap = readExcelToMap(filePath, hideRowIndex, startRowIndex);
		return LoserStarJsonUtil.toJsonDeep(excelMap);
	}
	/**
	 * 读取excel，转为json字符串
	 * @param file 文件对象
	 * @param hideRowIndex 隐藏的行索引
	 * @param startRowIndex 开始读取数据的行索引
	 * @return
	 * @throws Exception
	 */
	public static String readExcelToJson(File file, int hideRowIndex, int startRowIndex) throws Exception {
		Map<String, List<Map<String, String>>>  excelMap = readExcelToMap(file, hideRowIndex, startRowIndex);
		return LoserStarJsonUtil.toJsonDeep(excelMap);
	}
	/**
	 * 读取excel，转为json字符串
	 * @param in 文件输入流
	 * @param hideRowIndex 隐藏的行索引
	 * @param startRowIndex 开始读取数据的行索引
	 * @return
	 * @throws Exception
	 */
	public static String readExcelToJson(InputStream in,String fileName, int hideRowIndex, int startRowIndex) throws Exception {
		Map<String, List<Map<String, String>>>  excelMap = readExcelToMap(in,fileName,hideRowIndex, startRowIndex);
		return LoserStarJsonUtil.toJsonDeep(excelMap);
	}
	/**
	 * 读取excel，转为json字符串
	 * @param workbook excel对象
	 * @param hideRowIndex 隐藏的行索引
	 * @param startRowIndex 开始读取数据的行索引
	 * @return
	 * @throws Exception
	 */
	public static String readExcelToJson(Workbook workbook, int hideRowIndex, int startRowIndex) throws Exception {
		Map<String, List<Map<String, String>>>  excelMap = readExcelToMap(workbook, hideRowIndex, startRowIndex);
		return LoserStarJsonUtil.toJsonDeep(excelMap);
	}
	
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
	 * 把一个poi的excel的workbook对象输出
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
		writeWorkbook(workbook, new File(filePath));
	}
	
	/**
	 * 把一个List<LinkedHashMap<String, Object>>对象写入到一个excel的对象中
	 * @param sourceFile 源文件对象（模板文件对象，如果有就会以这个模板文件为基础覆盖里面的内容生成一个新的workbook）
	 * @param sourceFileSheetName 源文件对象的sheet
	 * @param fileName 生成的文件的格式（根据后缀名是xls还是xlsx判断以97-03格式生成还是以07格式生成）
	 * @param newFileSheetName 新生成的excel的sheet名
	 * @param list 要写入的数据
	 * @param startRowIndex 写入的起始行索引，第一行为0
	 * @param startColumnIndex 写入的起始列索引，第一列为0
	 * @return poi的workbook对象
	 * @throws Exception
	 */
	public static Workbook writeListMapToExcel(File sourceFile,String sourceFileSheetName ,String fileName,String newFileSheetName,List<LinkedHashMap<String, Object>> list,int startRowIndex,int startColumnIndex) throws Exception {
		Workbook workbook = null;
		Sheet sheet = null;
		if (sourceFile!=null&&LoserStarFileUtil.isFile(sourceFile)) {
			//源文件有，说明需要读取
			if (sourceFile.getAbsolutePath().endsWith("xls")) {
				workbook = new HSSFWorkbook(new FileInputStream(sourceFile));
			}else if (sourceFile.getAbsolutePath().endsWith("xlsx")) {
				workbook = new XSSFWorkbook(new FileInputStream(sourceFile));
			}
			sheet = workbook.getSheet(sourceFileSheetName);
		}else {
			//源文件没有，就直接生成个新的
			if (fileName.endsWith("xls")) {
				workbook = new HSSFWorkbook();
			}else if (fileName.endsWith("xlsx")) {
				workbook = new XSSFWorkbook();
			}else {
				throw new Exception("新文件必须是xls或xlsx后缀！");
			}
			sheet = workbook.createSheet(newFileSheetName);
		}
		
		workbook.setSheetName(0, newFileSheetName);

		for (int i =0; i < list.size(); i++) {//遍历行
			Row row = sheet.createRow(startRowIndex+i);//从第几行开始写
			Map<String, Object> map = list.get(i);
			int j=startColumnIndex;
			for (Entry<String, Object> entry : map.entrySet()) { 
				String key = entry.getKey();
				Object value = entry.getValue();
				Cell cell = row.createCell(j);
				cell.setCellValue(String.valueOf(value));
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

//			String json = readExcelToJson(file, 1, 2);
//			System.out.println(json);
/*			Map<String, List<Map<String, String>>> map = readExcelToMap(file, 0, 1);
			System.out.println(LoserStarJsonUtil.toJsonDeep(map));
			Date newsdate = dateNumberToDate(Integer.valueOf(map.get("Sheet1").get(0).get("NEWSDATE")));
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newsdate));*/
			
			//生成一个新的excel，或者读取一个现有的excel进行覆盖或追加（适用于类似模板）
			File sourceFile = new File("C:\\export.xlsx");
			File newFile = new File("C:\\export2.xlsx");
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
			Workbook workbook =null;
			workbook = writeListMapToExcel(sourceFile, "hellow", "xlsx", "hellow2", list, 19, 0);//没有源文件或者传个null，就生成一个新的，如果有源文件，就从第19行索引覆盖
			writeWorkbook(workbook, new FileOutputStream(newFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}