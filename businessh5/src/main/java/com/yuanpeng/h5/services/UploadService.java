package com.yuanpeng.h5.services;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanpeng.h5.common.HttpContant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import lombok.extern.slf4j.Slf4j;

/**
 * Created by  on 2017/5/26.
 */
@Slf4j
@Service
public class UploadService {
	/**
	 * 上传图片
	 * <p>
	 * 为CKEDITOR定制的图片上传功能，后续可以扩展上传其他格式的文件
	 * 上传的文件的基础路径为:
	 * ${apache.home}/${project.name}/${project.name}/freemarker/upload/img/${'yyyyMMdd'}/
	 * 每个文件夹下最多500个文件
	 * </p>
	 *
	 * @author mikko
	 */

	@Autowired
	HttpContant httpContant;
	/**
	 * ~~~ 上传文件的保存路径
	 */

	public static String FILE_UPLOAD_DIR;

	/**
	 * ~~~ 上传文件的保存的下一级路径，标示存储类型
	 */
	public static final String FILE_UPLOAD_SUB_IMG_DIR = "/img";
	/**
	 * ~~~ 上传图片保存的位置
	 */
	public static final String FOR_FREEMARKER_LOAD_DIR = "/images";
	/**
	 * ~~~ 每个上传子目录保存的文件的最大数目
	 */
	public static final int MAX_NUM_PER_UPLOAD_SUB_DIR = 500;
	/**
	 * ~~~ 上传文件的最大文件大小
	 */
	public static final long MAX_FILE_SIZE = 1024 * 1024 * 2;
	/**
	 * ~~~ 系统默认建立和使用的以时间字符串作为文件名称的时间格式
	 */
	public static final String DEFAULT_SUB_FOLDER_FORMAT_AUTO = "yyyyMMdd";
	/**
	 * ~~~ 这里扩充一下格式，防止手动建立的不统一
	 */
	public static final String DEFAULT_SUB_FOLDER_FORMAT_NO_AUTO = "yyyy-MM-dd";

	public String uploadPic(MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
		String url;
		// 获取图片原始文件名
		String originalFilename = file.getOriginalFilename();
		// 文件名使用当前时间
		String name = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		String filename = name + originalFilename;
		String savePath = httpContant.uploadLocation;
		return uploadPic(file, filename, savePath, request, response);
	}

	public String uploadPic(MultipartFile file, String filename, String savePath, HttpServletRequest request,
							HttpServletResponse response) {

		// 图片上传的相对路径（因为相对路径放到页面上就可以显示图片）

		File dir = new File(savePath);

		if (!dir.exists()) {
			dir.mkdirs();
		}

		String url = savePath + filename;
		// 上传图片
		try {
			file.transferTo(new File(url));

		} catch (IOException e) {
			log.info("上传图片失败:" + e.getMessage());
			return "上传图片失败";
		}

		return filename;
	}

	public void removeFile(String filePath) {

		File file = new File(filePath);
		file.delete();

	}

	/**
	 * 下载到默认路径
	 * 
	 * @param urlStr
	 * @param fileName
	 * @throws IOException
	 */
	public void downLoadFromUrl(String urlStr, String fileName) throws IOException {
		downLoadFromUrl(urlStr, fileName, httpContant.uploadLocation);
	}

	/**
	 * URL下载文件
	 * 
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	public void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
		if (StringUtils.isBlank(fileName)) {
			String[] rsl = StringUtils.split(urlStr, "/");
			fileName = rsl[rsl.length - 1];
		}
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		//设置超时间为3秒
		conn.setConnectTimeout(3 * 1000);
		//防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		//得到输入流
		InputStream inputStream = conn.getInputStream();

		//获取自己数组
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		//获取自己数组
		byte[] getData = bos.toByteArray();

		//文件保存位置
		File saveDir = new File(savePath);
		if (!saveDir.exists()) {
			saveDir.mkdir();
		}
		File file = new File(saveDir + File.separator + fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		if (fos != null) {
			fos.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}

		log.info("info:" + url + " download success");

	}
}
