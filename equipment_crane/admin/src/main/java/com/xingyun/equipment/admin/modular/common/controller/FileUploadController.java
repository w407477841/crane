package com.xingyun.equipment.admin.modular.common.controller;

import static com.xingyun.equipment.admin.core.util.FtpUtil.uploadFile;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import com.xingyun.equipment.admin.config.properties.FtpPropteries;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.core.util.FtpUtil;
import com.xingyun.equipment.admin.modular.common.model.FileUpload;
/**
 * ftp上传
 * @author lw
 * @date 2018年8月21日
 * @company jsxywg
 */
@RequestMapping(value = "/ssdevice/fileUpload")
@Controller
public class FileUploadController {

	private final Log logger = LogFactory.getLog(this.getClass());
	private final String rootPath ;
	private final String url ;
	/**
	 * FTP服务器端口
	 */
	private final int portId ;
	/**
	 * FTP登录账号
	 */
	private final String username ;
	/**
	 * FTP登录密码
	 */
	private final String password ;

	public FileUploadController(@Autowired FtpPropteries propteries) {
		this.rootPath = propteries.getRootPath();
		this.url = propteries.getHost();
		this.portId = propteries.getPort();
		this.username = propteries.getUsername();
		this.password = propteries.getPassword();
	}

	/**
	 * 文件上传
	 * 
	 * @param uploadFile
	 * @param type
	 */
	@RequestMapping(value = "/fileupload", method = RequestMethod.POST)
	@ResponseBody
	public  ResultDTO<FileUpload> fileupload(@RequestParam("uploadFile") MultipartFile uploadFile,
			@RequestParam("type") String type) {
		ResultDTO<FileUpload> model = null;
		FileUpload fileupload = new FileUpload();
		String systime = null;
		String destination = null;
		boolean bo = false;
		String fileStorePath = type  + "/" + FtpUtil.getSaveLocation();
		String org_file_name = null;
		String ext = "";

		try {
			if (uploadFile != null) {
				// 上传的文件名称
				org_file_name = uploadFile.getOriginalFilename();
				// 获取后缀名称.如xlsx
				ext = org_file_name.substring(org_file_name.lastIndexOf(".") + 1, org_file_name.length());
				systime = String.valueOf(System.currentTimeMillis());
				// 保存的路径
				destination = "/" + fileStorePath;
				InputStream inputstream = uploadFile.getInputStream();

				bo = uploadFile(url, portId, username, password, rootPath, destination, systime + "." + ext,
						inputstream);
			}
			if (bo) {
				String path = rootPath + "/" + fileStorePath + "/" + systime + "." + ext;
				long size = uploadFile.getSize();
				String fileSize = "";
				if (size >= 1024 * 1024) {
					fileSize = size / 1024 / 1024 + "M";
				} else if (size > 1024) {
					fileSize = size / 1024 + "K";
				} else {
					fileSize = size + "B";
				}
				fileupload.setFileModel(type);
				fileupload.setFileSize(fileSize);
				fileupload.setFilePath(path);
				fileupload.setFileType(ext);
				fileupload.setFileName(org_file_name);
				model = new ResultDTO<>(true, fileupload);
			} else {
				model = new ResultDTO<>(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			model = new ResultDTO<>(false);
		}

		return model;
	}

	/**
	 * 文件上传
	 *
	 * @param file
	 */
	@RequestMapping(value = "/photoupload", method = RequestMethod.POST)
	@ResponseBody
	public String photoupload(@RequestParam("file") MultipartFile file, @RequestParam("type") String type,
			@RequestParam("userName") String userName) throws IOException {
		String orgFileName ;
		String fileName ;
		String systime ;
		String ext ;
		String filePath = type + "/" + userName + "/" + FtpUtil.getSaveLocation();
		String fileList;
		// 图片名字
		orgFileName = file.getOriginalFilename();
		// 获取后缀名称.如xlsx
		ext = orgFileName.substring(orgFileName.lastIndexOf(".") + 1, orgFileName.length());
		systime = String.valueOf(System.currentTimeMillis());
		fileName = systime + "." + ext;

		InputStream in = file.getInputStream();
		uploadFile(url, portId, username, password, rootPath + "/", filePath, fileName, in);
		fileList = rootPath + "/" + filePath + "/" + fileName;
		return fileList;
	}

	/**
	 * 文件下载
	 * 
	 * @param remotePath
	 * @param response
	 */
	@GetMapping(value = "/filedownload")
	public void filedownload(HttpServletResponse response, @RequestParam String remotePath) {
		String[] sp = remotePath.split("/");
		FtpUtil.downloadFile(response, url, portId, username, password, remotePath, sp[sp.length - 1]);
	}
}
