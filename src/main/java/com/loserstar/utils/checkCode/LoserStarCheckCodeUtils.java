package com.loserstar.utils.checkCode;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;

import com.loserstar.utils.random.RandomUtils;

public class LoserStarCheckCodeUtils {
	
	/**
	 * 生成一个包含数字和字母的随机验证码
	 * @param count 要生成的个数
	 * @return
	 */
	public static String genCheckCode(int count) {
		String[] letterArray = {"A","B","C","D","E","F","G","H","J","K","M","N","P","Q","R","S","T","U","V","W","X","Y","Z",
				"2","3","4","5","6","7","8","9"};
		String checkCode ="";
		for (int i = 0; i <count; i++) {
			checkCode += letterArray[RandomUtils.getRandomInt(0, 31)];
		}
		System.out.println("生成验证码：");
		System.out.println(checkCode);
		return checkCode;
	}
	
	/**
	 * 生成一个纯字母的随机验证码
	 * @param count 要生成的个数
	 * @return
	 */
	public static String genCheckCodeForLetter(int count) {
		String[] letterArray = {"A","B","C","D","E","F","G","H","J","K","M","N","P","Q","R","S","T","U","V","W","X","Y","Z"};
		String checkCode ="";
		for (int i = 0; i <count; i++) {
			checkCode += letterArray[RandomUtils.getRandomInt(0, 23)];
		}
		System.out.println("生成验证码：");
		System.out.println(checkCode);
		return checkCode;
	}
	
	
	/**
	 * 生成一个纯数字的随机验证码
	 * @param count
	 * @return
	 */
	public static String genCheckCodeForNumber(int count) {
		String[] letterArray = {"1","2","3","4","5","6","7","8","9","0"};
		String checkCode ="";
		for (int i = 0; i <count; i++) {
			checkCode += letterArray[RandomUtils.getRandomInt(0, 10)];
		}
		System.out.println("生成验证码：");
		System.out.println(checkCode);
		return checkCode;
	}
	
    public static BufferedImage getImage(String code) {
        int width = 200, height = 50;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.gray);
        graphics.fillRect(0, 0, width, height);  //绘制验证码图像背景为红色
        Font currentFont = graphics.getFont();
        Font newFont = new Font(currentFont.getFontName(), currentFont.getStyle(), 50);
        FontMetrics fontMetrics = graphics.getFontMetrics(newFont);
        String string = code;
        graphics.setColor(Color.green);
        graphics.setFont(newFont);
        //绘制绿色的验证码，并使其居中
        graphics.drawString(string, (width - fontMetrics.stringWidth(string)) / 2, (height - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent());
        graphics.setColor(Color.blue);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 10; ++i) {  //绘制20条干扰线，其颜色为蓝
            int x1 = random.nextInt(width), y1 = random.nextInt(height);
            int x2 = random.nextInt(width), y2 = random.nextInt(height);
            graphics.drawLine(x1, y1, x2, y2);
        }
        return image;
    }
}