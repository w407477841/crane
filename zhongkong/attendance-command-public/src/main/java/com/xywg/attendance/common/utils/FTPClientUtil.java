package com.xywg.attendance.common.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

public class FTPClientUtil {

	/**
	 * @Title: downloadFile
	 * @Description: 下载图片
	 * @author CHENCHEN
	 * @param @param response
	 * @param @param ftpClient
	 * @param @param remotePath
	 * @param @throws IOException
	 * @return void
	 * @date 2018年1月16日
	 * @throws
	 */
	public static void downloadFile(OutputStream outputStream,
			FTPClient ftpClient, String remotePath) throws IOException {
		// response.setContentType("image/jpeg");
		ftpClient.setBufferSize(1024);
		// 设置文件类型（二进制）
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		ftpClient.retrieveFile(remotePath, outputStream);
	}

	/**
	 * @Title: uploadFile
	 * @Description: 上传文件
	 * @author CHENCHEN
	 * @param @param directory
	 * @param @param inputStream
	 * @param @param fileName
	 * @param @param ftpClient
	 * @param @throws IOException
	 * @return void
	 * @date 2018年1月16日
	 * @throws
	 */
	public static void uploadFile(String directory, InputStream inputStream,
			String fileName, FTPClient ftpClient) throws IOException {
		ftpClient.makeDirectory(directory);
		ftpClient.changeWorkingDirectory(directory);
		ftpClient.storeFile(fileName, inputStream);
	}

	/***
	 * 图片返回byte
	 * 
	 * @param ftp
	 * @param path
	 * @return
	 * @throws IOException
	 *             byte[]
	 * @author duanfen
	 */
	public static byte[] readFile(FTPClient ftp, String path) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		FTPClientUtil.downloadFile(bos, ftp, path);
		return bos.toByteArray();
	}

	/**
	 * @Title: connectFtp
	 * @Description: 连接ftp
	 * @author CHENCHEN
	 * @param @param host
	 * @param @param port
	 * @param @param username
	 * @param @param password
	 * @param @return
	 * @param @throws SocketException
	 * @param @throws IOException
	 * @return FTPClient
	 * @date 2018年1月16日
	 * @throws
	 */
	public static FTPClient connectFtp(String host, int port, String username,
			String password) throws SocketException, IOException {
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(host, port);
		// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
		ftpClient.login(username, password);// 登录
		int reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftpClient.disconnect();
		}
		ftpClient.setBufferSize(1024);
		// 设置文件类型（二进制）
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		ftpClient.enterLocalPassiveMode();
		return ftpClient;
	}

	/**
	 * 默认连接ftp
	 * @return
	 */
	public static FTPClient connectFtp()throws SocketException, IOException {
		return connectFtp(ApplicationContextProvider.getApplicationContext().getEnvironment().getProperty("ftp.host"),
				Integer.parseInt(ApplicationContextProvider.getApplicationContext().getEnvironment().getProperty("ftp.port")),
				ApplicationContextProvider.getApplicationContext().getEnvironment().getProperty("ftp.username"),
				ApplicationContextProvider.getApplicationContext().getEnvironment().getProperty("ftp.password"));
	}

	/**
	 * @Title: disconnect
	 * @Description: 断开ftp连接
	 * @author CHENCHEN
	 * @param @param ftpClient
	 * @param @throws IOException
	 * @return void
	 * @date 2018年1月16日
	 * @throws
	 */
	public static void disconnect(FTPClient ftpClient) throws IOException {
		if (ftpClient != null && ftpClient.isConnected()) {
			ftpClient.disconnect();
		}
	}

//	public static void main(String[] args) {
//		FTPClient ftpClient = new FTPClient();
//		try {
//			int reply;
//			ftpClient.connect("192.168.1.124", 21);
//			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
//			ftpClient.login("alice", "P@ssw0rd");// 登录
//			reply = ftpClient.getReplyCode();
//			if (!FTPReply.isPositiveCompletion(reply)) {
//				ftpClient.disconnect();
//			}
//			// response.setContentType("image/jpeg");
//
//			ftpClient.setBufferSize(1024);
//			// 设置文件类型（二进制）
//			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//			// ftpClient.retrieveFile(remotePath, response.getOutputStream());
//			File file = new File("d://teset.txt");
//			FileOutputStream fos = new FileOutputStream(file);
//			ftpClient.retrieveFile("/upload/picture/timg.jpg", fos);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (ftpClient.isConnected()) {
//				try {
//					ftpClient.disconnect();
//				} catch (IOException ioe) {
//				}
//			}
//		}
//	}
}
