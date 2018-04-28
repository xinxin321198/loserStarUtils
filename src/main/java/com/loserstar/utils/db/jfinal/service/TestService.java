/**
 * author: loserStar
 * date: 2018年4月18日上午10:14:20
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.db.jfinal.service;


import com.loserstar.utils.db.jfinal.base.BaseService;

/**
 * author: loserStar
 * date: 2018年4月18日上午10:14:20
 * remarks:
 */
public class TestService extends BaseService{
	public static final String TABLE_NAME = "表名";
	public static final String PRIMARY_KEY = "主键名";
	public static final String SOFT_DEL_FIELD= "软删除字段名";
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
