/**
 * author: loserStar
 * date: 2019年8月12日下午6:06:55
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.word;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import com.jfinal.plugin.activerecord.Record;
import com.loserstar.utils.file.LoserStarFileUtil;
import com.loserstar.utils.json.LoserStarJsonUtil;

/**
 * 字号‘八号’对应磅值5
字号‘七号’对应磅值5.5
字号‘小六’对应磅值6.5
字号‘六号’对应磅值7.5
字号‘小五’对应磅值9
字号‘五号’对应磅值10.5
字号‘小四’对应磅值12
字号‘四号’对应磅值14
字号‘小三’对应磅值15
字号‘三号’对应磅值16
字号‘小二’对应磅值18
字号‘二号’对应磅值22
字号‘小一’对应磅值24
字号‘一号’对应磅值26
字号‘小初’对应磅值36
 */
/**
 * 
 * author: loserStar
 * date: 2019年9月25日下午5:29:54
 * remarks:增加表第一行内容可以居中的设置
 */
public class LoserStarWordUtil {
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String sourceFilePath = "c://1.docx";
		String targetFilePath = "c://2.docx";
		System.out.println(sourceFilePath);
		//获取文档
		XWPFDocument document = getDocument(sourceFilePath);
		List<XWPFParagraph> paragraphList = document.getParagraphs();
		for (XWPFParagraph xwpfParagraph : paragraphList) {
			List<XWPFRun>  runs = xwpfParagraph.getRuns();
			for (XWPFRun run : runs) {
				System.out.println(run.getFontFamily());
			}
			System.out.println(xwpfParagraph.getText());
		}
//		List<String> textList = readParagraph(document);
//		for (String string : textList) {
//			System.out.println(string);
//		}
//		System.out.println(LoserStarJsonUtil.toJsonDeep(textList));
	}
	/**
	 * 读取文本demo
	 * @param path
	 * @return
	 */
    private static <T> List<String> readWordFileDemo(String path) {
        List<String> contextList = new ArrayList<String>();
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(path));
            if (path.endsWith(".doc")) {
                HWPFDocument document = new HWPFDocument(stream);
                WordExtractor extractor = new WordExtractor(document);
                String[] contextArray = extractor.getParagraphText();
                List<String> contextArrayList = Arrays.asList(contextArray);
                for (String context : contextArrayList) {
					contextList.add(context);
				}
                extractor.close();
            } else if (path.endsWith(".docx")) {
                XWPFDocument document = new XWPFDocument(stream).getXWPFDocument();
                List<XWPFParagraph> paragraphList = document.getParagraphs();
                for (XWPFParagraph paragraph : paragraphList) {
					contextList.add(paragraph.getParagraphText());
				}
                document.close();
            } else {
            	System.out.println("此文件{}不是word文件"+ path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != stream) try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("读取word文件失败");
            }
        }
        return contextList;
    }

    /**
     * 读取表格demo
     * @param path
     * @return
     */
    private static <T> List<String> readWordFileTableDemo(String path) {
        List<String> contextList = new ArrayList<String>();
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(path));
           if (path.endsWith(".docx")) {
                XWPFDocument document = new XWPFDocument(stream).getXWPFDocument();
                Iterator<XWPFTable> it = document.getTablesIterator();//得到word中的表格
				// 设置需要读取的表格  set是设置需要读取的第几个表格，total是文件中表格的总数
				int total = 4;
				int num = 1;
				while(it.hasNext()){
					XWPFTable table = it.next();  
					System.out.println("这是第" + num + "个表的数据");
					List<XWPFTableRow> rows = table.getRows(); 
					//读取每一行数据
					for (int i = 0; i < rows.size(); i++) {
						XWPFTableRow  row = rows.get(i);
						//读取每一列数据
						List<XWPFTableCell> cells = row.getTableCells(); 
						for (int j = 0; j < cells.size(); j++) {
							XWPFTableCell cell = cells.get(j);
							//输出当前的单元格的数据
							System.out.print(cell.getText() + "\t");
						}
						System.out.println();
					}
					
					num++;
				}
                document.close();
            } else {
            	System.out.println("此文件{}不是word文件"+ path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != stream) try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("读取word文件失败");
            }
        }
        return contextList;
    }

    
    public static void write2Docx()throws Exception{
    	String tempPath = "c:\\1.docx";
    	String outFilePath = "c:\\2.docx";
        XWPFDocument document= new XWPFDocument(new FileInputStream(new File(tempPath)));
        
        //读取内容
        List<XWPFParagraph> paragraphList = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphList) {
        	String text = paragraph.getParagraphText();
        	//匹配标记的段落
        	if (text.equals("${preface}")) {
        		//拿到该段落的字符串数组
        		List<XWPFRun> runs = paragraph.getRuns();
        		//删除该段落内原来的内容
        		for (int i = 0; i < runs.size(); i++) {
					paragraph.removeRun(i);
				}
        		//添加新段落
        		XWPFRun prefaceRun = paragraph.insertNewRun(0);
        		prefaceRun.setText("本标准按照GB 1.1《标准化工作导则 第1部分：标准的结构和编写》给出的规则起草。\r\n" + 
        				"本标准附录A、附录B、附录C和附录D为规范性附录。\r\n" + 
        				"本标准由红塔烟草（集团）有限责任公司提出。\r\n" + 
        				"本标准由红塔烟草（集团）有限责任公司的宣传策划部归口。\r\n" + 
        				"本标准起草部门：宣传策划部。\r\n" + 
        				"本标准主要起草人： 杨启成。\r\n" + 
        				"签发人：王勇。\r\n" + 
        				"");
        		prefaceRun.setColor("CA0C15");
        		prefaceRun.setFontFamily("宋体");
			}
		}
        //Write the Document in file system
        FileOutputStream out = new FileOutputStream(new File(outFilePath));

        //添加标题
        XWPFParagraph titleParagraph = document.createParagraph();
        //设置段落居中
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun titleParagraphRun = titleParagraph.createRun();

        titleParagraphRun.setText("Java PoI");
        titleParagraphRun.setColor("000000");
        titleParagraphRun.setFontSize(20);

        //段落
        XWPFParagraph firstParagraph = document.createParagraph();
        XWPFRun run = firstParagraph.createRun();
        run.setText("Java POI 生成word文件。");
        run.setColor("696969");
        run.setFontSize(16);

        //设置段落背景颜色
        CTShd cTShd = run.getCTR().addNewRPr().addNewShd();
        cTShd.setVal(STShd.CLEAR);
        cTShd.setFill("97FFFF");

        //换行
        XWPFParagraph paragraph1 = document.createParagraph();
        XWPFRun paragraphRun1 = paragraph1.createRun();
        paragraphRun1.setText("\r");
        XWPFParagraph paragraph2 = document.createParagraph();
        XWPFRun paragraphRun2 = paragraph2.createRun();
        paragraphRun2.setText("\r");

        //基本信息表格
        XWPFTable infoTable = document.createTable();
        //去表格边框
        infoTable.getCTTbl().getTblPr().unsetTblBorders();
        //列宽自动分割
        CTTblWidth infoTableWidth = infoTable.getCTTbl().addNewTblPr().addNewTblW();
        infoTableWidth.setType(STTblWidth.DXA);
        infoTableWidth.setW(BigInteger.valueOf(9072));

        //表格第一行
        XWPFTableRow infoTableRowOne = infoTable.getRow(0);
        infoTableRowOne.getCell(0).setText("职位");
        infoTableRowOne.addNewTableCell().setText(": Java 开发工程师");

        //表格第二行
        XWPFTableRow infoTableRowTwo = infoTable.createRow();
        infoTableRowTwo.getCell(0).setText("姓名");
        infoTableRowTwo.getCell(1).setText(": seawater");

        //表格第三行
        XWPFTableRow infoTableRowThree = infoTable.createRow();
        infoTableRowThree.getCell(0).setText("生日");
        infoTableRowThree.getCell(1).setText(": xxx-xx-xx");

        //表格第四行
        XWPFTableRow infoTableRowFour = infoTable.createRow();
        infoTableRowFour.getCell(0).setText("性别");
        infoTableRowFour.getCell(1).setText(": 男");

        //表格第五行
        XWPFTableRow infoTableRowFive = infoTable.createRow();
        infoTableRowFive.getCell(0).setText("现居地");
        infoTableRowFive.getCell(1).setText(": xx");
        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(document, sectPr);

        //添加页眉
        CTP ctpHeader = CTP.Factory.newInstance();
        CTR ctrHeader = ctpHeader.addNewR();
        CTText ctHeader = ctrHeader.addNewT();
        String headerText = "ctpHeader";
        ctHeader.setStringValue(headerText);
        XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeader, document);
        //设置为右对齐
        headerParagraph.setAlignment(ParagraphAlignment.RIGHT);
        XWPFParagraph[] parsHeader = new XWPFParagraph[1];
        parsHeader[0] = headerParagraph;
        policy.createHeader(XWPFHeaderFooterPolicy.DEFAULT, parsHeader);

        //添加页脚
        CTP ctpFooter = CTP.Factory.newInstance();
        CTR ctrFooter = ctpFooter.addNewR();
        CTText ctFooter = ctrFooter.addNewT();
        String footerText = "ctpFooter";
        ctFooter.setStringValue(footerText);
        XWPFParagraph footerParagraph = new XWPFParagraph(ctpFooter, document);
        headerParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFParagraph[] parsFooter = new XWPFParagraph[1];
        parsFooter[0] = footerParagraph;
        policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT, parsFooter);

        document.write(out);
        out.close();
    }
    
	
	   /**
     * 获取一个现有的Word文档的对象或者生成一个新的Word文档对象
     * @param filePath 文件路径
     * @return
     * @throws Exception
     */
    public static XWPFDocument getDocument(String filePath) throws Exception {
    	if(!LoserStarFileUtil.isFile(filePath)) {
    		throw new Exception(filePath+"不存在，请检查！");
    	}
    	return getDocument(new File(filePath));
    }
    
    
    /**
     * 获取一个现有的Word文档的对象或者生成一个新的Word文档对象
     * @param File 文件对象
     * @return
     * @throws Exception
     */
    public static XWPFDocument getDocument(File file) throws Exception {
			return getDocument(new FileInputStream(file));
    }
    
    /**
     * 获取一个现有的Word文档的对象或者生成一个新的Word文档对象
     * @param inputStream 文件的输入流，如果未null则生成新的文档对象
     * @return
     * @throws Exception
     */
    public static XWPFDocument getDocument(InputStream inputStream) throws Exception {
    	
    	XWPFDocument document= null;
    	if (inputStream==null) {
			document = new XWPFDocument();
		}else {
			document = new XWPFDocument(inputStream);
		}
    	return document;
    }
	
    /**
     * 输出Word文档
     * @param document
     * @param targetFilePath
     * @throws Exception
     */
    public static void writeDocx(XWPFDocument document,String targetFilePath) throws Exception {
    	writeDocx(document, new File(targetFilePath));
    }

    /**
     * 输出Word文档
     * @param document
     * @param targetFile
     * @throws Exception
     */
    public static void writeDocx(XWPFDocument document,File targetFile) throws Exception {
    	writeDocx(document, new FileOutputStream(targetFile));
    }
    

    /**
     * 输出Word文档
     * @param document
     * @param outputStream
     * @throws Exception
     */
    public static void writeDocx(XWPFDocument document,OutputStream outputStream) throws Exception {
    	if (outputStream==null) {
			throw new Exception("outputStream不能为null");
		}
    	BufferedOutputStream bufferedOutPut = new BufferedOutputStream(outputStream);
    	//刷新此缓冲的输出流，保证数据全部都能写出
    	bufferedOutPut.flush();
    	document.write(bufferedOutPut);
    	bufferedOutPut.close();
    }
    
	/**
	 * 读取Word的段落
	 * @param document
	 * @return
	 * @throws IOException
	 */
	public static List<String> readParagraph(XWPFDocument document) throws IOException{
		List<String> textList = new ArrayList<String>();
		List<XWPFParagraph> paragraphList = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphList) {
        	textList.add(paragraph.getParagraphText());
			}
        document.close();
		return textList;
	}
	
	public static List<List<List<String>>> readTable(XWPFDocument document) throws IOException{
		List<List<List<String>>> tableList = new ArrayList<List<List<String>>>(); 
         Iterator<XWPFTable> it = document.getTablesIterator();//得到word中的表格
			// 设置需要读取的表格  set是设置需要读取的第几个表格，total是文件中表格的总数
			while(it.hasNext()){
				XWPFTable table = it.next();  
				List<List<String>> tempTable = new ArrayList<List<String>>();
				List<XWPFTableRow> rows = table.getRows(); 
				//读取每一行数据
				for (int i = 0; i < rows.size(); i++) {
					XWPFTableRow  row = rows.get(i);
					List<String> tempRow = new ArrayList<String>();
					//读取每一列数据
					List<XWPFTableCell> cells = row.getTableCells(); 
					for (int j = 0; j < cells.size(); j++) {
						//获取单元格
						XWPFTableCell cell = cells.get(j);
						String tempCell = (cell.getText()==null||cell.getText().equals("null"))?"":cell.getText();
						tempRow.add(tempCell);
					}
					tempTable.add(tempRow);
				}
				tableList.add(tempTable);
			}
         document.close();
		return tableList;
	}
	



    
 
    
    /**
     * /**
     * 向Word的某个标记的地方替换为文本
     * @param document 文档对象
     * @param oldText 标记
     * @param newTextList 标记地方替换的段落内容
     * @param fontSize 所替换的字号大小
     * @param paragraphAlignment 对齐方式
     * @param fontFamily 字体
     * @return 处理好的文档对象
     */
    public static XWPFDocument processDocumentReplaceTextWithText(XWPFDocument document,String oldText,List<String> newTextList,int fontSize,ParagraphAlignment paragraphAlignment,String fontFamily) {
    	//读取内容
        List<XWPFParagraph> paragraphList = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphList) {
        	String text = paragraph.getParagraphText();
        	String trimText = text.trim();
        	System.out.println(trimText);
        	//匹配标记的段落
        	if (trimText.contains(oldText)) {
        		//删除该段落内原来的内容
        		while (paragraph.getRuns().size()!=0) {
					paragraph.removeRun(0);
				}
        		paragraph.setFirstLineIndent((Integer) getWidthFromCentimeter(1.3));//首行缩进：567==1厘米
        		if (paragraphAlignment!=null) {
        			paragraph.setAlignment(paragraphAlignment);//字体对齐方式：1左对齐 2居中3右对齐
				}
        		//添加新段落
        		for (int i = 0; i < newTextList.size(); i++) {
        			XWPFRun prefaceRun = paragraph.insertNewRun(i);
        			prefaceRun.setText(newTextList.get(i));
        			//字号大小
        			if (fontSize!=0) {
        				prefaceRun.setFontSize(fontSize);
					}
        			//字体
        			if (fontFamily!=null) {
        				prefaceRun.setFontFamily(fontFamily);
					}
        		}
			}
		}
        return document;
    }
    
    /**
     * /**
     * 向Word的某个标记的地方替换为段落
     * @param document 文档对象
     * @param oldText 标记
     * @param newTextList 标记地方替换的段落内容
     * @param fontSize 所替换的字号大小
     * @param paragraphAlignment 对齐方式
     * @param fontFamily 字体
     * @return 处理好的文档对象
     */
    public static XWPFDocument processDocumentReplaceTextWithParagraph(XWPFDocument document,String oldText,List<String> newTextList,int fontSize,ParagraphAlignment paragraphAlignment,String fontFamily) {
    	//读取内容
        List<XWPFParagraph> paragraphList = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphList) {
        	String text = paragraph.getParagraphText();
        	String trimText = text.trim();
        	System.out.println(trimText);
        	//匹配标记的段落
        	if (trimText.contains(oldText)) {
        		//删除该段落内原来的内容
        		while (paragraph.getRuns().size()!=0) {
					paragraph.removeRun(0);
				}
        		for (int i = 0; i < newTextList.size(); i++) {
        			//创建一个新段落
        			XWPFParagraph newParagraph =  document.insertNewParagraph(paragraph.getCTP().newCursor());
        			newParagraph.setFirstLineIndent((Integer) getWidthFromCentimeter(1.3));//首行缩进：567==1厘米
        			if (paragraphAlignment!=null) {
        				newParagraph.setAlignment(paragraphAlignment);//字体对齐方式：1左对齐 2居中3右对齐
					}
        			//运行段落
        			XWPFRun run=newParagraph.createRun();
        			run.setText(newTextList.get(i));
        			
        			//字号大小
        			if (fontSize!=0) {
        				run.setFontSize(fontSize);
					}
        			//字体
        			if (fontFamily!=null) {
						run.setFontFamily(fontFamily);
					}
        			
        		}
        		break;
			}
		}
        return document;
    }
    
    /**
     * 向Word的某个标记的地方替换为表格
     * @param document 文档对象
     * @param oldText 标记
     * @param newTextList 标记替换的表格的内容（二维数组）
     * @param cellWidthList 每一列的宽度（厘米）
     * @param rowMergeRangeList 要合并的行的范围对象
     * @param colMergeRangeList 要合并的列的范围对象
     * @param isFirstRowCenter首行是否水平居中
     * @return 处理好的文档对象
     * @throws Exception
     */
    public static XWPFDocument processDocumentReplaceTextWithTable(XWPFDocument document,String oldText,List<List<String>> newTextList,List<Double> cellWidthList,List<MergeRange> rowMergeRangeList,List<MergeRange> colMergeRangeList,boolean isFirstRowCenter) throws Exception {
    	
    	//读取内容
        List<XWPFParagraph> paragraphList = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphList) {
        	String text = paragraph.getParagraphText();
        	//匹配标记的段落
        	if (text.equals(oldText)) {
        		//删除该段落内原来的内容
        		while (paragraph.getRuns().size()!=0) {
					paragraph.removeRun(0);
				}
        		//基本信息表格
                XWPFTable infoTable = document.insertNewTbl(paragraph.getCTP().newCursor());
                //设置总体宽度
//                CTTblWidth infoTableWidth = infoTable.getCTTbl().addNewTblPr().addNewTblW();
//                infoTableWidth.setType(STTblWidth.DXA);
//                infoTableWidth.setW(BigInteger.valueOf(getWidthFromCentimeter(18d)));

                
                for (int i = 0; i < newTextList.size(); i++) {
                	List<String> row = newTextList.get(i);
                	XWPFTableRow infoTableRow = null;
                	if(i==0) {
                		//第一行直接取，以后的创建
                		infoTableRow = infoTable.getRow(0);
                	}else {
                		infoTableRow = infoTable.createRow();
                	}
                	//遍历列
					for (int j = 0; j < row.size(); j++) {
						XWPFTableCell cell = null;
						//如果是第一行第一列则直接取,如果是第一行而不是第一列则创建
						if (0==i) {
							if(0==j) {
								cell = infoTableRow.getCell(0);
							}else {
								cell = infoTableRow.addNewTableCell();
							}
							if (isFirstRowCenter) {
							CTTc cttc = cell.getCTTc();
							CTTcPr ctPr = cttc.addNewTcPr();
							ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
							cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
							}
						}else {
							//如果不是第一行,就直接取列
							cell = infoTableRow.getCell(j);
						}
						cell.setText(row.get(j));
						//设置每一列宽度
						if(cell.getCTTc().getTcPr()==null) {
							cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(getWidthFromCentimeter(cellWidthList.get(j))));
						}
					}
                }
                
                
                //合并行
                if (rowMergeRangeList!=null) {
                	for (MergeRange rowRange : rowMergeRangeList) {
                		if (rowRange.startColIndex!=rowRange.endColIndex) {
							throw new Exception("合并行时，必须列的索引一致！");
						}
                		mergeCellsVertically(infoTable, rowRange.startColIndex, rowRange.startRowIndex, rowRange.endRowIndex);
					}
				}
                //合并列
                if (colMergeRangeList!=null) {
                	for (MergeRange colRange : colMergeRangeList) {
                		if (colRange.startRowIndex!=colRange.endRowIndex) {
							throw new Exception("合并列时，必须行的索引一致！");
						}
                		mergeCellsHorizontal(infoTable, colRange.startRowIndex, colRange.startColIndex, colRange.endColIndex);
                	}
                }
                
                break;
        	}
        }
        
        return document;
    }
    
	
	/**
	 * 合并单元格的范围类
	 */
	public static class MergeRange{
		private int startRowIndex = 0;
		private int endRowIndex = 0;
		private int startColIndex = 0;
		private int endColIndex = 0;
		
		/**
		 * 创建一个范围对象
		 * @param startRowIndex 开始的行索引
		 * @param endRowIndex 结束的行索引
		 * @param startColIndex 开始的列索引
		 * @param endColIndex 结束的列索引
		 * @return
		 */
		public static MergeRange createInstance(int startRowIndex,int endRowIndex,int startColIndex,int endColIndex) {
			MergeRange mergeRange = new MergeRange();
			mergeRange.startRowIndex = startRowIndex;
			mergeRange.endRowIndex = endRowIndex;
			mergeRange.startColIndex = startColIndex;
			mergeRange.endColIndex = endColIndex;
			return mergeRange;
		}
	}
	
    /**
     * 通过一个每组有多少数量的一个数据集，构建一个合并哪些行的一个范围对象
     * @param groupRecordList 每组的数量有多少个的数据集
     * @param colArray 要合并的列的下标数组
     * @param startRowIndex 开始合并的起始行
     * @return
     */
    public static List<MergeRange> buildRowMergeRange(List<Record> groupRecordList,String countFieldName,int[] colArray,int startRowIndex){
    	List<MergeRange> rangeList = new ArrayList<MergeRange>();
    	int preMergEndIndex = startRowIndex-1;//上一个的结束行索引（本来应该是-1，但是因为有个表头，所以跳过表头）
		for (Record record : groupRecordList) {
			int currentStartRowIndex = preMergEndIndex+1;//开始行索引=上一个的索引+1
			int currentEndRowIndex = preMergEndIndex+record.getInt(countFieldName);//结束索引= 上一次结束的索引+这一次的数量
			//把当前加到上一次
			preMergEndIndex = currentEndRowIndex;
			for (int i = 0; i < colArray.length; i++) {
				rangeList.add(MergeRange.createInstance(currentStartRowIndex, currentEndRowIndex, colArray[i], colArray[i]));
			}
		}
		return rangeList;
    }
	
	/**
	 * 通过厘米，计算出Word中的宽度的单位的值（目前不知道是什么单位，但是知道1厘米=567）
	 * @param centimeter
	 * @return
	 */
	private static int getWidthFromCentimeter(Double centimeter) {
		int width = (int)(centimeter*567);
		return width;
	}
	
	
	/**
	 * 跨列合并单元格  
	 * @param table 表对象
	 * @param row 行的索引
	 * @param fromCell 开始列的索引
	 * @param toCell 结束列的索引
	 */
    private  static void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {    
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {    
            XWPFTableCell cell = table.getRow(row).getCell(cellIndex);    
            if ( cellIndex == fromCell ) {    
                // The first merged cell is set with RESTART merge value    
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);    
            } else {    
                // Cells which join (merge) the first one, are set with CONTINUE    
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);    
            }    
        }    
    }    
    /**
     * 跨行合并单元格  
     * @param table表对象
     * @param col 列索引
     * @param fromRow 开始行的索引
     * @param toRow 开始列的索引
     */
    private static void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {    
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {    
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);    
            if ( rowIndex == fromRow ) {    
                // The first merged cell is set with RESTART merge value    
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);    
            } else {    
                // Cells which join (merge) the first one, are set with CONTINUE    
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);    
            }    
        }    
    }   
    



	

}
