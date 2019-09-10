/**
 * author: loserStar
 * date: 2019年9月6日下午5:31:13
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.html;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * author: loserStar
 * date: 2019年9月6日下午5:31:13
 * remarks: 解析html的类（基于jsoup，jdk1.6最高支持到1.10.3版本）
 */
public class LoserStarHtmlUtil {

	public static void main(String[] args) throws IOException {
		Document document = Jsoup.connect("http://www.baidu.com").get();
	    Elements elements = document.select("#u1 a");
	    System.out.println(elements.outerHtml());
	    for (Element element : elements) {
	    	System.out.println(element.text());
		}
	}
}
