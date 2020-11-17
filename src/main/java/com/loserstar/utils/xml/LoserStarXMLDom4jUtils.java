package com.loserstar.utils.xml;

import java.util.Map;

import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 
 * author: loserStar
 * date: 2020年11月17日下午3:40:09
 * email:362527240@qq.com
 * remarks:基于dom4j的xml工具类
 */
public class LoserStarXMLDom4jUtils {

	public static void main(String[] args) {
		 Document document = DocumentHelper.createDocument();      
         Element root = document.addElement("members");// 创建根节点      
         Element abc = root.addElement("abc");
         abc.addCDATA("loserstar");
         root.addElement("def");
         String xmlString = document.asXML();
         System.out.println("xml1:");
         System.out.println(xmlString);
         
         
         Document document2 = createDocument("root");
         Element root2 = document2.getRootElement();
         System.out.println("xml2:");
         System.out.println(document2.asXML());
         System.out.println(root2.asXML());
	}
	
	/**
	 * 创建一个文档
	 * @return
	 */
	public static Document createDocument() {
		return DocumentHelper.createDocument();
	}
	
	/**
	 * 创建一个文档并添加一个根节点
	 * @param rootKey
	 * @return
	 */
	public static Document createDocument(String rootKey) {
		Document document = DocumentHelper.createDocument();
		document.addElement(rootKey);
		return document;
	}
	
	/**
	 * 输出为字符串
	 * @param document
	 * @return
	 */
	public static String getString(Document document) {
		return document.asXML();
	}
	
	/**
	 * 添加一个空的子节点
	 * @param branch 父节点对象
	 * @param key 子节点名称
	 * @return
	 */
	public static Element createElement(Branch branch,String key) {
		return branch.addElement(key);
	}
	
	/**
	 * 添加一个子节点，并且指定一个字符串值
	 * @param branch 父节点对象
	 * @param key 子节点名称
	 * @param value 子节点值
	 * @return
	 */
	public static Element createElement_Text(Branch branch,String key,String value){
		Element element = branch.addElement(key);
		element.setText(value);
		return element;
	}
	
	/**
	 * 添加一个子节点，并且指定一个用CDATA包裹的字符串值
	 * @param branch 父节点对象
	 * @param key 子节点名称
	 * @param value 子节点值
	 * @return
	 */
	public static Element createElement_CDATA(Branch branch,String key,String value){
		Element element = branch.addElement(key);
		element.setText(value);
		return element;
	}
	
	/**
	 * 遍历map里的所有key作为子节点添加进去
	 * @param branch 父节点对象
	 * @param map 打算作为子节点的map
	 */
	public static void createElement_Text(Branch branch,Map<String, Object> map){
		for (Map.Entry<String, Object> entry: map.entrySet()) {
			Element element = branch.addElement(entry.getKey());
			element.setText(entry.getValue()==null?"":entry.getValue().toString());
		}
	}
	
	/**
	 * 遍历map里的所有key作为子节点，value作为子节点的值添加进去，并使用CDATA包裹起来
	 * @param branch 父节点对象
	 * @param map 打算作为子节点的map
	 */
	public static void createElement_CDATA(Branch branch,Map<String, Object> map){
		for (Map.Entry<String, Object> entry: map.entrySet()) {
			Element element = branch.addElement(entry.getKey());
			element.addCDATA(entry.getValue()==null?"":entry.getValue().toString());
		}
	}
}
