/**
 * author: loserStar
 * date: 2019年8月13日下午3:55:53
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.numToRmb;

/**
 * author: loserStar
 * date: 2019年8月13日下午3:55:53
 * remarks:
 */
public class LoserStarNumToRmbUtil {
	private static String[] hangArr = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
    private static String[] unitArr = { "圆", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰" };
    private static String[] unitXiaoArr = { "分", "角" };
    
    /**
     * 把一个浮点数分为整数和小数
     * @param num
     * @return
     */
    public static String[] divide(double num) {
        long zheng = (long) num;
        long xiao = Math.round((num - zheng) * 100);
        String[] arr = new String[] { String.valueOf(zheng), String.valueOf(xiao) };
        return arr;
    }
	
    /**
     * 把一个数字转化为汉子
     * @param numStr
     * @return
     */
    public static String toHantr(double numStr) {
        if (numStr < 0) {
            System.out.print("请输入一个整数");
        }
        String[] arr1 = divide(numStr);
        // 得到整数部分
        String zheng = arr1[0];
        // 得到小数部分
        String xiao = arr1[1];
        String result = "";
        // 处理整数部分
        int numLen = zheng.length();


        for (int i = 0; i < numLen; i++) {
            // 得到第i位的数值
            int num = zheng.charAt(i) - '0';
            // 数值的汉字表示
            result += hangArr[num] + unitArr[numLen - 1 - i];
        }
        // 处理小数部分
        int sumLen = xiao.length();
        for (int i = 0; i < sumLen; i++) {
            if (i > 2)
            break;// 只精确到后两位
            // 得到第i位的数值
            int xnum = xiao.charAt(i) - '0';
            // 数值的汉字表示
            result += hangArr[xnum] + unitXiaoArr[sumLen - 1 - i];
        }
        // //替换所有零拾，零百，零仟为零
        result = result.replaceAll("零拾", "零");
        result = result.replaceAll("零佰", "零");
        result = result.replaceAll("零仟", "零");
        //把零零替换为零
        if (result.indexOf("零零") != -1) {
            result = result.replaceAll("零零", "零");
        }
        result = result.replaceAll("零亿", "亿");
        result = result.replaceAll("零万", "万");
        result = result.replaceAll("零圆", "圆");

        return result;
    }
    
    
    public static void main(String[] args) {
        System.out.print(toHantr(550*10000));
    }
}
