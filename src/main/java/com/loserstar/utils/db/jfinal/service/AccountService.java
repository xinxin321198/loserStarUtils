/**
 * author: loserStar
 * date: 2018年8月22日下午4:41:38
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.db.jfinal.service;

import com.loserstar.utils.db.jfinal.base.imp.BaseService;

/**
 * author: loserStar
 * date: 2018年8月22日下午4:41:38
 * remarks:
 */
public class AccountService extends BaseService {
	public static final String TABLE_NAME = "da_account";
	public static final String PRIMARY_KEY = "id";
	public static final String SOFT_DEL_FIELD= "";
	@Override
	protected String getTableName() {
		return null;
	}

	@Override
	protected String getPrimaryKey() {
		return null;
	}

	@Override
	protected String getSoftDelField() {
		return null;
	}

}
