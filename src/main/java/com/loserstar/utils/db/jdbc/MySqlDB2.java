package com.loserstar.utils.db.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * N年前大学时期自己尝试封装的类，废弃，用不了
 */
@Deprecated
public class MySqlDB2 {
	
	protected Connection conn = null;
//	protected Statement stmt = null; 
	protected ResultSet rs = null;
	protected PreparedStatement pstmt = null;
	
	/*创建连接 */
	protected  Connection getConn() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/petstore?user=petstoreapp&password=123&useUnicode=true&characterEncoding=gbk");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return conn;
	}
	
	/*创建连接 ，参数一：数据库名，参数二：用户名：参数三：密码，默认MYSQL驱动*/
	protected Connection getConn(String databaseName,String userName,String passWord){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String connectString = "jdbc:mysql://localhost/"+databaseName+"?user="+userName+"&password="+passWord+"&useUnicode=true&characterEncoding=gbk";
			conn = DriverManager.getConnection(connectString);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return conn;
	}
	
	
	/*创建连接   参数一：数据库连接URL*/
	protected  Connection getConn(String connectString) {
		//"jdbc:mysql://localhost/petstore?user=petstoreapp&password=123&useUnicode=true&characterEncoding=gbk"

		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(connectString);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return conn;
	}
	
	
	/*创建连接 ，参数一：数据库驱动类，参数二：数据库连接URL*/
	protected  Connection getConn(String className,String connectString) {
		//"jdbc:mysql://localhost/petstore?user=petstoreapp&password=123&useUnicode=true&characterEncoding=gbk"

		
		try {
			Class.forName(className);
			conn = DriverManager.getConnection(connectString);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return conn;
	}
	
	
	
	
/*	public  Statement getStatement() {
		try {
			if(conn != null) {
				stmt = conn.createStatement();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return stmt;
	}*/
	
	
	//增删改
	protected PreparedStatement getPreparedStatement(String sql){
		try {
			if(conn!= null){
				pstmt =  conn.prepareStatement(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return pstmt;
	}
	
	
	//查询
	protected  ResultSet getResultSet(String sql) {
		try {
			if(pstmt == null) {
				pstmt=conn.prepareStatement(sql);
			}
			rs = pstmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return rs;
	}
	
	

	
	
	//关闭所有连接
	protected boolean closeAll(){
		boolean brs,bstmt,bpstmt,bconn;
		brs = this.closeRs();
		if(rs==null){
			brs=true;
		}
//		bstmt = this.closeStmt();
		bpstmt = this.closePstmt();
		bconn = this.closeConn();
		if(brs&&bpstmt&&bconn){
			return true;
		}
		return false;
	}


	
	
	private  boolean closeConn() {
		try {
			if(conn != null) {
				conn.close();
				conn = null;
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
/*	private  boolean closeStmt() {
		try {
			if(stmt != null) {
				stmt.close();
				stmt = null;
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}*/
	
	private  boolean closeRs() {
		try {
			if(rs != null) {
				rs.close();
				rs = null;
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean closePstmt(){
		try {
			if(pstmt!=null){
				pstmt.close();
				pstmt=null;
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	

}
