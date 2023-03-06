//package com.loserstar.utils.word;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//import com.aspose.words.Document;
//import com.aspose.words.License;
//import com.aspose.words.SaveFormat;
//
///**
// * Word转换为其它格，aspose-Word 15.8.0测试可用，但效果不太好
// * @author loserStar
// *
// */
//public class LoserStarWordConvertUtils {
//    // 读取license.xml的内容
//    public static boolean getLicense() throws FileNotFoundException, Exception {
//        boolean result = true;
//      License aposeLic = new License();
////      String path = JFinalConfig.class.getResource("/").getPath()+"aposeLicense.xml";
////      InputStream is =JFinalConfig.class.getClass().getClassLoader().getResourceAsStream("aposeLicense.xml");
//      String exampleString = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n"
//      		+ "<License>\r\n"
//      		+ "    <Data>\r\n"
//      		+ "        <Products>\r\n"
//      		+ "            <Product>Aspose.Total for Java</Product>\r\n"
//      		+ "            <Product>Aspose.Words for Java</Product>\r\n"
//      		+ "        </Products>\r\n"
//      		+ "        <EditionType>Enterprise</EditionType>\r\n"
//      		+ "        <SubscriptionExpiry>20991231</SubscriptionExpiry>\r\n"
//      		+ "        <LicenseExpiry>20991231</LicenseExpiry>\r\n"
//      		+ "        <SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>\r\n"
//      		+ "    </Data>\r\n"
//      		+ "    <Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>\r\n"
//      		+ "</License>\r\n"
//      		+ "";
//      ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(exampleString.getBytes());
////      String licenceString = LoserStarFileUtil.ReadReaderByFilePath(path);
////      System.out.println(licenceString);
////      aposeLic.setLicense(new FileInputStream(new File(path)));
//      aposeLic.setLicense(tInputStringStream);
//      return result;
//    }
//    /**
//     * 
//     * @param inPath 源文件路径 .docx文件
//     * @param outPath 生成的文件 .pdf文件
//     * @return
//     */
//    public static void doc2pdf(String inPath, String outPath) {
//    	try {
//    		if (!getLicense()) {
//    		}
//    		Document doc = new Document(inPath);
//    		doc.save(new FileOutputStream(new File(outPath)), SaveFormat.PDF);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    }
//    
//    public static void doc2pdf(InputStream inputStream,OutputStream outputStream) {
//    	try {
//    		if (!getLicense()) {
//    		}
//    		Document doc = new Document(inputStream);
//    		doc.save(outputStream, SaveFormat.PDF);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    }
//    // main 方法测试工具类
//    public static void main(String[] args) {
//        //调用授权方法 源文件路径
//        String sourceFile = "D:\\opt\\attachments\\raw\\01002587\\20220920\\202209201507449987.docx";
//        // 生成的路径
//        String targetFile = "D:\\1.pdf";
//        // 调用方法
//        doc2pdf(sourceFile,targetFile);
////        String path = JFinalConfig.class.getResource("/").getPath()+"aposeLicense.xml";
////        String licence = LoserStarFileUtil.ReadReaderByFilePath(path);
////        System.out.println(licence);
//    }
//}
