/**
 * author: loserStar
 * date: 2019年9月10日下午2:43:18
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 *//*
package com.loserstar.utils.db.apacheDbUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.loserstar.utils.db.config.LoserStarDBConfig;

*//**
 * author: loserStar
 * date: 2019年9月10日下午2:43:18
 * remarks:
 *//*
public class DBUtilsMain {

	*//**
	 * @param args
	 * @throws SQLException 
	 *//*
	public static void main(String[] args) throws SQLException {
		DruidDataSource druidDataSource = new DruidDataSource();
		druidDataSource.setDriverClassName(LoserStarDBConfig.getDriver());
		druidDataSource.setUsername(LoserStarDBConfig.getUsername());
		druidDataSource.setPassword(LoserStarDBConfig.getPassword());
		druidDataSource.setUrl(LoserStarDBConfig.getConnectionstr());
		
		BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(LoserStarDBConfig.getDriver());
        ds.setUsername(LoserStarDBConfig.getUsername());
        ds.setPassword(LoserStarDBConfig.getPassword());
        ds.setUrl(LoserStarDBConfig.getConnectionstr());
        
		QueryRunner queryRunner = new QueryRunner(ds);
		//增
		for (int i = 0; i < 10; i++) {
			int count = queryRunner.update("INSERT INTO sys_users (id, user_name, password) VALUES ('"+UUID.randomUUID().toString()+"', 'loserStar', 'tttt')");
			System.out.println(count);
		}
		//查
		List<Map<String, Object>> list = queryRunner.query("select * from sys_users", new MapListHandler());
//		System.out.println(LoserStarJsonUtil.toJsonDeep(list));
		
		
		//删
		int count = queryRunner.update("delete from sys_users");
		System.out.println("删除："+count);
	}

}
*/