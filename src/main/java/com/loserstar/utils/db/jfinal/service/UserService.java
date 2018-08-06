/**
 * author: loserStar
 * date: 2018年8月6日下午6:15:05
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.db.jfinal.service;

import com.loserstar.utils.db.jfinal.base.BaseService;

/**
 * author: loserStar
 * date: 2018年8月6日下午6:15:05
 * remarks:
 */
public class UserService extends BaseService{
	public static final String TABLE_NAME = "sys_users";
	public static final String PRIMARY_KEY = "id";
	public static final String SOFT_DEL_FIELD= "";
	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	@Override
	protected String getSoftDelField() {
		return SOFT_DEL_FIELD;
	}
}
