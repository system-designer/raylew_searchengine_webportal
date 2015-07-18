package com.searchengine.webportal.utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Administrator on 2014/11/10.
 */
public class FileHelper {

    private static FileHelper fileHelper = new FileHelper();

    private FileHelper() {
    }

    //根据日期+TaskID生成目录。
    public static String getDir(String taskId) {
        String path = fileHelper.getClass().getClassLoader().getResource("").getPath().replace('/', '\\');
        int end = path.length() - "\\classes\\".length();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String subDir = df.format(new Date());
        path = path.substring(1, end) + "\\upload\\" + subDir + "\\" + taskId + "\\";
        return path;
    }

    private File parseUploadFile(HttpServletRequest request, AtomicReference<String> uploadedFileName) throws Exception {
        String tmpFilePath = getDir(request.getParameter("taskId"));
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            return null;
        }
        File uploadedFile = null;
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            File repository = new File(tmpFilePath);
            factory.setRepository(repository);
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(request);
            Iterator<FileItem> iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = iter.next();
                if (!item.isFormField()) {
                    String fileName = item.getName();
                    uploadedFileName.set(UUID.randomUUID().toString() + fileName);
                    uploadedFile = new File(tmpFilePath + uploadedFileName);
                    item.write(uploadedFile);
                }
            }
        } catch (Exception e) {

        }
        return uploadedFile;
    }

    /**
     * 上传文件保存到硬盘
     * 在网站根目录创建(upload/ [年月日]/[任务Id]/)文件夹
     *
     * @param request
     * @return 文件保存物理路径
     */
    public static String upload(HttpServletRequest request, String taskId) throws Exception {
        boolean result = true;
        String path = getDir(taskId);
        File file = new File(path);
        //如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            result = file.mkdirs();
        }
        if (result) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile imgFile1 = multipartRequest.getFile("file");
            try {
                if (imgFile1 != null) {
                    byte[] b = imgFile1.getBytes();
                    String strExt = getExtention(((CommonsMultipartFile) imgFile1).getFileItem().getName());
                    String strName = Long.toString(System.currentTimeMillis()) + "." + strExt;
                    String fullPath = path + strName;
                    FileOutputStream os = new FileOutputStream(fullPath);
                    os.write(b, 0, b.length);
                    os.close(); // 关闭输出流
                    return fullPath;
                }
            } catch (Exception ex) {
                throw ex;
            }
        }
        return "";
    }

    public static String getExtention(String fileName) {
        if (fileName.equals(null) == false && fileName.length() > 0) {  //--截取文件名
            int i = fileName.lastIndexOf(".");
            if (i > 0 && i < fileName.length()) {
                return fileName.substring(i + 1); //--扩展名
            }
        }
        return "";
    }

    public static boolean deleteFile(String fileName) {
        boolean result = false;
        File file = new File(fileName);
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }

    /**
     * 下载
     *
     * @param request
     * @param response
     * @param downLoadPath
     * @param contentType
     * @param realName
     * @throws Exception
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, String downLoadPath, String contentType, String realName) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        //String ctxPath = request.getSession().getServletContext().getRealPath("/") + "upload/";
        //String ctxPath = getDir();
        //String downLoadPath = ctxPath + storeName;
        long fileLength = new File(downLoadPath).length();
        response.setContentType(contentType);
        response.setHeader("Content-disposition", "attachment; filename=" + new String(realName.getBytes("utf-8"), "ISO8859-1"));
        response.setHeader("Content-Length", String.valueOf(fileLength));
        bis = new BufferedInputStream(new FileInputStream(downLoadPath));
        bos = new BufferedOutputStream(response.getOutputStream());
        byte[] buff = new byte[2048];
        int bytesRead;
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            bos.write(buff, 0, bytesRead);
        }
        bis.close();
        bos.close();
    }
}