/**
 * author: loserStar
 * date: 2018年4月17日下午3:58:37
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.array;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.loserstar.utils.json.LoserStarJsonUtil;

import jodd.util.ArraysUtil;

/**
 * author: loserStar
 * date: 2018年4月17日下午3:58:37
 * remarks:
 */
public class LoserStarArrayAppliactionMain {
	public static class Students {  
	      
	    private int age;  
	    private int score;  
	      
	    public Students(int age, int score){  
	        super();  
	        this.age = age;  
	        this.score = score;  
	    }  
	      
	    public int getAge() {  
	        return age;  
	    }  
	    public void setAge(int age) {  
	        this.age = age;  
	    }  
	    public int getScore() {  
	        return score;  
	    }  
	    public void setScore(int score) {  
	        this.score = score;  
	    }  
	}  
	public static void main(String[] args) {
		
		List<String> list = LoserStarArrayUtils.asList("loserStar","lxx","yuxi","huaning","lxx");
		LoserStarArrayUtils.desc(list);
		System.out.println(LoserStarArrayUtils.removeDuplicate(list));
		
		//自定义比较器排序
	       List<Students> students = new ArrayList<Students>();  
	        students.add(new Students(23, 100)); 
	        students.add(new Students(27, 98));  
	        students.add(new Students(29, 99));  
	        students.add(new Students(29, 98));  
	        students.add(new Students(22, 89));  
	        Collections.sort(students,new Comparator<Students>() {
			@Override
			public int compare(Students o1, Students o2) {
				if (o1.getAge()>o2.getAge()) {
					return 1;//大于
				}else if (o1.getAge()<o2.getAge()) {
					return -1;//小于
				}else {
					return 0;
				}
			}
		});
		System.out.println(LoserStarJsonUtil.toJsonSimple(students));
	}
}
