package com.xingyun.equipment.admin.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * @Description Ftp工具类
 * @author caolujiao
 * @date 2018/03/06
 *
 */
public class FtpUtil {

	/**
	 * Description: 向FTP服务器上传文件
	 *
	 * @param host
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param basePath
	 *            FTP服务器基础目录
	 * @param filePath
	 *            FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
	 * @param filename
	 *            上传到FTP服务器上的文件名
	 * @param input
	 *            输入流
	 * @return 成功返回true，否则返回false
	 */
	public static boolean uploadFile(String host, int port, String username,
									 String password, String basePath, String filePath, String filename,
									 InputStream input) {
		boolean result = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			// String tempPath = null;
			// 连接FTP服务器
			ftp.connect(host, port);
			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			// 登录
			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return result;
			}
			// 切换到上传目录
			if (!ftp.changeWorkingDirectory(basePath + filePath)) {
				// 如果目录不存在创建目录
				String bf = basePath + filePath;
				String[] dirs = bf.split("/");
				for (String dir : dirs) {
					if (null == dir || "".equals(dir)) {
						continue;
					}
					if (!ftp.changeWorkingDirectory(dir)) {
						if (!ftp.makeDirectory(dir)) {
							return result;
						} else {
							ftp.changeWorkingDirectory(dir);
						}
					}
				}
			}
			// 被动模式
			ftp.enterLocalPassiveMode();
			// 中文支持
			ftp.setControlEncoding("UTF-8");
			// 设置上传文件的类型为二进制类型
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			// 上传文件
			if (!ftp.storeFile(filename, input)) {
				return result;
			}
			input.close();
			ftp.logout();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}

	/**
	 * Description: 从FTP服务器下载文件
	 *
	 * @param response
	 *            response
	 * @param host
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * @return
	 */
	public static void downloadFile(HttpServletResponse response, String host,
									int port, String username, String password, String remotePath) {
		FTPClient ftpClient = new FTPClient();
		try {
			int reply;
			ftpClient.connect(host, port);
			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			// 登录
			ftpClient.login(username, password);
			reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
			}

			// 被动模式
			ftpClient.enterLocalPassiveMode();
			response.setContentType("image/jpeg");

			ftpClient.setBufferSize(1024);
			// 设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.retrieveFile(remotePath, response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
	}
	
	/**
	 * Description: 从FTP服务器下载文件
	 * 
	 * @param response
	 *            response
	 * @param host
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * @return
	 */
	public static void downloadFile(HttpServletResponse response, String host,
			int port, String username, String password, String remotePath,
			String fileName) {
		FTPClient ftpClient = new FTPClient();
		try {
			int reply;
			ftpClient.connect(host, port);
			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			// 登录
			ftpClient.login(username, password);
			reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
			}
			
			// 被动模式
			ftpClient.enterLocalPassiveMode();
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			ftpClient.setBufferSize(1024);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.retrieveFile(remotePath,
					response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
	}
	
	public static String getSaveLocation() {

		StringBuilder sb = new StringBuilder();
		sb.append(getTodayYear()).append("/")
				.append(getTodayMonth()).append("/").append(getTodayDay());
		return sb.toString();
	}
	
	public static String getTodayYear() {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(dt);
	}

	public static String getTodayMonth() {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		return sdf.format(dt);
	}

	public static String getTodayDay() {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		return sdf.format(dt);
	}

}
