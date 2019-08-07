/**
 * author: loserStar
 * date: 2018年4月25日下午6:22:05
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.loserstar.utils.file.LoserStarFileUtil;
import com.loserstar.utils.proerties.LoserStarPropertiesUtil;

/**
 * author: loserStar
 * date: 2018年4月25日下午6:22:05
 * remarks:httpServlet工具类
 */
public class LoserStarServleFileUtil{
	  // 上传文件存储目录
    private static final String UPLOAD_DIRECTORY = "upload";
 
    // 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
	

	/**
	 * 上传文件，返回新的文件名
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws FileUploadException 
	 */
    public static String uploadFile(HttpServletRequest request,HttpServletResponse response) throws IOException, FileUploadException {
		// 检测是否为多媒体上传
        if (!ServletFileUpload.isMultipartContent(request)) {
            // 如果不是则停止
            PrintWriter writer = response.getWriter();
            writer.println("Error: 表单必须包含 enctype=multipart/form-data");
            writer.flush();
            return "";
        }
     // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        /**
         * Apache文件上传组件在解析上传数据中的每个字段内容时，需要临时保存解析出的数据，以便在后面进行数据的进一步处理（保存在磁盘特定位置或插入数据库）。因为Java虚拟机默认可以使用的内存空间是有限的，超出限制时将会抛出“java.lang.OutOfMemoryError”错误。如果上传的文件很大，例如800M的文件，在内存中将无法临时保存该文件内容，Apache文件上传组件转而采用临时文件来保存这些数据；但如果上传的文件很小，例如600个字节的文件，显然将其直接保存在内存中性能会更加好些。

        setSizeThreshold方法用于设置是否将上传文件已临时文件的形式保存在磁盘的临界值（以字节为单位的int值），如果从没有调用该方法设置此临界值，将会采用系统默认值10KB。对应的getSizeThreshold() 方法用来获取此临界值。
         */
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> formItems = upload.parseRequest(request);
		 if (formItems != null && formItems.size() > 0) {
             // 迭代表单数据
             for (FileItem item : formItems) {
                 // 处理不在表单中的字段
                 if (!item.isFormField()) {
//                	 ServletContext servletContext = request.getServletContext();//servlet3.0 jdk1.7
                	 ServletContext servletContext = request.getSession().getServletContext();//servlet2.5 jdk1.6
            		 String uploadDir = LoserStarPropertiesUtil.getProperties(servletContext.getRealPath("配置文件路径")).getProperty("kaen.uploaddir");//获取文件上传路径
            		 uploadFile(uploadDir,item);
                     request.setAttribute("message",
                         "文件上传成功!");
                 }
             }
         }
		 return "";
	}
	
	/**
	 * 上传文件到某个绝对路径
	 * @param uploadDir 系统的绝对路径
	 * @param file 文件对象
	 * @return
	 * @throws IOException
	 */
    public static String uploadFile(String uploadDir,FileItem file) throws IOException {
		String fileRealName = file.getName();                   //获得原始文件名
		LoserStarFileUtil.createDir(uploadDir);//创建上传路径
		String newFileName = LoserStarFileUtil.generateUUIDFileName(fileRealName);//生成新文件名
		LoserStarFileUtil.WriteInputStreamToFilePath(file.getInputStream(),uploadDir+newFileName, false);//输出文件
		return newFileName;
	}
	/**
	 * 下载配置文件配置的路径下的某个文件
	 * @param fileUrl 文件的uuid文件名（带后缀）
	 * @param downName 下载时的名称
	 * @throws Exception 
	 */
    public static void downloadPropertiesDirFile(String fileUrl,String downName,HttpServletRequest request,HttpServletResponse response) throws Exception {
//   	 ServletContext servletContext = request.getServletContext();//servlet3.0 jdk1.7
   	 ServletContext servletContext = request.getSession().getServletContext();//servlet2.5 jdk1.6
		String uploadDir = LoserStarPropertiesUtil.getProperties(servletContext.getRealPath("配置文件路径")).getProperty("kaen.uploaddir");//获取文件上传路径
		String downloadFilePath = uploadDir+fileUrl;
		downloadFile(downloadFilePath,downName,response);
	}
	
	/**
	 * 下载文件，指定一个文件的绝对路径以及下载时显示的文件名
	 * @param downloadFilePath 文件的绝对路径
	 * @param downName 下载时的名称,为null时默认取真实的该文件名称
	 * @throws Exception
	 */
    public static void downloadFile(String downloadFilePath,String downName,HttpServletResponse response) throws Exception {
		InputStream inputStream;
		OutputStream outputStream ;
		try {
			File file = new File(downloadFilePath);
			if (downName==null||downName.equals("")) {
				downName = file.getName();
			}
			inputStream = new FileInputStream(file);
			outputStream = response.getOutputStream();
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(java.net.URLEncoder.encode(downName, "UTF-8")));
			response.addHeader("Content-Length", "" + file.length());
			response.setContentType("application/octet-stream");
			LoserStarFileUtil.WriteInputStreamToOutputStream(inputStream, outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
