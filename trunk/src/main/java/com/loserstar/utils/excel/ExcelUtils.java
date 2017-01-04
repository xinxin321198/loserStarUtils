/**
 * author: loserStar
 * version: 2016年11月29日下午3:49:38
 * email:362527240@qq.com
 * remarks: POI，excel操作的一些小封装
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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {


	public class ExcelPage {
		private int total;//总数据量
		private int pageSize = 10;//每页多少条
		private int pageNo;//当前页
		private int pageCount;//共多少页
		private List<List<String>> list;//数据
		
		ExcelPage(int pageSize,int pageNo,List<List<String>> list){
			this.total = list.size();
			this.pageSize = pageSize;
			this.pageNo = pageNo;
			
			//计算总页数
			int count = total/pageSize;
			if (total%pageSize!=0) {
				count++;
			}
			this.pageCount = count;
			
			//计算本页的数据
			int startIndex = pageSize*(pageNo-1);//取数据的起始索引=（页码-本页)X每页数量）
			List<List<String>> listTemp = new ArrayList<List<String>>();
			for (int i = startIndex,size=1; i < list.size(); i++) {
				listTemp.add(list.get(i));
				if (size == this.pageSize) {
					break;
				}
				size++;
			}
			this.list = listTemp;
		}
		
		public int getTotal() {
			return total;
		}
		public int getPageSize() {
			return pageSize;
		}
		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}
		public int getPageNo() {
			return pageNo;
		}
		public void setPageNo(int pageNo) {
			this.pageNo = pageNo;
		}
		public int getPageCount() {
			return pageCount;
		}
		public List<List<String>> getList() {
			return list;
		}
	}
	
	/**
	 * 传入总的二维数组数据以及每页多少条，得到分页的List，分页对象里包含每页的数据
	 * @param data 
	 * @param pageSize
	 * @return
	 */
	public List<ExcelPage> getPageList(List<List<String>> data,int pageSize){
		ExcelPage pageTemp = new ExcelPage(pageSize,1,data);
		int pageCount = pageTemp.getPageCount();//共多少页
		List<ExcelPage> pages = new ArrayList<ExcelPage>();
		for (int i = 0; i < pageCount; i++) {
			ExcelPage page = new ExcelPage(pageSize, i+1, data);
			pages.add(page);
		}
		return pages;
	}
	
	

	/**
	 * 写入数据到一个excel，写到一个现有的excel里或者创建一个的excel文件
	 * @param workbook 要写入数据的excel，如果传null表示新创建一个
	 * @param sheetname 要写入数据的excel里的sheet名称，如果workbook为null，则此参数作为新创建的excel的默认sheet
	 * @param content 要写入的字符串二维数组
	 * @param startRowIndex 从哪行开始写入，其实行为0
	 * @param startRowIndex 从列开始写入，其实行为0
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public HSSFWorkbook writeExcel97_2003ForTable(HSSFWorkbook workbook,String sheetName,int startRowIndex,int startColumnIndex,
			ExcelPage page,HSSFCellStyle style) throws FileNotFoundException, IOException,
			Exception {
		List<List<String>> content = page.getList();
		int rowCount = content.size();
		HSSFSheet sheet = null;
		if (null == workbook) {
			// 创建新的Excel 工作簿  
			workbook = new HSSFWorkbook();
			// 在Excel工作簿中建一工作表，其名为缺省值  
			sheet = workbook.createSheet(sheetName);
		}
		else{
			sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				sheet = workbook.createSheet(sheetName);
			}
		}
		for (int i =0; i < rowCount; i++) {//遍历行
			HSSFRow row = sheet.createRow(startRowIndex+i);//从第几行开始写
			for (int j = 0; j < content.get(i).size(); j++) {
				HSSFCell cell = row.createCell(j);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(content.get(i).get(j));
				cell.setCellStyle(style);
			}
		}
		return workbook;
	}

	/**
	 * 在某一行某一列上写入一个值
	 * @param workbook excel对象
	 * @param sheetName 工作薄名称
	 * @param rowIndex 行索引
	 * @param columnIndex 列索引
	 * @param data 要写入的字符串数据
	 * @return
	 * @throws Exception 
	 */
	public HSSFWorkbook writeExcel97_2003ForRowColumn(HSSFWorkbook workbook,String sheetName,int rowIndex,int columnIndex,String data,HSSFCellStyle style) throws Exception{
		HSSFSheet sheet = null;
		if (null == workbook) {
			// 创建新的Excel 工作簿  
			workbook = new HSSFWorkbook();
			// 在Excel工作簿中建一工作表，其名为缺省值  
			sheet = workbook.createSheet(sheetName);
		}
		else{
			sheet = workbook.getSheet(sheetName);
	    	if (null==sheet) {
				throw new Exception("找不到“"+sheetName+"”的sheet");
			}
		}
		HSSFRow row = sheet.getRow(rowIndex);
		if (row==null) {
			row = sheet.createRow(rowIndex);
		}
		HSSFCell cell = row.getCell(columnIndex);
		if (cell == null) {
			cell = row.createCell(columnIndex);
		}
		cell.setCellValue(data);
		if (style!=null) {
			cell.setCellStyle(style);
		}
		return workbook;
	}
	
	/**
	 * 根据行号得到合并的单元格
	 * @param workbook excel对象
	 * @param sheetName 工作薄索引
	 * @param rowIndex 行的索引
	 * @return
	 * @throws Exception 
	 */
	public CellRangeAddress readMergedRegion(HSSFWorkbook workbook,String sheetName,int rowIndex) throws Exception{
		HSSFSheet sheet =  workbook.getSheet(sheetName);
    	if (null==sheet) {
			throw new Exception("找不到“"+sheetName+"”的sheet");
		}
    	return sheet.getMergedRegion(rowIndex);
	}
	
  /** 
    * 复制原有sheet的合并单元格到新创建的sheet 
    *  
    * @param sheetCreat 新创建sheet 
    * @param sheet      原有的sheet 
    */  
    public  void mergerRegion(HSSFSheet fromSheet, HSSFSheet toSheet) {  
       int sheetMergerCount = fromSheet.getNumMergedRegions();  
       for (int i = 0; i < sheetMergerCount; i++) {  
        CellRangeAddress cellRangeAddress = fromSheet.getMergedRegion(i);  
        toSheet.addMergedRegion(cellRangeAddress);  
       }  
    }  
	
    /**
     * 合并单元格，传入坐标
     * @param workbook excel对象
     * @param sheetName 工作薄名称
     * @param firstRow 起始行索引
     * @param lastRow 结束行索引
     * @param firstCol 起始列索引
     * @param lastCol 结束列索引
     * @throws Exception 
     */
    public void mergerCell(HSSFWorkbook workbook,String sheetName,int firstRow, int lastRow, int firstCol, int lastCol) throws Exception{
    	HSSFSheet sheet =  workbook.getSheet(sheetName);
    	if (null==sheet) {
			throw new Exception("找不到“"+sheetName+"”的sheet");
		}
		CellRangeAddress cra =  new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		List<CellRangeAddress> mergedRegionList =  sheet.getMergedRegions();
		boolean flag = false;//默认没有单元格包含这些坐标
		for (CellRangeAddress cellRangeAddress : mergedRegionList) {
			if (cellRangeAddress.containsRow(firstRow)&&
					cellRangeAddress.containsRow(lastRow)&&
					cellRangeAddress.containsColumn(firstCol)&&
					cellRangeAddress.containsColumn(lastCol)
					) {
				flag = true;
			}
		}
		//如果没有单元格包含了要合并的这些坐标，那么菜可以合并，否则就会报错
		if(!flag){
			sheet.addMergedRegion(cra);
		}
    }
    
    /**
     * 根据需要保留的数量，删除剩余的sheet
     * @param workbook excel对象
     * @param sheetIndex 工作薄索引
     */
    public HSSFWorkbook removeSheet(HSSFWorkbook workbook,int reserveCount){
    	int remainCount = workbook.getNumberOfSheets()-reserveCount;
    	for (int i = 0; i < remainCount; i++) {
    		workbook.removeSheetAt(reserveCount);
		}
    	return workbook;
    }
    
    /**
     * 根据sheet的索引删除sheet
     * @param workbook excel对象
     * @param sheetIndex 工作薄索引
     */
    public HSSFWorkbook removeSheet(HSSFWorkbook workbook,int[] sheetIndex){
    	for (int i : sheetIndex) {
			workbook.removeSheetAt(i);
		}
    	return workbook;
    }
    /**
     * 根据sheetname删除sheet
     * @param workbook excel对象
     * @param sheetName 工作薄名称
     */
    public HSSFWorkbook removeSheet(HSSFWorkbook workbook,String[] sheetName){
    	for (String string : sheetName) {
			workbook.removeSheetAt(workbook.getSheetIndex(string));
		}
    	return workbook;
    }
    
    
    /**
     * 删除行，并且把下面的行往上移，根据某一列的数值是否为null或者空字符串
     * ps:如果删除的行当中有最后一行的话会报错，只能从倒数第二行删起，所以可以考虑把最后一行下面再加一个空行
     * @param HSSFWorkbook excel对象
     * @param sheetName 工作表名称
     * @param startRowIndex 检测的起始行（非索引）
     * @param endRowIndex 检测的结束行（非索引）
     * @param isNullColumnIndex 用来判断是否删除此行的列，空字符串或者null就删除,如果为-1就不判断，直接删除
     * @throws Exception 
     */
    public HSSFWorkbook removeRow(HSSFWorkbook workbook,String sheetName,int startRow,int endRow,int isNullColumn) throws Exception{
    	HSSFSheet sheet =  workbook.getSheet(sheetName);
    	if (null==sheet) {
			throw new Exception("找不到“"+sheetName+"”的sheet");
		}
    	if (-1 == isNullColumn) {//不分青红皂白删
    		for(int i=0;i<endRow-startRow+1;i++){
    			sheet.shiftRows(startRow, sheet.getLastRowNum(), -1);//开始不是使用索引，结束需要使用索引
    		}
		}
    	else{
    		for (int i = 0,steep=startRow; i < endRow-startRow+1;i++) {//要判断单元格内的值是否为空的才删
    			HSSFRow row = sheet.getRow(steep-1);//对应上索引
    			if(null == row.getCell(isNullColumn).getStringCellValue()||"".equals(row.getCell(isNullColumn).getStringCellValue())) {
    				sheet.shiftRows(steep, sheet.getLastRowNum(), -1);//开始不是使用索引，结束需要使用索引
    			}
    			else{
    				steep++;
    			}
    		}
    	}
    	return workbook;
    }
	
	/**
	 * 输出到某个路径
	 * @param path 输出路径
	 * @throws IOException 
	 */
	public void outPutToPath(String path,String fileName,HSSFWorkbook workbook) throws IOException{
		File file = new File(path+"/"+fileName);
		FileOutputStream fOutputStream = new FileOutputStream(file);
		// 把相应的Excel 工作簿存盘  
		workbook.write(fOutputStream);
		fOutputStream.flush();
		// 操作结束，关闭文件  
		fOutputStream.close();
	}
	/**
	 * 使用httpServletResponse去输出下载
	 * @param response HttpServletResponse
	 * @param fileName 下载的文件名
	 * @param workbook 工作薄对象
	 * @throws IOException
	 */
	public void outPutToResponse(HttpServletResponse response,String fileName,HSSFWorkbook workbook) throws IOException{
	 	response.setContentType("text/html;charset=UTF-8");   
    	response.setContentType("application/x-excel");  
    	response.setCharacterEncoding("UTF-8");  
    	response.setHeader("Content-Length",""); 
    	response.setHeader("Content-Disposition", "attachment; filename="+fileName);  
    	
		workbook.write(response.getOutputStream());
	}
	
	
	/**
	 * 使用某个输出流去输出
	 * @param os 输出流对象
	 * @param workbook excel对象
	 * @throws IOException 
	 */
	public void outPutToStream(OutputStream os,HSSFWorkbook workbook) throws IOException{
		workbook.write(os);
	}
	
	
	
	
	/**
	 * 根据httpServletRequest读取一个excel
	 * @param path 文件路径，包括文件名及后缀
	 * @return excel对象
	 * @throws IOException
	 */
	public HSSFWorkbook readExcel(HttpServletRequest request,String filePath) throws IOException{
		request.setCharacterEncoding("UTF-8");
		String rootpath = request.getSession().getServletContext().getRealPath("/");
		String path = rootpath + filePath ;  
		return readExcel(path);
	}
	/**
	 * 根据路径，读取一个excel文件
	 * @param path 文件路径，包括文件名及后缀
	 * @return excel对象
	 * @throws IOException
	 */
	public HSSFWorkbook readExcel(String path) throws IOException{
        int iIndex = path.lastIndexOf(".");
        String ext = (iIndex < 0) ? "" : path.substring(iIndex + 1).toLowerCase();
        if (!"xls,xlsx".contains(ext) || "".contains(ext)) {
            System.out.println("文件类型不是EXCEL！");
        }
        
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(path));
        HSSFWorkbook workbook = new HSSFWorkbook(poifsFileSystem);
        return workbook;
	}
	
	
	/**
	 * 读取excel98-2003版本，返回一个指定起始行和多少列的数据数组
	 * @param inputStream
	 * @param sheetName 要读的工作薄名称
	 * @param startRowIndex 需要从第几行开始遍历的行索引号（第一行的索引是0）
	 * @param columnCount 需要遍历的列数（一列的话，就是1）
	 * @return String[][]
	 * @throws IOException
	 * @throws Exception
	 */
	public String[][] analyzeExcel97_2003(InputStream inputStream,String sheetName,
			int startRowIndex, int columnCount) throws IOException, Exception {
		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream); 
		HSSFSheet sheet = workbook.getSheet(sheetName);
    	if (null==sheet) {
			throw new Exception("找不到“"+sheetName+"”的sheet");
		}
		return analyzeExcel97_2003_common(sheet, startRowIndex, columnCount);
	}

	/**
	 * 读取excel97-2003版本，返回一个指定起始行和多少列的数据数组
	 * @param inputStream
	 * @param sheetIndex 要读的工作薄的索引
	 * @param startRowIndex 需要从第几行开始遍历的行索引号（第一行的索引是0）
	 * @param columnCount 需要遍历的列数（一列的话，就是1）
	 * @return String[][]
	 * @throws IOException
	 * @throws Exception
	 */
	public String[][] analyzeExcel97_2003(InputStream inputStream,
			int sheetIndex, int startRowIndex, int columnCount)
			throws IOException, Exception {
		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream); 
		HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
		return analyzeExcel97_2003_common(sheet, startRowIndex, columnCount);
	}

	/**
	 * 公共的解析excel的方法，97-2003版本
	 * @param sheet
	 * @param startRowIndex
	 * @param columnCount
	 * @return
	 * @throws Exception 
	 */
	private String[][] analyzeExcel97_2003_common(HSSFSheet sheet, int startRowIndex, int columnCount) throws Exception{
		boolean isNull = false;//是否存在空值，false是没有
		StringBuffer errorMsg = new StringBuffer();
		int row_count = sheet.getLastRowNum()+1;//得到最后一行的索引+1，得到excel里的所有行数
		int row_count_array = row_count-startRowIndex;//实际数组的行长度（把excle的前两行减了）
		String excelArrary[][] = new String[row_count_array][columnCount];//声明出数组的大小，【行】【列】
		for (int i = startRowIndex; i < row_count; i++) {//遍历行(只要那一行有一列有数据，都被算作一行,从第startRowIndex+1行开始遍历)
			HSSFRow r = sheet.getRow(i);
			if(null==r){
				isNull = true;
				errorMsg.append("第"+(i+1)+"行  是空行<br/>\n");
				continue;
			}
			int cell_num = columnCount;//得到要遍历列的数量
			for (int j = 0; j < cell_num; j++) {//遍历列
				String tmpCell ="";
				if((r.getCell(j)==null||r.getCell(j).toString().trim().equals(""))&&((j==2)||(j==5)||(j==6))){//如果这个单元格是空值  并且是256列,不需要报错，填充为空值
					tmpCell ="";
				}else if((r.getCell(j)==null||r.getCell(j).toString().trim().equals(""))&&((j!=2)||(j!=5)||(j!=6))){//如果单元格是空值，并且不是256列，就报错
					isNull = true;//有空值
					errorMsg.append("第"+(i+1)+"行，第"+(j+1)+"列  的值不能为空<br/>\n");
				} else {
					tmpCell = r.getCell(j).toString().trim();
				}
				excelArrary[i-startRowIndex][j]=tmpCell;
			}
		}
		
		if(isNull){
			throw new Exception(errorMsg.toString());
		}
		return excelArrary;
	}
	
	
	
	/**
	 * 读取excel2007以上版本，返回一个指定起始行和多少列的数据数组
	 * @param inputStream
	 * @param sheetName 要读的工作薄名称
	 * @param startRowIndex  需要从第几行开始遍历的行索引号（第一行的索引是0）
	 * @param columnCount  需要遍历的列数（一列的话，就是1）
	 * @return String[][]
	 * @throws IOException
	 * @throws Exception
	 */
	public String[][] analyzeExcelMoreThan2007(InputStream inputStream,String sheetName,
			int startRowIndex, int columnCount) throws IOException, Exception {
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream); 
		XSSFSheet sheet = workbook.getSheet(sheetName);
    	if (null==sheet) {
			throw new Exception("找不到“"+sheetName+"”的sheet");
		}
		return analyzeExcelMoreThan2007_common(sheet, startRowIndex, columnCount);
	}


	/**
	 * 读取excel2007以上版本，返回一个指定起始行和多少列的数据数组
	 * @param inputStream
	 * @param sheetIndex 要读的工作薄索引
	 * @param startRowIndex 需要从第几行开始遍历的行索引号（第一行的索引是0）
	 * @param columnCount 需要遍历的列数（一列的话，就是1）
	 * @return String[][]
	 * @throws IOException
	 * @throws Exception
	 */
	public String[][] analyzeExcelMoreThan2007(InputStream inputStream,
			int sheetIndex, int startRowIndex, int columnCount)
			throws IOException, Exception {
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream); 
		XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
		return analyzeExcelMoreThan2007_common(sheet, startRowIndex, columnCount);
	}
	
	
	/**
	 * 公共的解析excel的方法，2007版本以上
	 * @param sheet
	 * @param startRowIndex
	 * @param columnCount
	 * @return
	 * @throws Exception 
	 */
	private String[][] analyzeExcelMoreThan2007_common(XSSFSheet sheet, int startRowIndex, int columnCount) throws Exception{
		boolean isNull = false;//是否存在空值，false是没有
		StringBuffer errorMsg = new StringBuffer();
		int row_count = sheet.getLastRowNum()+1;//得到最后一行的索引+1，得到excel里的所有行数
		int row_count_array = row_count-startRowIndex;//实际数组的行长度（把excle的前两行减了）
		String excelArrary[][] = new String[row_count_array][columnCount];//声明出数组的大小，【行】【列】
		for (int i = startRowIndex; i < row_count; i++) {//遍历行(只要那一行有一列有数据，都被算作一行,从第startRowIndex+1行开始遍历)
			XSSFRow r = sheet.getRow(i);
			if(null==r){
				isNull = true;
				errorMsg.append("第"+(i+1)+"行  是空行<br/>\n");
				continue;
			}
			int cell_num = columnCount;//得到要遍历列的数量
			for (int j = 0; j < cell_num; j++) {//遍历列
				String tmpCell ="";
				if((r.getCell(j)==null||r.getCell(j).toString().trim().equals(""))&&((j==2)||(j==5)||(j==6))){//如果这个单元格是空值  并且是256列,不需要报错，填充为空值
					tmpCell ="";
				}else if((r.getCell(j)==null||r.getCell(j).toString().trim().equals(""))&&((j!=2)||(j!=5)||(j!=6))){//如果单元格是空值，并且不是256列，就报错
					isNull = true;//有空值
					errorMsg.append("第"+(i+1)+"行，第"+(j+1)+"列  的值不能为空<br/>\n");
				} else {
					tmpCell = r.getCell(j).toString().trim();
				}
				excelArrary[i-startRowIndex][j]=tmpCell;
			}
		}
		
		if(isNull){
			throw new Exception(errorMsg.toString());
		}
		return excelArrary;
	}
	
	
	
	/**
	 * 验证numberString中有没有出现过number
	 * @param number
	 * @param numberString
	 * @return
	 */
	private Boolean validateIndexArray(int number,String numberString) {
		String[] indexsArray = numberString.split(",");
		for (int i = 0; i < indexsArray.length; i++) {
			if (String.valueOf(number).equals(indexsArray[i])) {
				return true;
			}
		}
		return false;
	}
	
	

	
	
	
	/**
	 * 读取excel97-2003以上版本，返回一个指定起始行和多少列的数据数组,指定需要读取哪几行(传入行号的集合以逗号分隔)
	 * @param inputStream
	 * @param sheetIndex 要读的工作薄索引
	 * @param startRowIndex 需要从第几行开始遍历的行索引号（第一行的索引是0）
	 * @param columnCount 需要遍历的列数（一列的话，就是1）
	 * @param indexs 需要读取的数据的行号，使用逗号分隔开
	 * @param ignoreColumn 需要忽略空值报错的列，使用逗号分隔开（忽略的列将以空字符串填充）
	 * @return String[][]
	 * @throws IOException
	 * @throws Exception
	 */
	public String[][] analyzeExcel97_2003ForIndexs(InputStream inputStream,
			int sheetIndex, int startRowIndex, int columnCount,String indexs,String ignoreColumns)
					throws IOException, Exception {
		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream); 
		HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
		boolean isNull = false;//是否存在空值，false是没有
		StringBuffer errorMsg = new StringBuffer();
		int row_count = sheet.getLastRowNum()+1;//得到最后一行的索引+1，得到excel里的所有行数
		int row_count_array = row_count-startRowIndex;//实际数组的行长度（把excle的前两行减了）
		String excelArrary[][] = new String[row_count_array][columnCount];//声明出数组的大小，【行】【列】
		List<String[]> returnList = new ArrayList<String[]>();//返回值的list
		for (int i = startRowIndex; i < row_count; i++) {//遍历行(只要那一行有一列有数据，都被算作一行,从第startRowIndex+1行开始遍历)
			if(!this.validateIndexArray(i+1, indexs)){
				continue;
			}
			HSSFRow r = sheet.getRow(i);
			if(null==r){
				isNull = true;
				errorMsg.append("第"+(i+1)+"行  是空行<br/>\n");
				continue;
			}
			int cell_num = columnCount;//得到要遍历列的数量
			for (int j = 0; j < cell_num; j++) {//遍历列
				String tmpCell ="";
				if((r.getCell(j)==null||r.getCell(j).toString().trim().equals(""))&&(this.validateIndexArray(j, ignoreColumns))){//如果当前列存在于ignoreColumns字符串中,那么，此列将不需要报错，填充为空值
					tmpCell ="";
				}else if((r.getCell(j)==null||r.getCell(j).toString().trim().equals(""))&&(!this.validateIndexArray(j, ignoreColumns))){//如果单元格是空值，并且不是256列，就报错
					isNull = true;//有空值
					errorMsg.append("第"+(i+1)+"行，第"+(j+1)+"列  的值不能为空<br/>\n");
				} else {
					tmpCell = r.getCell(j).toString().trim();
				}
				excelArrary[i-startRowIndex][j]=tmpCell;
			}
		}
		if(isNull){
			throw new Exception(errorMsg.toString());
		}
		
		for (int i = 0; i < excelArrary.length; i++) {
			if (excelArrary[i][0]==null) {//如果姓名是空值或者null
				continue;
			} else {
				returnList.add(excelArrary[i]);//把这一行放进容器里
			}
		}
		String[][] returnArray = new String[returnList.size()][columnCount];
		returnList.toArray(returnArray);
		
		
		return returnArray;
	}
	
	/**
	 * 读取excel2007以上版本，返回一个指定起始行和多少列的数据数组,指定需要读取哪几行(传入行号的集合以逗号分隔)
	 * @param inputStream
	 * @param sheetIndex 要读的工作薄索引
	 * @param startRowIndex  需要从第几行开始遍历的行索引号（第一行的索引是0）
	 * @param columnCount  需要遍历的列数（一列的话，就是1）
	 * @param indexs 需要读取的数据的行号，使用逗号分隔开
	 * @param ignoreColumn 需要忽略空值报错的列，使用逗号分隔开（忽略的列将以空字符串填充）
	 * @return String[][]
	 * @throws IOException
	 * @throws Exception
	 */
	public String[][] analyzeExcelMoreThan2007ForIndexs(InputStream inputStream,
			int sheetIndex, int startRowIndex, int columnCount,String indexs,String ignoreColumns)
					throws IOException, Exception {
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream); 
		XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
		boolean isNull = false;//是否存在空值，false是没有
		StringBuffer errorMsg = new StringBuffer();
		int row_count = sheet.getLastRowNum()+1;//得到最后一行的索引+1，得到excel里的所有行数
		int row_count_array = row_count-startRowIndex;//实际数组的行长度（把excle的前两行减了）
		String excelArrary[][] = new String[row_count_array][columnCount];//声明出数组的大小，【行】【列】
		List<String[]> returnList = new ArrayList<String[]>();//返回值的list
		for (int i = startRowIndex; i < row_count; i++) {//遍历行(只要那一行有一列有数据，都被算作一行,从第startRowIndex+1行开始遍历)
			if(!this.validateIndexArray(i+1, indexs)){
				continue;
			}
			XSSFRow r = sheet.getRow(i);
			if(null==r){
				isNull = true;
				errorMsg.append("第"+(i+1)+"行  是空行<br/>\n");
				continue;
			}
			int cell_num = columnCount;//得到要遍历列的数量
			for (int j = 0; j < cell_num; j++) {//遍历列
				String tmpCell ="";
				if((r.getCell(j)==null||r.getCell(j).toString().trim().equals(""))&&(this.validateIndexArray(j, ignoreColumns))){//如果当前列存在于ignoreColumns字符串中,那么，此列将不需要报错，填充为空值
					tmpCell ="";
				}else if((r.getCell(j)==null||r.getCell(j).toString().trim().equals(""))&&(!this.validateIndexArray(j, ignoreColumns))){//如果单元格是空值，并且不是256列，就报错
					isNull = true;//有空值
					errorMsg.append("第"+(i+1)+"行，第"+(j+1)+"列  的值不能为空<br/>\n");
				} else {
					tmpCell = r.getCell(j).toString().trim();
				}
				excelArrary[i-startRowIndex][j]=tmpCell;
			}
		}
		
		if(isNull){
			throw new Exception(errorMsg.toString());
		}
		
		for (int i = 0; i < excelArrary.length; i++) {
			if (excelArrary[i][0]==null) {//如果姓名是空值或者null
				continue;
			} else {
				returnList.add(excelArrary[i]);//把这一行放进容器里
			}
		}
		String[][] returnArray = new String[returnList.size()][columnCount];
		returnList.toArray(returnArray);
		
		
		return returnArray;
	}

	/**
	 * 读取excel2007以上版本，返回一个指定起始行和多少列的数据数组,指定需要读取那几行
	 * @param inputStream
	 * @param sheetIndex 要读的工作薄索引
	 * @param startRowIndex 需要从第几行开始遍历的行索引号（第一行的索引是0）
	 * @param columnCount 需要遍历的列数（一列的话，就是1）
	 * @param indexs 需要读取的数据的行号，使用逗号分隔开
	 * @param ignoreColumn 需要忽略空值报错的列，使用逗号分隔开（忽略的列将以空字符串填充）
	 * @return String[][]
	 * @throws IOException
	 * @throws Exception
	 */
	public String[][] analyzeExcel97_2003ForIgnoreColumns(InputStream inputStream,
			int sheetIndex, int startRowIndex, int columnCount,
			String ignoreColumns) throws IOException, Exception {
		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream); 
		HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
		boolean isNull = false;//是否存在空值，false是没有
		StringBuffer errorMsg = new StringBuffer();
		int row_count = sheet.getLastRowNum()+1;//得到最后一行的索引+1，得到excel里的所有行数
		int row_count_array = row_count-startRowIndex;//实际数组的行长度（把excle的前两行减了）
		String excelArrary[][] = new String[row_count_array][columnCount];//声明出数组的大小，【行】【列】
		for (int i = startRowIndex; i < row_count; i++) {//遍历行(只要那一行有一列有数据，都被算作一行,从第startRowIndex+1行开始遍历)
			HSSFRow r = sheet.getRow(i);
			if(null==r){
				isNull = true;
				errorMsg.append("第"+(i+1)+"行  是空行<br/>\n");
				continue;
			}
			int cell_num = columnCount;//得到要遍历列的数量
			for (int j = 0; j < cell_num; j++) {//遍历列
				String tmpCell ="";
				if((r.getCell(j)==null||r.getCell(j).toString().trim().equals(""))&&(this.validateIndexArray(j, ignoreColumns))){//如果当前列存在于ignoreColumns字符串中,那么，此列将不需要报错，填充为空值
					tmpCell ="";
				}else if((r.getCell(j)==null||r.getCell(j).toString().trim().equals(""))&&(!this.validateIndexArray(j, ignoreColumns))){//如果单元格是空值，并且不是256列，就报错
					isNull = true;//有空值
					errorMsg.append("第"+(i+1)+"行，第"+(j+1)+"列  的值不能为空<br/>\n");
				} else {
					tmpCell = r.getCell(j).toString().trim();
				}
				excelArrary[i-startRowIndex][j]=tmpCell;
			}
		}
		if(isNull){
			throw new Exception(errorMsg.toString());
		}
		
		
		
		return excelArrary;
	}

	/**
	 * 读取excel2007以上版本，返回一个指定起始行和多少列的数据数组,指定需要读取那几行
	 * @param inputStream
	 * @param sheetIndex 要读的工作薄索引
	 * @param startRowIndex  需要从第几行开始遍历的行索引号（第一行的索引是0）
	 * @param columnCount  需要遍历的列数（一列的话，就是1）
	 * @param indexs 需要读取的数据的行号，使用逗号分隔开
	 * @param ignoreColumn 需要忽略空值报错的列，使用逗号分隔开（忽略的列将以空字符串填充）
	 * @return String[][]
	 * @throws IOException
	 * @throws Exception
	 */
	public String[][] analyzeExcelMoreThan2007ForIgnoreColumns(InputStream inputStream,
			int sheetIndex, int startRowIndex, int columnCount,
			String ignoreColumns) throws IOException, Exception {
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream); 
		XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
		boolean isNull = false;//是否存在空值，false是没有
		StringBuffer errorMsg = new StringBuffer();
		int row_count = sheet.getLastRowNum()+1;//得到最后一行的索引+1，得到excel里的所有行数
		int row_count_array = row_count-startRowIndex;//实际数组的行长度（把excle的前两行减了）
		String excelArrary[][] = new String[row_count_array][columnCount];//声明出数组的大小，【行】【列】
		for (int i = startRowIndex; i < row_count; i++) {//遍历行(只要那一行有一列有数据，都被算作一行,从第startRowIndex+1行开始遍历)
			XSSFRow r = sheet.getRow(i);
			if(null==r){
				isNull = true;
				errorMsg.append("第"+(i+1)+"行  是空行<br/>\n");
				continue;
			}
			int cell_num = columnCount;//得到要遍历列的数量
			for (int j = 0; j < cell_num; j++) {//遍历列
				String tmpCell ="";
				if((r.getCell(j)==null||r.getCell(j).toString().trim().equals(""))&&(this.validateIndexArray(j, ignoreColumns))){//如果当前列存在于ignoreColumns字符串中,那么，此列将不需要报错，填充为空值
					tmpCell ="";
				}else if((r.getCell(j)==null||r.getCell(j).toString().trim().equals(""))&&(!this.validateIndexArray(j, ignoreColumns))){//如果单元格是空值，并且不是256列，就报错
					isNull = true;//有空值
					errorMsg.append("第"+(i+1)+"行，第"+(j+1)+"列  的值不能为空<br/>\n");
				} else {
					tmpCell = r.getCell(j).toString().trim();
				}
				excelArrary[i-startRowIndex][j]=tmpCell;
			}
		}
		
		if(isNull){
			throw new Exception(errorMsg.toString());
		}
		
		
		
		return excelArrary;
	}

}
