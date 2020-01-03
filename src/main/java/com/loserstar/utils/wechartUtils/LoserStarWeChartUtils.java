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
import com.loserstar.utils.string.LoserStarStringUtils;

/**
 * author: loserStar
 * date: 2019年5月22日上午10:40:54
 * remarks:企业微信调用工具类
 */
public class LoserStarWeChartUtils {
	
	
	/**
	 * 获取token
	 * @param corpid 参数在企业微信后台“我的企业”可以找到
	 * @param secret 该参数在企业微信后台的自建应用里可以看到
	 * @return
	 * @throws Exception 
	 */
	public static String getToken(String corpid,String secret) throws Exception {
		if (null==corpid||"".equals(corpid)) {
			throw new Exception("没有找到corpid，企业id，该参数在企业微信后台“我的企业”可以找到，请使用带参数的构造函数初始化或者调用init方法初始化");
		}
		if (null==secret||"".equals(secret)) {
			throw new Exception("没有找到secret，自定义应用的secret，该参数在企业微信后台的自建应用里可以看到");
		}
		String getTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+corpid+"&corpsecret="+secret;
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
	public static String sendMsg(String token,String agentId,String userid,String msg) throws Exception {
		if (null==agentId||"".equals(agentId)) {
			throw new Exception("没有找到agentId，自定义应用的agentId，该参数在企业微信后台的自建应用里可以看到");
		}
		String sendUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+token;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("touser", userid);
		paramMap.put("toparty", "");
		paramMap.put("totag", "");
		paramMap.put("agentid", agentId);
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
	public static String updateUserInfo(String token,String userId,Map<String, Object> paramMap) throws Exception {
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
	public static List<Map<String, Object>> getDepartmentList(String token,String departmentId) throws Exception{
		String url = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token="+token;
		if (departmentId!=null&&!departmentId.equals("")) {
			url+="&id="+departmentId;
		}
		String resultJson = LoserStarHttpUtil.get(url, null);
		Map<String, Object> resultMap = LoserStarJsonUtil.toModel(resultJson, Map.class);
		String errorcode = resultMap.get("errcode").toString();
		String errmsg = resultMap.get("errmsg").toString();
		if (!errorcode.equals("0")) {
			if(errorcode.equals("42001")) {
				//token失效，需要重新获取
				throw new Exception("token失效");
			}else {
				throw new Exception(resultJson);
			}
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
	public static List<Map<String, Object>> getUserListByDepartmentId(String token,int departmentId,boolean fetch_child) throws Exception{
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
			if(errorcode.equals("42001")) {
				//token失效，需要重新获取
				throw new Exception("token失效");
			}else {
				throw new Exception(resultJson);
			}
		}
		List<Map<String, Object>> userList = (List<Map<String, Object>>) LoserStarObjMapConvertUtil.ConvertObjectToList(resultMap.get("userlist"));
		return userList;
	}
	
	/**
	 * 根据token和code，获取包含企业微信用户的唯一凭证UserId的map
	 * 但也可能是
	 * @param token
	 * @param code
	 * @return
	 * a) 当用户为企业成员时返回示例如下：
	 * 参数	说明
		errcode	返回码
		errmsg	对返回码的文本描述内容
		UserId	成员UserID。若需要获得用户详情信息，可调用通讯录接口：读取成员
		DeviceId	手机设备号(由企业微信在安装时随机生成，删除重装会改变，升级不受影响)
		
		b) 非企业成员授权时返回示例如下：
		errcode	返回码
		errmsg	对返回码的文本描述内容
		OpenId	非企业成员的标识，对当前企业唯一
		DeviceId	手机设备号(由企业微信在安装时随机生成，删除重装会改变，升级不受影响)
	 */
	@SuppressWarnings("unchecked")
	public  static Map<String, Object> getWeixinUserInfoForMap(String token,String code){
		String getTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token="+token+"&code="+code;
		String result = LoserStarHttpUtil.get(getTokenUrl, null);
		return LoserStarJsonUtil.toModel(result, Map.class);
	}
	
	/**
	 * 根据token和code，获取企业微信用户的唯一凭证UserId
	 * @param token
	 * @param code
	 * @return
	 * @throws Exception 
	 */
	public  static String getWeixinUserInfoForString(String token,String code) throws Exception{
		Map<String, Object> resultMap = getWeixinUserInfoForMap(token, code);
		String errcode = LoserStarStringUtils.toString(resultMap.get("errcode")); 
		if (errcode.equals("")||!errcode.equals("0")) {
			throw new Exception("获取userId失败："+resultMap.get("errmsg"));
		}else if(errcode.equals("42001")) {
			//token失效，需要重新获取
			throw new Exception("token失效");
		}
		String userId = LoserStarStringUtils.toString(resultMap.get("UserId"));
		if (userId.equals("")) {
			String OpenId = LoserStarStringUtils.toString(resultMap.get("OpenId"));
			throw new Exception("没有获取到UserId,当前属于非企业成员的授权，请勿使用个人微信访问，必须使用企业微信进行访问，或尝试在企业微信的“设置”里切换到对应的企业，OpenId="+OpenId);
		}
		return userId;
	}
	
	/**
	 * 根据token和企业微信用户的唯一拼争UserId获取用户的详细信息
	 * @param token
	 * @param userId
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public  static Map<String, Object> getWeixinUserInfoDetail(String token,String userId) throws Exception{
		String getTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token="+token+"&userid="+userId;
		String result = LoserStarHttpUtil.get(getTokenUrl, null);
		Map<String, Object> resultMap = LoserStarJsonUtil.toModel(result, Map.class);
		String errcode = LoserStarStringUtils.toString(resultMap.get("errcode")); 
		if (errcode.equals("")||!errcode.equals("0")) {
			throw new Exception("获取用户详细信息失败："+resultMap.get("errmsg"));
		}else if(errcode.equals("42001")) {
			//token失效，需要重新获取
			throw new Exception("token失效");
		}
		return resultMap;
	}
	
	/**
	 * 获取使用js-sdk需要的JsapiTicket（用于生成签名）
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String JS_SDK_getJsapiTicket(String token) throws Exception {
		String JsapiTicket = "";
		String getTokenUrl ="https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token="+token;
		String result = LoserStarHttpUtil.get(getTokenUrl, null);
		Map<String, Object> resultMap = LoserStarJsonUtil.toModel(result, Map.class);
		String errcode = LoserStarStringUtils.toString(resultMap.get("errcode")); 
		if (errcode.equals("")||!errcode.equals("0")) {
			throw new Exception("获取jsapi_ticket信息失败："+resultMap.get("errmsg"));
		}else {
			JsapiTicket = LoserStarStringUtils.toString(resultMap.get("ticket"));
		}
		return JsapiTicket;
	}
}
