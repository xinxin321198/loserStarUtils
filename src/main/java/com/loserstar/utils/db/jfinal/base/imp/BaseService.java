package com.loserstar.utils.db.jfinal.base.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.loserstar.utils.idgen.SnowflakeIdWorker;

/**
 * 
 * author: loserStar
 * date: 2019年10月15日上午9:25:25
 * remarks:基础service，为了兼容联合主键，反向又为了兼容老代码，老的getById(String id)也提供，getById(String... id)也提供
 */
public  abstract class BaseService {
	public enum DBType{
		mysql,db2,oracle,sqlserver
	}
	/**
	 * 删除
	 */
	private static final String DEL = "1";
	/**
	 * 未删除
	 */
	private static final String NOT_DEL = "0";
	
	/**
	 * 批量curd时的标记名称
	 */
	public static final String EDIT_FLAG = "curd_flag";
	public static final String EDIT_FLAG_C = "c";
	public static final String EDIT_FLAG_U = "u";
	public static final String EDIT_FLAG_R = "r";
	public static final String EDIT_FLAG_D = "d";
	/**
	 * 返回具体表名称
	 * @return
	 */
	protected abstract String getTableName();
	/**
	 * 返回表对应的主键名称
	 * @return
	 */
	protected abstract String getPrimaryKey();
	
	protected String dataSourceName;//jfinal使用多数据源时候指定数据源名称
	
	/**
	 * 返回该表软删除使用的字段，查询列表的方法会自动过滤该字段值(如果未指定该字段(null或者空字符串)，则认为该表不具有软删除功能)
	 * @return
	 */
	protected abstract String getSoftDelField();
	/**
	 * 构造方法检查该有的变量是否有
	 */
	public BaseService() {
		try {
			if (getTableName()==null||getTableName().equals("")) {
				throw new Exception(this.getClass().getName()+" 中的getTableName()方法没有指定表名称");
			}
			if (getPrimaryKey()==null||getPrimaryKey().equals("")) {
				throw new Exception(this.getClass().getName()+" 中的getPrimaryKey()方法没有指定表的主键名称");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public BaseService(String dataSourceName) {
		try {
			if (getTableName()==null||getTableName().equals("")) {
				throw new Exception(this.getClass().getName()+" 中的getTableName()方法没有指定表名称");
			}
			if (getPrimaryKey()==null||getPrimaryKey().equals("")) {
				throw new Exception(this.getClass().getName()+" 中的getPrimaryKey()方法没有指定表的主键名称");
			}
			this.dataSourceName = dataSourceName;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 检查whereHelper是否为null,是旧返回空字符串，不是就加上where 并且toString
	 * @param whereHelper
	 * @return
	 */
	private String CheckWhereHelper(WhereHelper whereHelper) {
		String result = "";
		if(whereHelper!=null) {
			if (whereHelper.getStrWhereList()!=null&&whereHelper.getStrWhereList().size()>0) {
				result = " where "+whereHelper.toString();
			}else {
				 result+=" "+(whereHelper.getOrderStr()==null?"":whereHelper.getOrderStr());
			}
		}
		return  result;
	}
	
	/**
	 * 检查软删除字段是否存在，存在返回true，不存在返回false
	 * @return
	 */
	private boolean CheckSoftDelField() {
		if(getSoftDelField()==null||getSoftDelField().equals("")) {
			return false;
		}else {
			return true;
		}
	}
	/**
	 * 检测是否指定jfinal的数据源
	 * @return 指定返回true，未指定返回false
	 */
	private boolean CheckDataSourceName() {
		if (this.dataSourceName==null||this.dataSourceName.equals("")) {
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * 添加softDelField的过滤条件
	 * @param whereHelper
	 * @return
	 */
	private WhereHelper addSoftDelField(WhereHelper whereHelper) {
		if (CheckSoftDelField()) {
			if (whereHelper==null) {
				whereHelper = new WhereHelper();
			}
			whereHelper.addStrWhere(" and "+getSoftDelWhere());
		}
		return whereHelper;
	}
	/**
	 * 得到软删除条件（软删除字段带表名前缀的条件）
	 */
	public String getSoftDelWhere() {
		return getSoftDelWhere(null);
	}
	
	/**
	 * 可以指定软删除字段的表名前缀（在多表连接的时候会指定，如果不指定的话就按照实现类中的表名输出）
	 * @param tableName
	 * @return
	 */
	public String getSoftDelWhere(String tableName) {
		String softDelPre = getTableName();
		if(tableName!=null&&!tableName.contentEquals("")) {
			softDelPre = tableName;
		}
		String softDelWhere = "  ("+softDelPre+"."+getSoftDelField()+"= '"+NOT_DEL+"' or "+softDelPre+"."+getSoftDelField()+" is null or "+softDelPre+"."+getSoftDelField()+" = 0)";
		return softDelWhere;
	}
	
	/**
	 * 根据sql得到单调数据（效率慢，因为是取所有数据出来再取第一条）
	 * @see com.loserstar.utils.db.jfinal.base.imp.BaseService.getFirstList(WhereHelper, DBType)
	 * @param sql
	 * @return
	 */
	public Record get(String sql) {
		List<Record> list = CheckDataSourceName()?Db.use(this.dataSourceName).find(sql):Db.find(sql);
		if (list!=null&&list.size()>0) {
			return list.get(0);
		}else {
			return null;
		}
	}
	
	/**
	 * 根据sql得到单调数据（效率慢，因为是取所有数据出来再取第一条）
	 * 顺带转为对应的jfinal实体
	 * @param <T>
	 * @param sql
	 * @param class1 要转的实体类的class
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unchecked")
	public <T extends Model<T>> T get(String sql,Class<T> class1) {
		T t = null;
		try {
			if (CheckDataSourceName()) {
					t = (T) class1.newInstance().use(this.dataSourceName).findFirst(sql);
			}else {
				t = (T) class1.newInstance().findFirst(sql);
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}
	
	/**
	 * 根据sql得到单调数据(参数化查询方式，效率慢，因为是取所有数据出来再取第一条)
	 * @see com.loserstar.utils.db.jfinal.base.imp.BaseService.getFirstList(WhereHelper, DBType)
	 * @param sql
	 * @return
	 */
	public Record get(String sql,Object... paras) {
		List<Record> list = CheckDataSourceName()?Db.use(this.dataSourceName).find(sql, paras):Db.find(sql,paras);
		if (list!=null&&list.size()>0) {
			return list.get(0);
		}else {
			return null;
		}
	}
	
	/**
	 * 根据sql得到单调数据(参数化查询方式，效率慢，因为是取所有数据出来再取第一条)
	 * 顺带转为对应的jfinal实体
	 * @see com.loserstar.utils.db.jfinal.base.imp.BaseService.getFirstList(WhereHelper, DBType)
	 * @param <T>
	 * @param sql
	 * @param class1 要转的实体类的class
	 * @param paras 查询参数
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unchecked")
	public <T extends Model<T>> T get(String sql,Class<T> class1,Object... paras){
		T t = null;
		try {
			if (CheckDataSourceName()) {
					t = (T) class1.newInstance().use(this.dataSourceName).findFirst(sql,paras);
			}else {
				t = (T) class1.newInstance().findFirst(sql,paras);
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}
	
	/**
	 * 统计数量
	 * @param whereHelper
	 * @return
	 */
	public int getCount(WhereHelper whereHelper) {
		String sql ="select COUNT("+getPrimaryKey()+") count from "+getTableName()+CheckWhereHelper(whereHelper);
		return get(sql).getInt("count");
	}
	
	/**
	 * 设置某条记录的某个字段为null值
	 * @param tableName 表名
	 * @param fieldName 要设为null的字段名
	 * @param primaryId 主键ID的值
	 * @return
	 */
	public boolean updateFieldIsNull(String fieldName,String primaryId) {
		String sql = "UPDATE "+getTableName()+" SET "+fieldName+" = NULL WHERE "+getPrimaryKey()+"='"+primaryId+"'";
		int row = CheckDataSourceName()?Db.use(this.dataSourceName).update(sql):Db.update(sql);
		if (row<1) {
			return false;
		}
		return true;
	}
	public boolean updateFieldIsNull(String fieldName,long primaryId) {
		String sql = "UPDATE "+getTableName()+" SET "+fieldName+" = NULL WHERE "+getPrimaryKey()+"="+primaryId+"";
		int row = CheckDataSourceName()?Db.use(this.dataSourceName).update(sql):Db.update(sql);
		if (row<1) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 查询列表
	 * new一个whereHelper参数：如果设置过软删除字段自动过滤
	 * null:直接不添加软删除过滤
	 * @param whereHelper 查询条件
	 * @return
	 */
	public List<Record> getList(WhereHelper whereHelper){
		addSoftDelField(whereHelper);
		return getList_notSoftDel(whereHelper);
	}
	
	/**
	 * 查询列表
	 * 顺带转为jfinal实体
	 * new一个whereHelper参数：如果设置过软删除字段自动过滤
	 * null:直接不添加软删除过滤
	 * @param <T>
	 * @param whereHelper 查询条件
	 * @param class1 要转的实体类的class
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public <T extends Model<T>> List<T> getList(WhereHelper whereHelper,Class<T> class1) {
		addSoftDelField(whereHelper);
		return getList_notSoftDel(whereHelper,class1);
	}
	
	/**
	 * 查询列表(不自动添加软删除过滤)
	 * @param whereHelper
	 * @return
	 */
	public List<Record> getList_notSoftDel(WhereHelper whereHelper){
		String sql ="select * from "+getTableName()+CheckWhereHelper(whereHelper);
		return CheckDataSourceName()?Db.use(this.dataSourceName).find(sql):Db.find(sql);
	}
	
	/**
	 * 查询列表(不自动添加软删除过滤)
	 * 顺带转为jfinal实体
	 * @param <T>
	 * @param whereHelper 查询条件
	 * @param class1 要转的实体类的class
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unchecked")
	public <T extends Model<T>> List<T> getList_notSoftDel(WhereHelper whereHelper,Class<T> class1) {
		String sql ="select * from "+getTableName()+CheckWhereHelper(whereHelper);
		List<T> tList = null;
		try {
			if (CheckDataSourceName()) {
					tList = class1.newInstance().use(this.dataSourceName).find(sql);
			}else {
				tList = (List<T>) class1.newInstance().find(sql);
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tList;
	}
	
	/**
	 * 多表连接查询，默认查询出的字段使用*（此方法会造成如果两张表有相同名称的字段，会显示不全）
	 * @param joinHelper
	 * @param whereHelper
	 * @return
	 */
	@Deprecated
	public List<Record> getJoinList(JoinHelper joinHelper,WhereHelper whereHelper) {
		return getJoinList(null, joinHelper, whereHelper);
	}
	
	/**
	 * 多表连接查询，并且指定查询出的字段名称(不自动添加软删除过滤)
	 * @param selectFiled
	 * @param joinHelper
	 * @param whereHelper
	 * @return
	 */
	@Deprecated
	public List<Record> getJoinList(String selectFiled,JoinHelper joinHelper,WhereHelper whereHelper) {
		addSoftDelField(whereHelper);
		return getJoinList_notSoftDel(selectFiled,joinHelper,whereHelper);
	}
	
	/**
	 *  多表连接查询，并且指定查询出的字段名称
	 * @param selectFiled
	 * @param joinHelper
	 * @param whereHelper
	 * @return
	 */
	@Deprecated
	public List<Record> getJoinList_notSoftDel(String selectFiled,JoinHelper joinHelper,WhereHelper whereHelper) {
		if (selectFiled==null||selectFiled.equals("")) {
			selectFiled = " * ";
		}
		String sql = "select "+selectFiled+" from "+getTableName();
		if (joinHelper!=null) {
			sql+=joinHelper.toString();
		}
		sql+=CheckWhereHelper(whereHelper);
		return CheckDataSourceName()?Db.use(this.dataSourceName).find(sql):Db.find(sql);
	}
	
	/**
	 * 根据条件查询到的列表，获取第一条数据
	 * 	new一个whereHelper参数：如果设置过软删除字段自动过滤
	 * null:直接不添加软删除过滤
	 * ps:看了jfinal源码，测试后发现jfinal是先取出所有数据，然后取第一条，效率不行，请参考使用数据库方言的方式
	 * @see com.loserstar.utils.db.jfinal.base.imp.BaseService.getFirstList(WhereHelper, DBType)
	 * @param whereHelper
	 * @return
	 */
	@Deprecated
	public Record getFirstList(WhereHelper whereHelper) {
		addSoftDelField(whereHelper);
		return getFirstList_notSoftDel(whereHelper);
	}
	
	/**
	 * 根据条件查询到的列表，获取第一条数据(不自动添加软删除字段过滤)
	 * ps:看了jfinal源码，测试后发现jfinal是先取出所有数据，然后取第一条，效率不行，请参考使用数据库方言的方式
	 * @see com.loserstar.utils.db.jfinal.base.imp.BaseService.getFirstList_notSoftDel(WhereHelper, DBType)
	 * @param whereHelper
	 * @return
	 */
	@Deprecated
	public Record getFirstList_notSoftDel(WhereHelper whereHelper) {
		String sql ="select * from "+getTableName()+CheckWhereHelper(whereHelper);
		return Db.findFirst(sql);
	}
	
	/**
	 * 根据条件查询到的列表，获取第一条数据
	 * 	new一个whereHelper参数：如果设置过软删除字段自动过滤
	 * null:直接不添加软删除过滤
	 * @param whereHelper
	 * @return
	 */
	public Record getFirstList(WhereHelper whereHelper,DBType dbType){
		addSoftDelField(whereHelper);
		return getFirstList_notSoftDel(whereHelper,dbType);
	}
	
	/**
	 * 根据条件查询到的列表，获取第一条数据
	 * new一个whereHelper参数：如果设置过软删除字段自动过滤
	 * null:直接不添加软删除过滤
	 * @param <T>
	 * @param whereHelper
	 * @param dbType 数据库类型（不同数据库获取第一条数据的语句不一样）
	 * @param class1 要转的实体类的class
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public <T extends Model<T>> T getFirstList(WhereHelper whereHelper,DBType dbType,Class<T> class1){
		addSoftDelField(whereHelper);
		return getFirstList_notSoftDel(whereHelper, dbType, class1);
	}
	
	/**
	 * 根据条件查询到的列表，获取第一条数据(不自动添加软删除字段过滤)
	 * @param whereHelper
	 * @param dbType 数据库类型（不同数据库获取第一条数据的语句不一样）
	 * @return
	 */
	public Record getFirstList_notSoftDel(WhereHelper whereHelper,DBType dbType){
		String sql ="";
		if(dbType.equals(DBType.db2)) {
			sql = "select * from "+getTableName()+CheckWhereHelper(whereHelper)+" FETCH FIRST 1 ROWS ONLY";
		}else if(dbType.equals(DBType.mysql)) {
			sql = "select * from "+getTableName()+CheckWhereHelper(whereHelper)+" LIMIT 1";
		}else if(dbType.equals(DBType.oracle)) {
			sql = "select * from "+getTableName()+CheckWhereHelper(whereHelper)+" ROWNUM <= 1";
		}else if(dbType.equals(DBType.sqlserver)) {
			sql = "select TOP number|percent column_name(s) from "+getTableName()+CheckWhereHelper(whereHelper);
		}
		return get(sql);
	}
	
	/**
	 * 根据条件查询到的列表，获取第一条数据(不自动添加软删除字段过滤)
	 * @param <T>
	 * @param whereHelper
	 * @param dbType 数据库类型（不同数据库获取第一条数据的语句不一样）
	 * @param class1 要转的实体类的class
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public <T extends Model<T>> T getFirstList_notSoftDel(WhereHelper whereHelper,DBType dbType,Class<T> class1){
		String sql ="";
		if(dbType.equals(DBType.db2)) {
			sql = "select * from "+getTableName()+CheckWhereHelper(whereHelper)+" FETCH FIRST 1 ROWS ONLY";
		}else if(dbType.equals(DBType.mysql)) {
			sql = "select * from "+getTableName()+CheckWhereHelper(whereHelper)+" LIMIT 1";
		}else if(dbType.equals(DBType.oracle)) {
			sql = "select * from "+getTableName()+CheckWhereHelper(whereHelper)+" ROWNUM <= 1";
		}else if(dbType.equals(DBType.sqlserver)) {
			sql = "select TOP number|percent column_name(s) from "+getTableName()+CheckWhereHelper(whereHelper);
		}
		return get(sql,class1);
	}
	
	/**
	 * 查询列表(分页)的列表数据
	 * 	new一个whereHelper参数：如果设置过软删除字段自动过滤
	 * null:直接不添加软删除过滤
	 * @param pageNumber 页码
	 * @param pageSize 每页多少条
	 * @param whereHelper 查询条件
	 * @return
	 */
	public Page<Record> getListPage(int pageNumber,int pageSize,WhereHelper whereHelper){
		addSoftDelField(whereHelper);
		return getListPage_notSoftDel(pageNumber, pageSize, whereHelper);
	}
	
	/**
	 * 查询列表(分页)的列表数据(不自动添加软删除字段过滤)
	 * @param pageNumber
	 * @param pageSize
	 * @param whereHelper
	 * @return
	 */
	public Page<Record> getListPage_notSoftDel(int pageNumber,int pageSize,WhereHelper whereHelper){
		String sqlExceptSelect = "from "+getTableName()+CheckWhereHelper(whereHelper);
		return CheckDataSourceName()?Db.use(this.dataSourceName).paginate(pageNumber, pageSize, "select *",sqlExceptSelect):Db.paginate(pageNumber, pageSize, "select *",sqlExceptSelect);
	}
	
	/**
	 * 根据字符串主键id得到一条记录
	 * @param id 联合主键的多个主键值
	 * @return
	 */
	public Record getByIds(Object[] id) {
		String[] pKeys = getPrimaryKey().split(",");
		if (pKeys.length != id.length)
			throw new IllegalArgumentException("primary key number must equals id value number");
		
		List<Record> result = new ArrayList<Record>();
		String sql = "";
		if (CheckDataSourceName()) {
			sql = Db.use(this.dataSourceName).getConfig().getDialect().forDbFindById(getTableName(), pKeys);
			result = Db.use(this.dataSourceName).find(sql, id);
		}else {
			sql = Db.use().getConfig().getDialect().forDbFindById(getTableName(), pKeys);
			result = Db.find(sql, id);
		}
		
		return result.size() > 0 ? result.get(0) : null;
	}
	
	
	/**
	 * 根据字符串主键id得到一条记录
	 * @param <T>
	 * @param class1 要转的实体类的class
	 * @param id 联合主键的多个主键值
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public <T extends Model<T>> T getByIds(Class<T> class1,Object[] id) {
		T t = null;
		try {
			if (CheckDataSourceName()) {
					t = (T) class1.newInstance().use(this.dataSourceName).findByIdLoadColumns(id, "*");
			}else {
				t = (T) class1.newInstance().findByIdLoadColumns(id,"*");
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}
	
	/**
	 * 根据字符串主键id得到一条记录
	 * @param id 主键
	 * @return
	 */
	public Record getById(Object id) {
		Object [] ids = {id};
		return getByIds(ids);
	}
	
	/**
	 * 根据字符串主键id得到一条记录
	 * @param <T>
	 * @param id 联合主键的多个主键值
	 * @param class1 要转的实体类的class
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public <T extends Model<T>> T getById(Object id,Class<T> class1) {
		Object [] ids = {id};
		return getByIds(class1, ids);
	}
	
	
	/**
	 * 保存一条记录，根据是否有主键来决定新增还是修改(自动生成去横岗的guid)
	 * @param record
	 * @return
	 */
	@Deprecated
	public boolean save(Record record) {
		boolean flag = false;
		if (record.getStr(getPrimaryKey())==null||record.getStr(getPrimaryKey()).equals("")) {
			record.set(getPrimaryKey(), UUID.randomUUID().toString().replaceAll("-", ""));
			flag = CheckDataSourceName()?Db.use(this.dataSourceName).save(getTableName(), getPrimaryKey(), record):Db.save(getTableName(),getPrimaryKey(), record);
		}else {
			flag = CheckDataSourceName()?Db.use(this.dataSourceName).update(getTableName(),getPrimaryKey(), record):Db.update(getTableName(),getPrimaryKey(), record);
		}
		return flag;
	}
	
	/**
	 * 保存一条记录，根据是否有主键来决定新增还是修改(自动生成自增的guid，用作排序)
	 * @param record
	 * @return
	 */
	@Deprecated
	public boolean saveLongGuid(Record record) {
		boolean flag = false;
		if (record.getStr(getPrimaryKey())==null||record.getStr(getPrimaryKey()).equals("")) {
			record.set(getPrimaryKey(), SnowflakeIdWorker.FakeGuid());
			flag = CheckDataSourceName()?Db.use(this.dataSourceName).save(getTableName(),getPrimaryKey(), record):Db.save(getTableName(),getPrimaryKey(), record);
		}else {
			flag = CheckDataSourceName()?Db.use(this.dataSourceName).update(getTableName(),getPrimaryKey(), record):Db.update(getTableName(),getPrimaryKey(), record);
		}
		return flag;
	}
	
	/**
	 * 新增
	 * @param record
	 * @return
	 */
	public boolean insert(Record record) {
		return CheckDataSourceName()?Db.use(this.dataSourceName).save(getTableName(),getPrimaryKey(), record):Db.save(getTableName(),getPrimaryKey(), record);
	}
	
	/**
	 * 新增
	 * @param <T>
	 * @param t jfinal实体对象
	 * @return
	 */
	public <T extends Model<T>> boolean insert(T t) {
		return t.save();
	}
	
	/**
	 * 修改
	 * @param record
	 * @return
	 */
	public boolean update(Record record) {
		return CheckDataSourceName()?Db.use(this.dataSourceName).update(getTableName(), getPrimaryKey(),record):Db.update(getTableName(), getPrimaryKey(),record);
	}
	
	/**
	 * 修改
	 * @param <T>
	 * @param t jfinal实体对象
	 * @return
	 */
	public <T extends Model<T>> boolean update(T t) {
		return t.update();
	}
	
	/**
	 * 删除本表的所有数据(慎用，无条件，会硬删所有数据)
	 * @return
	 */
	@Deprecated
	public int deleteAll() {
//		return CheckDataSourceName()?Db.use(this.dataSourceName).delete("DELETE FROM "+getTableName()):Db.delete("DELETE FROM "+getTableName());
		try {
			throw new Exception("此方法太危险，之后不再提供，请自己去写delete语句调用，谢谢！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * 根据条件删除数据（慎用，会硬删数据）
	 * @param whereHelper
	 * @return
	 */
	@Deprecated
	public int deleteByWhere(WhereHelper whereHelper) {
//		return CheckDataSourceName()?Db.use(this.dataSourceName).delete("DELETE FROM "+getTableName()+CheckWhereHelper(whereHelper)):Db.delete("DELETE FROM "+getTableName()+CheckWhereHelper(whereHelper));
		try {
			throw new Exception("此方法太危险，之后不再提供，请自己去写delete语句调用，谢谢！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * 根据主键id删除一条记录(慎用，会硬删数据)
	 * @param id
	 * @return
	 */
	@Deprecated
	public boolean deleteById(String id) {
//		return CheckDataSourceName()?Db.use(this.dataSourceName).deleteById(getTableName(),getPrimaryKey(), id):Db.deleteById(getTableName(),getPrimaryKey(), id);
		try {
			throw new Exception("此方法太危险，之后不再提供，请自己去写delete语句调用，谢谢！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 根据主键软删除一条记录
	 * @param id
	 * @return
	 */
	public boolean deleteSoftById(String id) {
		boolean flag = false;
		try {
		if (getSoftDelField()==null||getSoftDelField().equals("")) {
				throw new Exception(this.getClass().getName()+"没有指定该表的软删除字段，不允许进行软删除");
		}
		Record record = new Record();
		record.set(getPrimaryKey(), id);
		record.set(getSoftDelField(), DEL);
		flag = CheckDataSourceName()?Db.use(this.dataSourceName).update(getTableName(), getPrimaryKey(), record):Db.update(getTableName(), getPrimaryKey(), record);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 批量新增
	 * @param list
	 * @return 返回每条sql影响的行数
	 */
	public int[] batchInsert(List<Record> list) {
		return CheckDataSourceName()?Db.use(this.dataSourceName).batchSave(getTableName(), list, list.size()):Db.batchSave(getTableName(), list, list.size());
	}
	
	
	/**
	 * 批量修改
	 * @param list
	 * @return 返回每条sql影响的行数
	 */
	public int[] batchUpdate(List<Record> list) {
		return CheckDataSourceName()?Db.use(this.dataSourceName).batchUpdate(getTableName(), list, list.size()):Db.batchUpdate(getTableName(), list, list.size());
	}
	
	
	/**
	 * 批量保存，根据主键是否存在来决定是insert还是update
	 * @param list
	 * @return 执行成功的条数
	 */
	@Deprecated
	public int batchSave(List<Record> list) {
		int count = 0;
		for (Record record : list) {
			boolean flag = save(record);
			if (flag) {
				count++;
			}
		}
		return count;
	}
	
	
	/**
	 * 批量保存，根据flag标记来判断删除还是新增修改(c新增u修改r读取d删除)u在无id的情况下为新增
	 * @param kpiList
	 * @return
	 */
	@Deprecated
	public boolean batchCURDSaveList(List<Record> list) {
		boolean flag = true;
		 for (Record record : list) {
			 String recordFlag = record.getStr(EDIT_FLAG);
			 String id = record.getStr(getPrimaryKey());
			 if (recordFlag!=null&&recordFlag.equals("d")&&id!=null&&!id.equals("")) {
				 record.remove(EDIT_FLAG);
				flag  =deleteById(id);
			}else if(recordFlag!=null&&(recordFlag.equals("c")||recordFlag.equals("u"))) {
				record.remove(EDIT_FLAG);
				flag = saveLongGuid(record);
			}
		}
		 return flag;
	}
	
	/**
	 * 添加一个curd_flag=r的K-V到record对象中
	 * @param record
	 * @return
	 */
	@Deprecated
	public static Record setCURDFlag(Record record,String curdValue) {
		record.set(EDIT_FLAG, curdValue);
		return record;
	}
	/**
	 * 批量的添加一个curd_flag=r的K-V到record对象中
	 * @param record
	 * @return
	 */
	@Deprecated
	public static List<Record> setCURDFlag(List<Record> records,String curdValue) {
		for (Record record : records) {
			record.set(EDIT_FLAG, curdValue);
		}
		return records;
	}
	
}
