package com.loserstar.utils.codeCounter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CodeCounter {
	
	static long normalLines = 0;//有效行
	static long commentLines = 0;//注释行
	static long whiteLines = 0;//空白行
	
	public static void main(String[] args) {
		File f = new File("E://ynzl/ynzl2/common/src/main/java/cn/b2m/eucp/sdkhttp");
		File[] codeFiles = f.listFiles();
		for(File child : codeFiles){
			if(child.getName().matches(".*\\.java$")) {
				parse(child);
			}
		}
		
		System.out.println("normalLines:" + normalLines);
		System.out.println("commentLines:" + commentLines);
		System.out.println("whiteLines:" + whiteLines);
		
	}

	private static void parse(File f) {
		BufferedReader br = null;
		boolean comment = false;
		try {
			br = new BufferedReader(new FileReader(f));
			String line = "";
			while((line = br.readLine()) != null) {
				line = line.trim();
				if(line.matches("^[\\s&&[^\\n]]*$")) {
					whiteLines ++;
				} else if (line.startsWith("/*") && !line.endsWith("*/")) {
					commentLines ++;
					comment = true;	
				} else if (line.startsWith("/*") && line.endsWith("*/")) {
					commentLines ++;
				} else if (true == comment) {
					commentLines ++;
					if(line.endsWith("*/")) {
						comment = false;
					}
				} else if (line.startsWith("//")) {
					commentLines ++;
				} else {
					normalLines ++;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
					br = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
