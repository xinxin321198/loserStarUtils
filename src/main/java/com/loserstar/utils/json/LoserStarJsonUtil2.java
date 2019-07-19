package com.loserstar.utils.json;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

/**
 * 基于JSON-java的json处理类
 * https://github.com/stleary/JSON-java
 * author: loserStar
 * date: 2018年3月13日下午2:42:44
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
public class LoserStarJsonUtil2 {

	
	public static String toJson(Map<?, ?> m){
		JSONObject jsonObject = new JSONObject(m);
		return jsonObject.toString();
	}
	
	public static JSONObject toModel(String jsonStr){
		JSONObject jsonObject = new JSONObject(jsonStr);
		return jsonObject;
	}
	
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("luoxinxin", "123");
		map.put("wangping", "456");
		map.put("loserStar", "999999");
		String[] liStrings = {"1","a","b","z"};
//		map.put("list", liStrings);
		System.out.println(toJson(map));
		
		String jsonStr = "{\"code\":1,\"message\":\"\",\"data\":[{\"id\":3,\"parentId\":1,\"code\":\"/\",\"name\":\"问题管理\",\"icon\":\"settings\",\"type\":\"10\",\"sort\":20,\"enableFlag\":true,\"delFlag\":\"0\",\"authorities\":\"mbp_problem_info\",\"items\":[{\"id\":4,\"parentId\":3,\"code\":\"/problemInfo/list\",\"name\":\"问题查询\",\"icon\":\"settings\",\"type\":\"1\",\"sort\":30,\"enableFlag\":true,\"delFlag\":\"0\",\"authorities\":\"problemInfo_list\",\"field\":\"id\"}],\"field\":\"id\"},{\"id\":22,\"parentId\":1,\"code\":\"/\",\"name\":\"统计报表\",\"icon\":\"settings\",\"type\":\"10\",\"sort\":30,\"enableFlag\":true,\"delFlag\":\"0\",\"authorities\":\"mbp_report\",\"items\":[{\"id\":23,\"parentId\":22,\"code\":\"/problemInfo/report/0\",\"name\":\"问题统计\",\"icon\":\"settings\",\"type\":\"1\",\"sort\":15,\"enableFlag\":true,\"delFlag\":\"0\",\"authorities\":\"problemReport_category\",\"field\":\"id\"},{\"id\":24,\"parentId\":22,\"code\":\"/knowLedgeBase/problemquery\",\"name\":\"问题文档查询\",\"icon\":\"settings\",\"type\":\"1\",\"sort\":50,\"enableFlag\":true,\"delFlag\":\"0\",\"authorities\":\"knowLedgeProblem_query\",\"field\":\"id\"}],\"field\":\"id\"},{\"id\":27,\"parentId\":1,\"code\":\"/\",\"name\":\"问题简报管理\",\"icon\":\"settings\",\"type\":\"10\",\"sort\":40,\"enableFlag\":true,\"delFlag\":\"0\",\"authorities\":\"mbp_briefing\",\"items\":[{\"id\":28,\"parentId\":27,\"code\":\"/problemBriefing/list\",\"name\":\"问题简报管理\",\"icon\":\"settings\",\"type\":\"1\",\"sort\":20,\"enableFlag\":true,\"delFlag\":\"0\",\"component\":\"problemBriefingList\",\"componentCode\":\"./views/mbp/problemBriefing/problemBriefingList.vue\",\"componentRouter\":\"/problemBriefing/list\",\"authorities\":\"problemBriefing_list\",\"field\":\"id\"}],\"field\":\"id\"},{\"id\":36,\"parentId\":1,\"code\":\"/\",\"name\":\"知识库管理\",\"icon\":\"settings\",\"type\":\"10\",\"sort\":50,\"enableFlag\":true,\"delFlag\":\"0\",\"authorities\":\"mbp_knowLedge_base\",\"items\":[{\"id\":37,\"parentId\":36,\"code\":\"/knowLedgeBase/list/0\",\"name\":\"知识发布\",\"icon\":\"settings\",\"type\":\"1\",\"sort\":10,\"enableFlag\":true,\"delFlag\":\"0\",\"authorities\":\"knowLedgeBase_list\",\"field\":\"id\"},{\"id\":38,\"parentId\":36,\"code\":\"/knowLedgeBase/query\",\"name\":\"知识库查询\",\"icon\":\"settings\",\"type\":\"1\",\"sort\":20,\"enableFlag\":true,\"delFlag\":\"0\",\"authorities\":\"knowLedgeBase_query\",\"field\":\"id\"}],\"field\":\"id\"},{\"id\":160,\"parentId\":1,\"code\":\"/\",\"name\":\"员工热线\",\"icon\":\"settings\",\"type\":\"10\",\"sort\":203,\"enableFlag\":true,\"delFlag\":\"0\",\"authorities\":\"mbp_problem_info\",\"items\":[{\"id\":161,\"parentId\":160,\"code\":\"/problemInfo/add/2/18\",\"name\":\"员工热线\",\"icon\":\"settings\",\"type\":\"1\",\"sort\":12,\"enableFlag\":true,\"delFlag\":\"0\",\"authorities\":\"problemInfo_add\",\"field\":\"id\"}],\"field\":\"id\"},{\"id\":162,\"parentId\":1,\"code\":\"/\",\"name\":\"举报箱\",\"icon\":\"settings\",\"type\":\"10\",\"sort\":205,\"enableFlag\":true,\"delFlag\":\"0\",\"authorities\":\"mbp_problem_info\",\"items\":[{\"id\":163,\"parentId\":162,\"code\":\"/problemInfo/add/2/19\",\"name\":\"审计部举报箱\",\"icon\":\"settings\",\"type\":\"1\",\"sort\":12,\"enableFlag\":true,\"delFlag\":\"0\",\"authorities\":\"problemInfo_add\",\"field\":\"id\"}],\"field\":\"id\"}],\"extData\":{\"current\":{\"id\":4327,\"username\":\"01000119\",\"delFlag\":\"0\",\"fullName\":\"葛孚明\",\"authorities\":[],\"accountNonExpired\":true,\"accountNonLocked\":true,\"credentialsNonExpired\":true,\"enabled\":true,\"roleIds\":[],\"field\":\"id\"},\"orgLeader\":\"\",\"parentOrg\":\"\",\"roleids\":\"2\"}}";
		JSONObject model = toModel(jsonStr);
	}
}
