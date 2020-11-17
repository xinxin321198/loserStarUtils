package com.loserstar.utils.xml;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.loserstar.utils.string.LoserStarStringUtils;

/**
 * 
 * author: loserStar
 * date: 2020年11月6日上午9:33:28
 * email:362527240@qq.com
 * remarks:xml相关工具类
 */
public class LoserStarXmlW3CDomUtils {

	/**
	 * 创建document对象
	 * @return
	 * @throws ParserConfigurationException
	 */
	public static Document createDocument() throws ParserConfigurationException {
		// 创建解析器工厂			
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = factory.newDocumentBuilder();
		Document document = db.newDocument();
		return document;
	}
	
	public static Transformer createTransformer() throws TransformerConfigurationException {
		// 创建TransformerFactory对象
		TransformerFactory tff = TransformerFactory.newInstance();
		// 创建 Transformer对象
		Transformer tf = tff.newTransformer();
		return tf;
	}
	/**
	 * 输出为字符串
	 * @param document
	 * @param transform
	 * @return
	 * @throws TransformerException
	 */
	public static String getString(Document document,Transformer transform) throws TransformerException {
		Writer writer = new StringWriter();
		transform.transform(new DOMSource(document), new StreamResult(writer));
		return writer.toString();
	}
	
	/**
	 * 创建一个节点
	 * @param document
	 * @param elementName 节点名称
	 * @return
	 */
	public static  Element createElement(Document document,String elementName) {
		return document.createElement(elementName);
	}
	
	/**
	 * 创建一个节点，把一个字符串放入该节点中
	 * @param document
	 * @param elementName 节点名称
	 * @param value 要放入节点的字符串
	 * @return
	 */
	public static  Element createElement(Document document,String elementName,String value) {
		Element element = document.createElement(elementName);
		element.setTextContent(value);
		return element;
	}
	
	/**
	 * 创建一个节点，并添加他的单个
	 * @param document
	 * @param elementName 该节点的名称
	 * @param value 要放入的子节点
	 * @return
	 */
	public static  Element createElement(Document document,String elementName,Element value) {
		Element element = document.createElement(elementName);
		element.appendChild(value);
		return element;
	}
	
	/**
	 * 创建一个节点，并添加该节点的子节点
	 * @param document
	 * @param elementName 节点名称
	 * @param values 子节点集合
	 * @return
	 */
	public static  Element createElement(Document document,String elementName,Collection<Element> values) {
		Element element = document.createElement(elementName);
		for (Element v : values) {
			element.appendChild(v);
		}
		return element;
	}
	
	/**
	 * 根据map里的key，生成节点集合
	 * @param document
	 * @param value map
	 * @return
	 */
	public static  List<Element> createElement(Document document,Map<String, Object> value) {
		List<Element> elements = new ArrayList<Element>();
		for (Map.Entry<String, Object> entry : value.entrySet()) {
			Element chiledElement = document.createElement(entry.getKey());
			chiledElement.setTextContent(LoserStarStringUtils.toString(entry.getValue()));
			elements.add(chiledElement);
		}
		return elements;
	}
	
	public static void setText(Element element,String text) {
		element.setTextContent(text);
	}
	
	public static void appendChild(Element element,Element childElement) {
		element.appendChild(childElement);
	}
	
	public static void setAttribute(Element element,String name,String value) {
		element.setAttribute(name, value);
	}
	
	
	/*public static String toXml(Map<String, Object> map) throws Exception {
		Document document = createDocument();
		Transformer tf = createTransformer();
		Element root = buildElement_root(document,map);
		buildElement_child(document,map.get(root.getTagName()), root);
		return getString(document, tf);
	}
	
	public static Element buildElement_root(Document document,Map<String, Object> map) throws Exception {
		Element element = null;
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (element != null) {
				throw new Exception("map里有且只能有一个根节点");
			}
			element = document.createElement(entry.getKey());
			document.appendChild(element);
		}
		return element;
	}
	
	public static void buildElement_child(Document document,Object object,Element prentElement) {
		if (object instanceof Map) {
			//如果是map，就遍历key，取值生成节点
			Map<String, Object> map = (Map<String, Object>) object;
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				Element chiledElement = document.createElement(entry.getKey());
				if (entry.getValue() instanceof Map) {
					buildElement_child(document, entry.getValue(), chiledElement);
				}else if (object instanceof List) {
					//如果是个数组，则遍历之后添加到父节点上
					List<Object> list = (List<Object>) object;
					for (Object o : list) {
						Element list_element = document.createElement(o.toString());
						if (o instanceof Map ||o instanceof List) {
							buildElement_child(document, o, list_element);
						}else {
							list_element.setTextContent(o.toString());
						}
						chiledElement.appendChild(list_element);
					}
				}else {
					chiledElement.setTextContent(entry.getValue().toString());
				}
				
				prentElement.appendChild(chiledElement);
			}
		}
	}*/
	
}
