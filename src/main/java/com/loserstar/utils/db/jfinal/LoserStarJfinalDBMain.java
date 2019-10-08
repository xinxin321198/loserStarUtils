/**
 * author: loserStar
 * date: 2018年3月30日上午10:51:12
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.db.jfinal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.dialect.AnsiSqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.loserstar.utils.db.config.LoserStarDBConfig;
import com.loserstar.utils.db.jfinal.base.IDBUtils;
import com.loserstar.utils.db.jfinal.base.imp.InStr;
import com.loserstar.utils.db.jfinal.base.imp.JoinHelper;
import com.loserstar.utils.db.jfinal.base.imp.JoinTable;
import com.loserstar.utils.db.jfinal.base.imp.WhereHelper;
import com.loserstar.utils.db.jfinal.base.imp.InStr.AndOr;
import com.loserstar.utils.db.jfinal.base.imp.JoinTable.JoinType;
import com.loserstar.utils.db.jfinal.base.imp.MySqlDBUtils;
import com.loserstar.utils.db.jfinal.service.AccountService;
import com.loserstar.utils.db.jfinal.service.UserService;
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
		String connectionStr = LoserStarDBConfig.getConnectionstr();
		DruidPlugin dp  = new DruidPlugin(connectionStr, LoserStarDBConfig.getUsername(),LoserStarDBConfig.getPassword());
		dp.setDriverClass(LoserStarDBConfig.getDriver());
		ActiveRecordPlugin arp = new ActiveRecordPlugin(UUID.randomUUID().toString(),dp);
		arp.setShowSql(true);//打印出执行的sql
		arp.setDialect(new AnsiSqlDialect());
		arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));//false 是大写, true是小写, 不写是区分大小写
		dp.start();
		arp.start();
	}

	public static void main(String[] args) {
		startPlugin();
		

		
		UserService userService = new UserService();
//		List<Record> userList = Db.find("select * from sys_users");
		WhereHelper whereHelper = new WhereHelper();
		List<String> list = new ArrayList<String>();
		list.add("aaa");
		list.add("bbb");
		list.add("c");
		list.add("dd");
//		whereHelper.addIn(new InStr(AndOr.AND, "user_name", list));
//		whereHelper.addStrWhere(" and user_name like '%名%'");
		whereHelper.addStrOrder(" order by user_name desc");
		JoinHelper joinHelper = new JoinHelper();
		joinHelper.addLeftJoin(new JoinTable(JoinType.left, AccountService.TABLE_NAME ,"user_id", UserService.TABLE_NAME,UserService.PRIMARY_KEY));
		IDBUtils dbUtils = new MySqlDBUtils();
		String selectFiled = dbUtils.toSelectFiledStr("dataagg", joinHelper);
		List<Record> pageList = userService.getJoinList(selectFiled,joinHelper, whereHelper);
		System.out.println(LoserStarJsonUtil.toJsonDeep(pageList));
//		userService.deleteAll();
//		System.out.println("delete all");
	}
}
