/**
 * author: loserStar
 * date: 2018年3月30日上午10:51:12
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.db.jfinal;

import java.util.List;
import java.util.UUID;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.dialect.AnsiSqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.loserstar.utils.db.config.DBConfig;
import com.loserstar.utils.json.LoserStarJsonUtil;

/**
 * author: loserStar
 * date: 2018年3月30日上午10:51:12
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks: 使用jfinal简单操作数据库，可用于用代码处理各种数据的逻辑
 */
public class LoserStarJfinalDBMain {
	private static void startPlugin() {
		String connectionStr = DBConfig.getConnectionstr();
		DruidPlugin dp  = new DruidPlugin(connectionStr, DBConfig.getUsername(),DBConfig.getPassword());
		dp.setDriverClass(DBConfig.getDriver());
		ActiveRecordPlugin arp = new ActiveRecordPlugin(UUID.randomUUID().toString(),dp);
		arp.setShowSql(false);//打印出执行的sql
		arp.setDialect(new AnsiSqlDialect());
		arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));// 不区分大小写
		dp.start();
		arp.start();
	}

	public static void main(String[] args) {
		startPlugin();
		List<Record> userList = Db.find("select * from sys_users");
		System.out.println(LoserStarJsonUtil.toJsonDeep(userList));
	}
}
