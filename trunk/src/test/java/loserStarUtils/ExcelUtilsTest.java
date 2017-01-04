/**
 * author: loserStar
 * version: 2016年11月30日下午2:34:31
 * email:362527240@qq.com
 * remarks: excelUtils的测试用例，相关用法
 */
package loserStarUtils;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.loserstar.utils.excel.ExcelUtils;
import com.loserstar.utils.excel.ExcelUtils.ExcelPage;

public class ExcelUtilsTest {
	private ExcelUtils excelUtils = new ExcelUtils();
	
	//测试数据
	private List<List<String>> allDataList = new ArrayList<List<String>>();
	final int rowCount = 100;
	final int columnCount = 10;

	@Before
	public void setUp() throws Exception {
		//模拟生成一个二维数组
				for (int i = 0; i < rowCount; i++) {
					List<String> row = new ArrayList<String>();
					for (int j = 0; j < columnCount; j++) {
						row.add(""+i+"行|"+j+"列");
					}
					allDataList.add(row);
				}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * 测试获取分页
	 */
	@Test
	public void getPageListTest() {
		
		//把数据分页,5页一条
		List<ExcelPage> excelPages = excelUtils.getPageList(allDataList, 5);
		
		//校验数据
		assertNotNull(excelPages);
		assertEquals(20, excelPages.size());
		assertEquals(20, excelPages.get(0).getPageCount());
		assertEquals(1, excelPages.get(0).getPageNo());
		assertEquals(5, excelPages.get(0).getPageSize());
		assertNotNull(excelPages.get(0).getList());
		assertEquals(5, excelPages.get(0).getList().size());
		assertEquals(100, excelPages.get(0).getTotal());
		
		//展示数据
		System.out.println("-------------------------共有"+excelPages.size()+"页");
		for (ExcelPage excelPage : excelPages) {
			System.out.println("-----------------------------------------当前第"+excelPage.getPageNo()+"页，每页有"+excelPage.getPageSize()+"条数据，共有"+excelPage.getPageCount()+"页，共有"+excelPage.getTotal()+"条数据");
			List<List<String>> list = excelPage.getList();
			for (List<String> row : list) {
				System.out.println(row.get(0).split("|")[0]+"行");
				for (String column : row) {
					System.out.print("  "+column);
				}
				System.out.println("");
			}
		}
		
		
	}

	/**
	 * 写入excel
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void writeExcel97_2003ForTableTest() throws FileNotFoundException, IOException, Exception{
		//生成新的
		HSSFWorkbook workbook1 = excelUtils.writeExcel97_2003ForTable(null, "Sheet1", 0,0, excelUtils.getPageList(allDataList, 20).get(0), null);
		excelUtils.outPutToPath("c://", "新生成.xls", workbook1);
		
       //取得根目录路径  
       String rootPath=excelUtils.getClass().getResource("/").getFile().toString();  
       System.out.println("//取得根目录路径   "+rootPath);
       //当前目录路径  
       String currentPath1=excelUtils.getClass().getResource(".").getFile().toString();  
       System.out.println("//当前目录路径   "+currentPath1);
       String currentPath2=excelUtils.getClass().getResource("").getFile().toString();  
       System.out.println("//当前目录路径   "+currentPath2);
       //当前目录的上级目录路径  
       String parentPath=excelUtils.getClass().getResource("../").getFile().toString();
       System.out.println("//当前目录的上级目录路径    "+parentPath);
       
		//读入模板写
		HSSFWorkbook workbook2 = excelUtils.readExcel(currentPath1+"distributionBillTemplate.xls");
		excelUtils.writeExcel97_2003ForTable(workbook2, "Sheet1", 0,0, excelUtils.getPageList(allDataList, 20).get(0), null);
		excelUtils.outPutToPath("c://", "读取模板.xls", workbook2);
	}
}
