/**
 * author: loserStar
 * date: 2019年8月8日上午9:30:22
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.system;

import java.util.Properties;

/**
 * author: loserStar
 * date: 2019年8月8日上午9:30:22
 * remarks:系统相关工具类
 */
public class LoserStarSystemUtil {

	/**
	 * 输出系统信息
	 */
	public static void printSystemInfo() {
		Properties properties = System.getProperties();
		System.out.println("Java 运行时环境版本:"+properties.getProperty("java.version"));
		System.out.println("Java 运行时环境供应商:"+properties.getProperty("java.vendor"));
		System.out.println("Java 供应商的 URL:"+properties.getProperty("java.vendor.url"));
		System.out.println("Java 安装目录:"+properties.getProperty("java.home"));
		System.out.println("Java 虚拟机规范版本:"+properties.getProperty("java.vm.specification.version"));
		System.out.println("Java 虚拟机规范供应商:"+properties.getProperty("java.vm.specification.vendor"));
		System.out.println("Java 虚拟机规范名称:"+properties.getProperty("java.vm.specification.name"));
		System.out.println("Java 虚拟机实现版本:"+properties.getProperty("java.vm.version"));
		System.out.println("Java 虚拟机实现供应商:"+properties.getProperty("java.vm.vendor"));
		System.out.println("Java 虚拟机实现名称:"+properties.getProperty("java.vm.name"));
		System.out.println("Java 运行时环境规范版本:"+properties.getProperty("java.specification.version"));
		System.out.println("Java 运行时环境规范供应商:"+properties.getProperty("java.specification.vendor"));
		System.out.println("Java 运行时环境规范名称:"+properties.getProperty("java.specification.name"));
		System.out.println("Java 类格式版本号:"+properties.getProperty("java.class.version"));
		System.out.println("Java 类路径:"+properties.getProperty("java.class.path"));
		System.out.println("加载库时搜索的路径列表:"+properties.getProperty("java.library.path"));
		System.out.println("默认的临时文件路径:"+properties.getProperty("java.io.tmpdir"));
		System.out.println("要使用的 JIT 编译器的名称:"+properties.getProperty("java.compiler"));
		System.out.println("一个或多个扩展目录的路径:"+properties.getProperty("java.ext.dirs"));
		System.out.println("操作系统的名称:"+properties.getProperty("os.name"));
		System.out.println("操作系统的架构:"+properties.getProperty("os.arch"));
		System.out.println("操作系统的版本:"+properties.getProperty("os.version"));
		System.out.println("文件分隔符（在 UNIX 系统中是“/”）:"+properties.getProperty("file.separator"));
		System.out.println("路径分隔符（在 UNIX 系统中是“:”）:"+properties.getProperty("path.separator"));
		System.out.println("行分隔符（在 UNIX 系统中是“/n”）:"+properties.getProperty("line.separator"));
		System.out.println("用户的账户名称:"+properties.getProperty("user.name"));
		System.out.println("用户的主目录:"+properties.getProperty("user.home"));
		System.out.println("用户的当前工作目录:"+properties.getProperty("user.dir"));
	}
	
	public static void main(String[] args) {
		printSystemInfo();
	}
}
