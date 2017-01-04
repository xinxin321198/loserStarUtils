/**
 * author: lxx
 * version: 2016年10月8日下午3:14:48
 * email:362527240@qq.com
 * remarks:
 */
package com.loserstar.utils.random;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.loserstar.utils.date.DateUtils;


public class RandomUtils {
	public static Random r = new Random();
	
	/**
	 * 根据一个范围，得到这个返回内的随机整数[begin,end)
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int getRandomInt(int begin,int end){
		return begin+r.nextInt(end-begin);
	}
	
	/**
	 * 根据一个小数的个数，返回对应的小数位的double值
	 * @param count
	 * @return
	 */
	
	/**
	 * 根据一个范围和小数位数，返回一个随机的double数,[begin,end]
	 * @param begin 最小数
	 * @param end 最大数
	 * @param decimalPlace 小数位
	 * @return
	 */
	public static double getRandomDouble(double begin,double end,int decimalPlace){
		StringBuffer sBuffer = new StringBuffer("#.");
		for (int i = 0; i < decimalPlace; i++) {
			sBuffer.append("0");
		}
		DecimalFormat dFormat = new DecimalFormat(sBuffer.toString());
		return Double.parseDouble(dFormat.format(r.nextDouble() *(end-begin)+begin));
	}
	
	
	/**
	 * 得到随机蔬菜基地
	 * @return
	 */
	public static String getRandomStartAddr(){
		int max = 6;
		String[] startAddrArray = new String[max];
		startAddrArray[0] = "通海长河村基地";
		startAddrArray[1] = "通海大梨树村";
		startAddrArray[2] = "江川白衣寨农业基地";
		startAddrArray[3] = "江川前卫镇生鲜基地";
		startAddrArray[4] = "江川丁家庄基地";
		startAddrArray[5] = "江川广茂镇";
		return  startAddrArray[r.nextInt(max-1)];
	}
	
	
	/**
	 * 得到随机车牌号
	 * @return
	 */
	public static String getRandomCarNo(){
		int max = 5;
		String[] carNoArray = new String[max];
		carNoArray[0] = "云F 034DE";
		carNoArray[1] = "云F 89E71";
		carNoArray[2] = "云F 654A7";
		carNoArray[3] = "云F 63U85";
		carNoArray[4] = "云F 87R51";
		
		return carNoArray[r.nextInt(max-1)];
	}
	
	/**
	 * 得到当天的从小到大排好序的一个时间集合，时分秒随机
	 * @param date 年月日部分的参数传进来
	 * @param count 要获取几个时间
	 * @return
	 */
	public static List<Date> getSortRandomTimeList(Date date,int count){
		List<Date> timeList = new ArrayList<Date>();
		for (int i = 0; i < count; i++) {
			timeList.add(getRandomTime(date));
		}
		Collections.sort(timeList);
		return timeList;
	}
	
	/**
	 * 得到某一年月日的时间，时分秒随机
	 * @param date
	 * @return
	 */
	public static Date getRandomTime(Date date){
		DecimalFormat df = new DecimalFormat("00");
		return DateUtils.parseDate(DateUtils.formatDate(date, "yyyy-MM-dd")+" "+df.format(r.nextInt(24))+":"+df.format(r.nextInt(60))+":"+df.format(r.nextInt(60)));
	}
}
