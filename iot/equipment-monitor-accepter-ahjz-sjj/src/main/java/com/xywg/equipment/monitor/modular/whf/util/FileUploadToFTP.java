package com.xywg.equipment.monitor.modular.whf.util;


import com.xywg.equipment.monitor.config.properties.FTPProperties;
import com.xywg.equipment.monitor.modular.whf.model.FileUpload;
import com.xywg.equipment.monitor.modular.whf.utils.FtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.xywg.equipment.monitor.modular.whf.utils.FtpUtil.uploadFile;

@Component
public class FileUploadToFTP {

    @Autowired
   private   FTPProperties ftpProperties;
    /**
     * 文件上传
     *
     */
   public String uploadPicture(String fileName){
       File file = new File(ftpProperties.getFilePath()+fileName);
       long size = file.length();
       FileInputStream fileInputStream = null;
       String ftpFileUrl ="";
       try {
           fileInputStream = new FileInputStream(file);

           FileUpload fileUpload =   fileupload(fileName,fileInputStream,"picture",size);
           ftpFileUrl = fileUpload.getFilePath();
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }catch (IOException e) {
           e.printStackTrace();
       }

     return ftpFileUrl;
   }




    public  FileUpload fileupload(String fileName,FileInputStream inputStream,String type,long size) {
        FileUpload fileupload = new FileUpload();
        String systime = null;
        String destination = null;
        boolean bo = false;
        String fileStorePath = type  + "/" + FtpUtil.getSaveLocation();
        String ext = "";
        try {
                // 获取后缀名称.如xlsx
                ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
                systime = String.valueOf(System.currentTimeMillis());
                // 保存的路径
                destination = "/" + fileStorePath;
                bo = uploadFile(ftpProperties.getHost(), ftpProperties.getPort(), ftpProperties.getUsername(), ftpProperties.getPassword(), ftpProperties.getRootpath(), destination, systime + "." + ext,
                        inputStream);
            if (bo) {
                String path = ftpProperties.getRootpath() + "/" + fileStorePath + "/" + systime + "." + ext;

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
                fileupload.setFileName(fileName);

            } else {
               return  fileupload;
            }
        } catch (Exception e) {
            e.printStackTrace();

           return  fileupload;
        }

        return fileupload;
    }


}
