package com.xywg.equipment.monitor.iot.core.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.SocketException;

/**
 * @Description Ftp工具类
 * @author hjy
 * @date 2018/03/06
 *
 */
@SuppressWarnings("all")
@Service
public class FtpUtils {

	// ftp服务器地址
	@Value("${ftp.host}")
	public String hostname ;
	// ftp服务器端口号默认为21
	@Value("${ftp.port}")
	public Integer port ;
	// ftp登录账号
	@Value("${ftp.username}")
	public String username;
	// ftp登录密码
	@Value("${ftp.password}")
	public String password ;

	public FTPClient ftpClient = null;
	private static final int BUFFER_SIZE = 1024000;

	/**
	 * 65 初始化ftp服务器
	 */
	public void initFtpClient() {
		ftpClient = new FTPClient();
		ftpClient.setControlEncoding("utf-8");
		try {
			System.out.println("connecting...ftp服务器:" + this.hostname + ":" + this.port);
			// 连接ftp服务器
			ftpClient.connect(hostname, port);
			// 登录ftp服务器
			ftpClient.login(username, password);
			// 是否成功登录服务器
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("connect failed...ftp服务器:" + this.hostname + ":" + this.port);
			}
			System.out.println("connect successfu...ftp服务器:" + this.hostname + ":" + this.port);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 上传文件
	 *
	 * @param pathname       ftp服务保存地址
	 * @param fileName       上传到ftp的文件名
	 * @param file 待上传文件的名称（绝对地址） *
	 * @return
	 */
	public boolean uploadFile(String pathname, String fileName, MultipartFile file) {
		boolean flag = false;
		InputStream inputStream = null;
		try {
			System.out.println("开始上传文件");
			// inputStream = new FileInputStream(new File(originfilename));
			inputStream = file.getInputStream();
			initFtpClient();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			CreateDirecroty(pathname);
			ftpClient.makeDirectory(pathname);
			ftpClient.changeWorkingDirectory(pathname);
			ftpClient.storeFile(fileName, inputStream);
			inputStream.close();
			ftpClient.logout();
			flag = true;
			System.out.println("上传文件成功");
		} catch (Exception e) {
			System.out.println("上传文件失败");
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * 上传文件
	 *
	 * @param pathname       ftp服务保存地址
	 * @param fileName       上传到ftp的文件名
	 * @param originfilename 待上传文件的名称（绝对地址） *
	 * @return
	 */
	public boolean uploadFile(String pathname, String fileName, String originfilename) {
		boolean flag = false;
		InputStream inputStream = null;
		try {
			System.out.println("开始上传文件");
			inputStream = new FileInputStream(new File(originfilename));
			initFtpClient();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			CreateDirecroty(pathname);
			ftpClient.makeDirectory(pathname);
			ftpClient.changeWorkingDirectory(pathname);
			ftpClient.storeFile(fileName, inputStream);
			inputStream.close();
			ftpClient.logout();
			flag = true;
			System.out.println("上传文件成功");
		} catch (Exception e) {
			System.out.println("上传文件失败");
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * 上传文件
	 *
	 * @param pathname    ftp服务保存地址
	 * @param fileName    上传到ftp的文件名
	 * @param inputStream 输入文件流
	 * @return
	 */
	public boolean uploadFile(String pathname, String fileName, InputStream inputStream,
							  Long beginSize) {
		boolean flag = false;
		if(beginSize==null) {
			beginSize=0L;
		}
		try {
			System.out.println("开始上传文件");
			initFtpClient();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.setBufferSize(BUFFER_SIZE);
			CreateDirecroty(pathname);
			ftpClient.makeDirectory(pathname);
			ftpClient.changeWorkingDirectory(pathname);
			//等待写入的文件
			FileInputStream fis = (FileInputStream)inputStream;
			OutputStream out = ftpClient.appendFileStream(new String(fileName.getBytes("utf-8"),"iso-8859-1"));
			//把文件指针移动到 开始位置
			ftpClient.setRestartOffset(beginSize);
			//定义最小移动单位是 1024字节 也就是1kb
			byte[] bytes = new byte[1024];
			int c;
			while ((c = fis.read(bytes)) != -1) {
				out.write(bytes, 0, c);
			}
			out.flush();
			out.close();
			fis.close();
			System.out.println("上传文件成功");
			flag=true;
			ftpClient.logout();
		} catch (Exception e) {
			System.out.println("出现异常");
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
	// 改变目录路径
	public boolean changeWorkingDirectory(String directory) {
		boolean flag = true;
		try {
			flag = ftpClient.changeWorkingDirectory(directory);
			if (flag) {
				System.out.println("进入文件夹" + directory + " 成功！");

			} else {
				System.out.println("进入文件夹" + directory + " 失败！开始创建文件夹");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return flag;
	}
	// 创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
	public boolean CreateDirecroty(String remote) throws IOException {
		boolean success = true;
		String directory = remote + "/";
		// 如果远程目录不存在，则递归创建远程服务器目录
		if (!"/".equalsIgnoreCase(directory) && !changeWorkingDirectory(new String(directory))) {
			int start = 0;
			int end = 0;
			if (directory.startsWith("/")) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf("/", start);
			String path = "";
			String paths = "";
			while (true) {
				String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
				path = path + "/" + subDirectory;
				if (!existFile(path)) {
					if (makeDirectory(subDirectory)) {
						changeWorkingDirectory(subDirectory);
					} else {
						System.out.println("创建目录[" + subDirectory + "]失败");
						changeWorkingDirectory(subDirectory);
					}
				} else {
					changeWorkingDirectory(subDirectory);
				}

				paths = paths + "/" + subDirectory;
				start = end + 1;
				end = directory.indexOf("/", start);
				// 检查所有目录是否创建完毕
				if (end <= start) {
					break;
				}
			}
		}
		return success;
	}

	// 判断ftp服务器文件是否存在
	public boolean existFile(String path) throws IOException {
		boolean flag = false;
		FTPFile[] ftpFileArr = ftpClient.listFiles(path);
		if (ftpFileArr.length > 0) {
			flag = true;
		}
		return flag;
	}

	// 创建目录
	public boolean makeDirectory(String dir) {
		boolean flag = true;
		try {
			flag = ftpClient.makeDirectory(dir);
			if (flag) {
				System.out.println("创建文件夹" + dir + " 成功！");

			} else {
				System.out.println("创建文件夹" + dir + " 失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * * 下载文件 *
	 *
	 * @param pathname  FTP服务器文件目录 *
	 * @param filename  文件名称 *
	 * @param localpath 下载后的文件路径 *
	 * @return
	 */
	public boolean downloadFile(String pathname, String filename, String localpath) {
		boolean flag = false;
		OutputStream os = null;
		try {
			System.out.println("开始下载文件");
			initFtpClient();
			// 切换FTP目录
			ftpClient.changeWorkingDirectory(pathname);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			FTPFile[] ftpFiles = ftpClient.listFiles();
			for (FTPFile file : ftpFiles) {
				if (filename.equalsIgnoreCase(file.getName())) {
					String filepath=localpath + "/" + file.getName();

					File localFile = new File(filepath);
					if(!localFile.getParentFile().exists()){
						localFile.getParentFile().mkdirs();
						localFile.createNewFile();
					}
					os = new FileOutputStream(localFile);
					ftpClient.retrieveFile(file.getName(), os);
					os.close();
				}
			}
			ftpClient.logout();
			flag = true;
			System.out.println("下载文件成功");
		} catch (Exception e) {
			System.out.println("下载文件失败");
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * * 下载文件 *
	 *
	 * @param pathname  FTP服务器文件目录 *
	 * @param filename  文件名称 *
	 * @return
	 */
	public long getFileSize(String pathname, String filename) {
		long fileSize=-1L;
		OutputStream os = null;
		try {
			System.out.println("开始获取文件大小");
			initFtpClient();
			// 切换FTP目录
			ftpClient.changeWorkingDirectory(pathname);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			FTPFile[] ftpFiles = ftpClient.listFiles();
			for (FTPFile file : ftpFiles) {
				if (filename.equalsIgnoreCase(file.getName())) {
					fileSize=file.getSize();
				}
			}
			ftpClient.logout();
			System.out.println("获取文件大小为:"+fileSize);
		} catch (Exception e) {
			System.out.println("获取文件大小失败");
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return fileSize;
	}

	/**
	 * * 删除文件 *
	 *
	 * @return
	 */
	public boolean deleteFile(String fileurl) {
		boolean flag = false;
		String pathname = fileurl.substring(0,fileurl.lastIndexOf("/"));
		String filename = fileurl.substring(fileurl.lastIndexOf("/")+1);
		try {
			System.out.println("开始删除文件");
			initFtpClient();
			// 切换FTP目录
			ftpClient.changeWorkingDirectory(pathname);
			ftpClient.dele(filename);
			ftpClient.logout();
			flag = true;
			System.out.println("删除文件成功");
		} catch (Exception e) {
			System.out.println("删除文件失败");
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	public boolean hasFile(String filePath) {
		boolean flag = false;
		try {

			initFtpClient();
			// 设置文件类型为二进制，与ASCII有区别
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			// 设置编码格式
			ftpClient.setControlEncoding("GBK");

			// 提取绝对地址的目录以及文件名
			String dir = filePath.substring(0, filePath.lastIndexOf("/"));
			String file = filePath.substring(filePath.lastIndexOf("/") + 1);

			// 进入文件所在目录，注意编码格式，以能够正确识别中文目录
			ftpClient.changeWorkingDirectory(new String(dir.getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING));

			// 检验文件是否存在
			InputStream is = ftpClient
					.retrieveFileStream(new String(file.getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING));
			if (is == null || ftpClient.getReplyCode() == FTPReply.FILE_UNAVAILABLE) {
				return false;
			}

			if (is != null) {
				is.close();
				ftpClient.completePendingCommand();
			}
			return true;

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * 获取文件的输入流
	 *
	 * @param path            ftp定义的存储路径 例如 /ftpFile/images
	 * @return
	 * @throws Exception
	 */
	public InputStream getInputStream(String path) {
		byte[] bytes = null;
		InputStream in = null;
		try {
			initFtpClient();
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

			// 转到指定下载目录
			if (path != null) {// 验证是否有该文件夹，有就转到，没有创建后转到该目录下
				ftpClient.changeWorkingDirectory(path);// 转到指定目录下
			}
			// 不需要遍历，改为直接用文件名取
			String remoteAbsoluteFile = toFtpFilename(path);

			// 下载文件
			ftpClient.setBufferSize(1024 * 1024);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			in = ftpClient.retrieveFileStream(remoteAbsoluteFile);
			/*
			 * bytes = input2byte(in); System.out.println("下载成功!" + bytes.length); //
			 * in.read(bytes); in.close();
			 */
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(ftpClient!=null){
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return in;
	}

	/**
	 * 文件转成 byte[]
	 *
	 * @param inStream
	 * @return
	 * @throws IOException
	 * @see [类、类#方法、类#成员]
	 */
	public static byte[] input2byte(InputStream inStream) throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] in2b = swapStream.toByteArray();
		swapStream.close();
		return in2b;
	}

	/** 转化输出的编码 */
	private static String toFtpFilename(String fileName) throws Exception {
		return new String(fileName.getBytes("UTF-8"), "ISO8859-1");
	}

	public static void main(String[] args) throws IOException {
		FtpUtils ftp = new FtpUtils();
		//ftp.uploadFile("ftpFile/data", "123.docx", "E://123.docx");
		// ftp.downloadFile("ftpFile/video", "123.mp4", "D://");
		// ftp.deleteFile("ftpFile/data", "123.docx");
		System.out.println(ftp.hasFile("ftpFile/video/123.mp4"));
		System.out.println(ftp.getFileSize("ftpFile/video", "123.mp4"));
	}
}
