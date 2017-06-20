/**
 * author: loserStar
 * date: 2017年6月20日下午4:37:29
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks: json示例
 */
package loserStarUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.loserstar.utils.json.GsonUtils;
import com.loserstar.utils.json.JacksonUtils;

public class JsonTest {

	@Test
	public void jacsonTest() throws IOException {
		UserTestVo user = new UserTestVo();
		user.setId(1L);
		user.setName("luoxinxin");
		user.setHands(Arrays.asList("left","rigth","up","down"));
		Map<String, Object> goods = new HashMap<>(); 
		UserTestVo good = new UserTestVo();
		good.setId(11);//被gson注解忽略，Jackson不会忽略
		good.setName("hehe");
		good.setHands(Arrays.asList("ear"));
		goods.put("ear", good);
		user.setGoods(goods);
		System.out.println("---------jackson--------------");
		System.out.println(JacksonUtils.toJson(user));
		
		System.out.println("--------gson----------------");
		System.out.println(GsonUtils.toJson(user));
		
		System.out.println("----------反序列化--------------");
		user.setId(999);
		user.setName("userDeserialize");
		System.out.println("反序列化的字符串:"+JacksonUtils.toJson(user));
		String jsonStr = JacksonUtils.toJson(user);
		//jacson反序列化
		UserTestVo userDeserialize_jacson = JacksonUtils.toObject(jsonStr, UserTestVo.class);
		System.out.println(userDeserialize_jacson.getName());
		//gson反序列化
		UserTestVo userDeserialize_gson = GsonUtils.fromJson(jsonStr, UserTestVo.class);
		System.out.println(userDeserialize_gson.getGoods().get("ear"));
	}
}
