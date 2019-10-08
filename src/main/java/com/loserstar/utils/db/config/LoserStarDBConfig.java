package com.loserstar.utils.db.config;

public class LoserStarDBConfig {
	public static final boolean isTest = true;//false为生产，true为测试机
	public static final boolean isUpdate = false;//更新开关
	
//	private static final String driver = "com.ibm.db2.jcc.DB2Driver";//db2
	private static final String driver = "com.mysql.jdbc.Driver";//mysql
	
	//生产配置
	private static final String connectionStr_product = "jdbc:mysql://localhost:3306/dataagg";
	private static final String username_product = "root";
	private static final String password_product = "root";
	
	//测试机配置
	private static final String connectionStr_test = "jdbc:mysql://localhost:3306/dataagg";
	private static final String username_test = "root";
	private static final String password_test = "root";
	
	
	public static String getDriver() {
		return driver;
	}
	public static String getConnectionstr() {
		return isTest?connectionStr_test:connectionStr_product;
	}
	public static String getUsername() {
		return isTest?username_test:username_product;
	}
	public static String getPassword() {
		return isTest?password_test:password_product;
	}
	
	
}
