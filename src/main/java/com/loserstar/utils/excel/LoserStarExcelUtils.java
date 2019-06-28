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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
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
	 * @param file excel文件对象
	 * @param hideRowIndex 隐藏行的index，此行的值当做key
	 * @param startRowIndex 数据开始行的index
	 * @return
	 * @throws IOException
	 */
	public static Map<String, List<Map<String, String>>> readExcelToMap(File file, int hideRowIndex, int startRowIndex)
			throws IOException {
		if (file.getName().endsWith("xlsx")) {
			return readExcelToMap(new FileInputStream(file),"xlsx",hideRowIndex,startRowIndex);
		} else if (file.getName().endsWith("xls")) {
			return readExcelToMap(new FileInputStream(file),"xlsx",hideRowIndex,startRowIndex);
		} else {
			new Exception("上传文件必须后缀必须为xls或xlsx！");
		}
		return null;
	}
	
	public static Map<String, List<Map<String, String>>> readExcelToMap(InputStream in,String fileName, int hideRowIndex, int startRowIndex)
			throws IOException {
		Map<String, List<Map<String, String>>> excelMap= new HashMap<String, List<Map<String, String>>>();
		POIFSFileSystem fs = null;
		CellStyle cellStyle = null;
		Workbook workbook = null;
		if (fileName.endsWith("xlsx")) {
			workbook = new XSSFWorkbook(in);
		} else if (fileName.endsWith("xls")) {
			fs = new POIFSFileSystem(in);
			workbook = new HSSFWorkbook(fs);
		} else {
			new Exception("上传文件必须后缀必须为xls或xlsx！");
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
				cell.setCellType(CellType.STRING);
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
						cell.setCellType(CellType.STRING);
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
	
	
	public static String readExcelToJson(File file, int hideRowIndex, int startRowIndex) throws IOException {
		Map<String, List<Map<String, String>>>  excelMap = readExcelToMap(file, hideRowIndex, startRowIndex);
		return LoserStarJsonUtil.toJsonDeep(excelMap);
	}
	/**
	 * 把一个List<Map<String,String>>写到excel里(重复写时候有BUG，抽空再调)
	 * @param sheetName 要写的sheet名称，如果不传就使用默认的第一个
	 * @param list 数据
	 * @param inFilePath 要写的excel文件
	 * @param outFilePath 输出的excel文件
	 * @throws IOException
	 */
	public static void writeListMapToExcel(String sheetName,List<Map<String, String>> list,String filePath) throws IOException {
		Workbook workbook = null;
		Sheet sheet = null;
		String suffix = LoserStarFileUtil.getFileNameSuffix(filePath);
		//判断文件存不存在，存在就读取出来写了覆盖，不存在就创建新的，根据后缀判断是新版还是旧版的excel
		File excel = new File(filePath);
		if (excel.exists()&&false) {
			//存在
			if (suffix.equals(".xlsx")) {
				workbook = new XSSFWorkbook(filePath);
			} else if (suffix.equals(".xls")) {
				workbook = new HSSFWorkbook(new FileInputStream(filePath));
			}
			sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				sheet = workbook.createSheet(sheetName);
			}
		}else {
			//不存在，创建新的
			excel.createNewFile();
			if (suffix.equals(".xlsx")) {
				workbook = new XSSFWorkbook();
			} else if (suffix.equals(".xls")) {
				workbook = new HSSFWorkbook();
			}
			sheet = workbook.createSheet();
		}
		
		for (int i =0; i < list.size(); i++) {//遍历行
			Row row = sheet.createRow(i);//从第几行开始写
			Map<String, String> map = list.get(i);
			int j=0;
			for (Entry<String, String> entry : map.entrySet()) { 
				String key = entry.getKey();
				String value = entry.getValue();
				Cell cell = row.createCell(j);
				cell.setCellValue(value);
				j++;
			}
		}
		
		//遍历完，输出
		FileOutputStream fos = new FileOutputStream(filePath);
		workbook.write(fos);
		fos.flush();
		fos.close();
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
			File file = new File("C:\\export.xlsx");
//			String json = readExcelToJson(file, 1, 2);
//			System.out.println(json);
			Map<String, List<Map<String, String>>> map = readExcelToMap(file, 0, 1);
			System.out.println(LoserStarJsonUtil.toJsonDeep(map));
			Date newsdate = dateNumberToDate(Integer.valueOf(map.get("Sheet1").get(0).get("NEWSDATE")));
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newsdate));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}