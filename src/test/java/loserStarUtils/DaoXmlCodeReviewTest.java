/**
 * author: lxx
 * version: 2016年5月13日上午11:42:11
 * email:362527240@qq.com
 * remarks:
 */
package loserStarUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class DaoXmlCodeReviewTest {

	static int countFiles = 0;// 声明统计文件个数的变量
	static int countFolders = 0;// 声明统计文件夹的变量

	public static final String CODE_DIRECTORY = "D:\\development\\keWorkSpace";// 要检测的目录

	public static final String FILE_NAME_ENDING = "Dao.xml";// 要检测的文件的文件名是以某个字符串结尾的

	public static final String MATCHINGSTRING = "1=1"; // 每一行中匹配的字符串

	public static final boolean IS_IGNORE_CASE = true;// 是否忽略大小写（true忽略，false不忽略）

	/**
	 * 检测dao的xml中where 1=1的写法，会影响查询效率
	 * 
	 * @throws IOException
	 */
//	@Test
	public void check() throws IOException {
		System.out.println(("-------------------检测以Dao.xml结尾的文件中是否存在1=1的sql写法-------------"));
		File folder = new File(CODE_DIRECTORY);// 默认目录
		String keyword = "";
		if (!folder.exists()) {// 如果文件夹不存在
			System.out.println("目录不存在：" + folder.getAbsolutePath());
			return;
		}
		List<File> fileList = searchFile(folder, keyword);// 调用方法获得文件数组
		System.out.println("在 " + folder + " 以及所有子文件时查找对象" + keyword);
		System.out.println("查找了" + countFiles + " 个文件，" + countFolders + " 个文件夹，共找到 " + fileList.size() + " 个符合条件的文件：");

		for (File file : fileList) {
			String fileName = file.getAbsolutePath();
			boolean flag1 = fileName.matches(".*build.*");// 忽略带build的目录
			boolean flag2 = fileName.matches(".*bin.*");// 忽略带bin的目录
			boolean flag3 = fileName.matches(".*classes.*");// 忽略带classes的目录
			if (!flag1 && !flag2 && !flag3) {// 如果不包含build,bin，classes路径的文件都不读取
				System.out.println("正在检测文件" + fileName);

				BufferedReader reader = new BufferedReader(new FileReader(file));
				String tempString = null;
				int line = 1;
				// 一次读入一行，直到读入null为文件结束
				while ((tempString = reader.readLine()) != null) {
					// 显示行号
					if (IS_IGNORE_CASE) {
						if (tempString.matches(".*((?i)" + DaoXmlCodeReviewTest.MATCHINGSTRING + ").*")) {
							System.out.println("line " + line + ": " + tempString);
						}
					} else {
						if (tempString.matches(".*" + DaoXmlCodeReviewTest.MATCHINGSTRING + ".*")) {
							System.out.println("line " + line + ": " + tempString);
						}
					}
					line++;
				}
				reader.close();
			}
		}
	}

	public static List<File> searchFile(File folder, final String keyWord) {// 递归查找包含关键字的文件
		File[] subFolders = folder.listFiles(new FileFilter() {// 运用内部匿名类获得文件
			public boolean accept(File pathname) {// 实现FileFilter类的accept方法
				if (pathname.isFile())// 如果是文件
					countFiles++;
				else
					// 如果是目录
					countFolders++;
				if (pathname.isDirectory()
						|| (pathname.isFile() && pathname.getName().toLowerCase().contains(keyWord.toLowerCase())))// 目录或文件包含关键字
					return true;
				return false;
			}
		});

		List<File> fileList = new ArrayList<File>();// 声明一个集合
		for (int i = 0; i < subFolders.length; i++) {// 循环显示文件夹或文件
			if (subFolders[i].isFile()) {// 如果是文件则将文件添加到结果列表中
				String fileName = subFolders[i].getName();
				boolean falg = false;
				if (IS_IGNORE_CASE) {
					falg = fileName.matches(".*((?i)" + FILE_NAME_ENDING + ")$");// 正则匹配以DAO.xml结尾的文件
				} else {
					falg = fileName.matches(".*" + FILE_NAME_ENDING + "$");// 正则匹配以DAO.xml结尾的文件
				}
				if (falg) {
					fileList.add(subFolders[i]);
				}
			} else {// 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
				List<File> fileList2 = searchFile(subFolders[i], keyWord);
				for (int j = 0; j < fileList2.size(); j++) {// 循环显示文件
					fileList.add(fileList2.get(j));// 文件保存到集合中
				}
			}
		}
		return fileList;
	}
}
