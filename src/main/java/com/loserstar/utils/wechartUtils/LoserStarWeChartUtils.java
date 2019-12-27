/**
 * author: loserStar
 * date: 2019年5月22日上午10:40:54
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.wechartUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.loserstar.utils.ObjectMapConvert.LoserStarObjMapConvertUtil;
import com.loserstar.utils.date.LoserStarDateUtils;
import com.loserstar.utils.http.LoserStarHttpUtil;
import com.loserstar.utils.json.LoserStarJsonUtil;

/**
 * author: loserStar
 * date: 2019年5月22日上午10:40:54
 * remarks:企业微信调用工具类
 */
public class LoserStarWeChartUtils {

	private String corpid = "";//企业id
	private String secret = "";////企业门户待办应用的secret
	private String agentId = "";//企业门户待办应用功能的agentId
	
	/**
	 * @param corpid
	 * @param secret
	 * @param agentId
	 */
	public LoserStarWeChartUtils(String corpid, String secret, String agentId) {
		super();
		this.corpid = corpid;
		this.secret = secret;
		this.agentId = agentId;
	}
	
	/**
	 * 检测基本参数是否存在
	 * @throws Exception
	 */
	private void checkParam() throws Exception {
		if (this.corpid==null||this.corpid.equals("")) {
			throw new Exception("没有找到corpid，企业id，该参数在企业微信后台“我的企业”可以找到，请使用带参数的构造函数初始化或者调用init方法初始化");
		}
		if (this.secret==null||this.secret.equals("")) {
			throw new Exception("没有找到secret，自定义应用的secret，该参数在企业微信后台的自建应用里可以看到");
		}
//		if (this.agentId==null||this.agentId.equals("")) {
//			throw new Exception("没有找到agentId，自定义应用的agentId，该参数在企业微信后台的自建应用里可以看到");
//		}
	}
	/**
	 * 初始化
	 * @param corpid
	 * @param secret
	 * @param agentId
	 */
	public void init(String corpid,String secret,String agentId) {
		this.corpid = corpid;
		this.secret = secret;
		this.agentId = agentId;
	}
	
	/**
	 * 获取token
	 * @param corpid
	 * @param secret
	 * @return
	 * @throws Exception 
	 */
	public String getToken() throws Exception {
		checkParam();
		String getTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+this.corpid+"&corpsecret="+this.secret;
		String result = LoserStarHttpUtil.get(getTokenUrl, null);
		Map<String, Object> tokenResult = LoserStarJsonUtil.toModel(result, Map.class);
		String access_token = tokenResult.get("access_token").toString();
		return access_token;
	}
	
	/**
	 * 以某应用发送消息给某人
	 * @param token
	 * @param agentId
	 * @param userid 企业微信用户userid
	 * @param msg 消息
	 * @return
	 * @throws Exception 
	 */
	public String sendMsg(String token,String userid,String msg) throws Exception {
		checkParam();
		if (this.agentId==null||this.agentId.equals("")) {
			throw new Exception("没有找到agentId，自定义应用的agentId，该参数在企业微信后台的自建应用里可以看到");
		}
		String sendUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+token;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("touser", userid);
		paramMap.put("toparty", "");
		paramMap.put("totag", "");
		paramMap.put("agentid", this.agentId);
		paramMap.put("msgtype", "text");
		Map<String, String> text = new HashMap<String, String>();
//		text.put("content", "这是测试"+LoserStarDateUtils.format(new Date())+"<a href='https://open.weixin.qq.com/connect/oauth2/authorize?appid=wwf137351476e08eb5&redirect_uri=http://wx.hongta.com:8081/EPMobile/test/index.do&response_type=code&scope=snsapi_userinfo&agentid=1000003&state=STATE#wechat_redirect'>登录大厅办理</a>");
		if (msg==null||msg.equals("")) {
			msg = "这是测试"+LoserStarDateUtils.format(new Date())+"<a href='http://www.baidu.com'>百度链接MainTest</a>";
		}
		text.put("content",msg );
		paramMap.put("text", text);
		paramMap.put("safe", 0);
		String param = LoserStarJsonUtil.toJsonDeep(paramMap);
		String result = LoserStarHttpUtil.post(sendUrl, null, param);
		return result;
	}
	
	/**
	 * 更新成员信息，参考https://work.weixin.qq.com/api/doc#90000/90135/90197
	 * @param token
	 * @param userId
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */
	public String updateUserInfo(String token,String userId,Map<String, Object> paramMap) throws Exception {
		checkParam();
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token="+token;
		paramMap.put("userid", userId);
		String postJson = LoserStarJsonUtil.toJsonDeep(paramMap);
		String result = LoserStarHttpUtil.post(url, null, postJson);
		return result;
	}
	
	/**
	 * 获取部门信息
	 * @param token
	 * @param departmentId 部门id。获取指定部门及其下的子部门。 如果不填，默认获取全量组织架构
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getDepartmentList(String token,String departmentId) throws Exception{
		String url = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token="+token;
		if (departmentId!=null&&!departmentId.equals("")) {
			url+="&id="+departmentId;
		}
		String resultJson = LoserStarHttpUtil.get(url, null);
		Map<String, Object> resultMap = LoserStarJsonUtil.toModel(resultJson, Map.class);
		String errorcode = resultMap.get("errcode").toString();
		String errmsg = resultMap.get("errmsg").toString();
		if (!errorcode.equals("0")) {
			throw new Exception(resultJson);
		}
		List<Map<String, Object>> departmentList = (List<Map<String, Object>>) LoserStarObjMapConvertUtil.ConvertObjectToList(resultMap.get("department"));
		return departmentList;
	}
	
	/**
	 * 获取部门下的成员
	 * @param token
	 * @param departmentId weixin的部门id
	 * @param fetch_child 是否递归部门下的成员
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getUserListByDepartmentId(String token,int departmentId,boolean fetch_child) throws Exception{
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token="+token+"&department_id="+departmentId;
		if (fetch_child) {
			url+="&fetch_child=1";
		}else {
			url+="&fetch_child=0";
		}
		String resultJson = LoserStarHttpUtil.get(url, null);
		Map<String, Object> resultMap = LoserStarJsonUtil.toModel(resultJson, Map.class);
		String errorcode = resultMap.get("errcode").toString();
		String errmsg = resultMap.get("errmsg").toString();
		if (!errorcode.equals("0")) {
			throw new Exception(resultJson);
		}
		List<Map<String, Object>> userList = (List<Map<String, Object>>) LoserStarObjMapConvertUtil.ConvertObjectToList(resultMap.get("userlist"));
		return userList;
	}
}
