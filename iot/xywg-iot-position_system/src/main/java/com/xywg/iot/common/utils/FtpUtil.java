package com.xywg.iot.common.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description Ftp工具类
 * @author caolujiao
 * @date 2018/03/06
 *
 */
public class FtpUtil {



	/**
	 * Description: 从FTP服务器下载文件
	 *
	 * @param outputStream
	 *            outputStream
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
	public static void downloadFile(OutputStream outputStream, String host,
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
			ftpClient.setBufferSize(1024);
			// 设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.retrieveFile(remotePath, outputStream);
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
