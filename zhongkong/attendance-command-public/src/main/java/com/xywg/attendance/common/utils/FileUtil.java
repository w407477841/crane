package com.xywg.attendance.common.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.io.*;
import java.net.SocketException;

/**
 * 文件读取工具类
 */
public class FileUtil {
	private static Logger log = LoggerFactory.getLogger(FileUtil.class);
	/**
	 * 读取文件内容，作为字符串返回
	 */
	public static String readFileAsString(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException(filePath);
		}

		if (file.length() > 1024 * 1024 * 1024) {
			throw new IOException("File is too large");
		}

		StringBuilder sb = new StringBuilder((int) (file.length()));
		// 创建字节输入流
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
			// 创建一个长度为10240的Buffer
			byte[] bbuf = new byte[10240];
			// 用于保存实际读取的字节数
			int hasRead = 0;
			while ((hasRead = fis.read(bbuf)) > 0) {
				sb.append(new String(bbuf, 0, hasRead));
			}
		} catch(Exception e) {
			log.error(e.getMessage(),e);
		} finally {
			if(fis!= null) {
				fis.close();
				fis = null;
			}
		}
		return sb.toString();
	}

	/**
	 * 根据文件路径读取byte[] 数组
	 */
	public static byte[] readFileByBytes(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException(filePath);
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(
					(int) file.length());
			BufferedInputStream in = null;

			try {
				in = new BufferedInputStream(new FileInputStream(file));
				short bufSize = 1024;
				byte[] buffer = new byte[bufSize];
				int len1;
				while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
					bos.write(buffer, 0, len1);
				}

				byte[] var7 = bos.toByteArray();
				return var7;
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException var14) {
					log.error(var14.getMessage());
				}

				bos.close();
			}
		}
	}

	/**
	 * 图片读取ftp
	 * 
	 * @param path
	 * @return byte[]
	 * @author duanfen
	 * @throws IOException 
	 */
	public static byte[] getFile(String path) throws IOException {
		Environment environment = ApplicationContextProvider.getApplicationContext().getEnvironment();
		FTPClient ftp = null;
		try {
			ftp = FTPClientUtil.connectFtp(environment.getProperty("ftp.host"), Integer.parseInt(environment.getProperty("ftp.port")),
					environment.getProperty("ftp.username"), environment.getProperty("ftp.password"));
		} catch (SocketException e) {
			log.error(e.getMessage());
		}
		return  FTPClientUtil.readFile(ftp, path);
	}
}
