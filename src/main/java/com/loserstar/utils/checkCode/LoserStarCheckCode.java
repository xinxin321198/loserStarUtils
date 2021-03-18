package com.loserstar.utils.checkCode;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 验证码生成
 * author: loserStar
 * date: 2021年3月18日上午11:01:39
 * email:362527240@qq.com
 * remarks:
 */
public class LoserStarCheckCode {

    private StringBuilder verificationCodeContent;

    public LoserStarCheckCode(int length) {
        verificationCodeContent = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; ++i) {
            int charType = random.nextInt(3);  //随机的验证码字符类型，0表示数字，1表示小写字母，2表示大写字母
            char c;
            switch (charType) {
                case 0:
                    c = (char) (random.nextInt(10) + '0');
                    break;
                case 1:
                    c = (char) (random.nextInt(26) + 'a');
                    break;
                default:
                    c = (char) (random.nextInt(26) + 'A');
            }
            verificationCodeContent.append(c);
        }
    }

    public String getContent() {
        return verificationCodeContent.toString();
    }

    public BufferedImage getImage() {
        int width = 200, height = 50;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.gray);
        graphics.fillRect(0, 0, width, height);  //绘制验证码图像背景为红色
        Font currentFont = graphics.getFont();
        Font newFont = new Font(currentFont.getFontName(), currentFont.getStyle(), 50);
        FontMetrics fontMetrics = graphics.getFontMetrics(newFont);
        String string = verificationCodeContent.toString();
        graphics.setColor(Color.green);
        graphics.setFont(newFont);
        //绘制绿色的验证码，并使其居中
        graphics.drawString(string, (width - fontMetrics.stringWidth(string)) / 2, (height - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent());
        graphics.setColor(Color.blue);
        Random random = new Random();
        for (int i = 0; i < 20; ++i) {  //绘制20条干扰线，其颜色为蓝
            int x1 = random.nextInt(width), y1 = random.nextInt(height);
            int x2 = random.nextInt(width), y2 = random.nextInt(height);
            graphics.drawLine(x1, y1, x2, y2);
        }
        return image;
    }
}