package com.xywg.equipment.monitor.modular.whf.utils;


import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Properties;

/**
 * @author wangcw
 * FTP  上传下载  删除 工具类
 * 属性值由配置文件配置
 */
@SuppressWarnings("all")
public class FTPUtils {

    public static Properties props;
    static FTPClient ftpClient = new FTPClient();

    static {
        Resource resource = new ClassPathResource("/application.yml");
        try {
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static FTPClient connect() {
        ftpClient.setControlEncoding("UTF-8");
        try {
            //连接FTP服务器
            ftpClient.connect(props.getProperty("host"), Integer.parseInt(props.getProperty("port")));
            //登录FTP服务器
            ftpClient.login(props.getProperty("username"), props.getProperty("password"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftpClient;
    }

    /**
     * 上传文件（可供Action/Controller层使用）
     *
     * @param pathname    FTP服务器保存目录
     * @param fileName    上传到FTP服务器后的文件名称
     * @param inputStream 输入文件流
     * @return
     */
    public static boolean uploadFile(String pathname, String fileName, InputStream inputStream) {
        boolean flag = false;

        FTPClient ftpClient = connect();
        try {
            //是否成功登录FTP服务器
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                return flag;
            }
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            createDirecroty(pathname);
            ftpClient.makeDirectory(pathname);
            ftpClient.changeWorkingDirectory(pathname);
            ftpClient.storeFile(fileName, inputStream);
            inputStream.close();
            ftpClient.logout();
            flag = true;
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
     * 上传文件（可供Action/Controller层使用）
     *
     * @param pathname    FTP服务器保存目录
     * @param fileName    上传到FTP服务器后的文件名称
     * @param inputStream 输入文件流
     * @return
     */
    public  boolean uploadFileAsynchronous(String pathname, String fileName, InputStream inputStream) {
        boolean flag = false;

        FTPClient ftpClient = connect();
        try {
            //是否成功登录FTP服务器
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                return flag;
            }
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            createDirecroty(pathname);
            ftpClient.makeDirectory(pathname);
            ftpClient.changeWorkingDirectory(pathname);
            ftpClient.storeFile(fileName, inputStream);
            inputStream.close();
            ftpClient.logout();
            flag = true;
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
     * 图片缩放,w，h为缩放的目标宽度和高度
     * src为源文件目录，dest为缩放后保存目录
     *
     * @param inputStream
     * @param dest
     * @param w
     * @param h
     * @throws Exception
     */
    public static void zoomImage(InputStream inputStream, String dest, int w, int h) throws Exception {
        double wr = 0, hr = 0;
        File destFile = new File(dest);
        //读取图片
        BufferedImage bufImg = ImageIO.read(inputStream);
        //设置缩放目标图片模板
        Image image = bufImg.getScaledInstance(w, h,Image.SCALE_SMOOTH );
        //获取缩放比例
        wr = w * 1.0 / bufImg.getWidth();
        hr = h * 1.0 / bufImg.getHeight();
        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
        image = ato.filter(bufImg, null);
        try {
            //写入缩减后的图片
            ImageIO.write((BufferedImage) image, dest.substring(dest.lastIndexOf(".") + 1), destFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 上传文件（可对文件进行重命名）
     *
     * @param pathname       FTP服务器保存目录
     * @param filename       上传到FTP服务器后的文件名称
     * @param originfilename 待上传文件的名称（绝对地址）
     * @return
     */
    public static boolean uploadFileFromProduction(String pathname, String filename, String originfilename) {
        boolean flag = false;
        try {
            InputStream inputStream = new FileInputStream(new File(originfilename));
            flag = uploadFile(pathname, filename, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 上传文件（不可以进行文件的重命名操作）
     *
     * @param pathname       FTP服务器保存目录
     * @param originfilename 待上传文件的名称（绝对地址）
     * @return
     */
    public static boolean uploadFileFromProduction(String pathname, String originfilename) {
        boolean flag = false;
        try {
            String fileName = new File(originfilename).getName();
            InputStream inputStream = new FileInputStream(new File(originfilename));
            flag = uploadFile(pathname, fileName, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 保存文件
     *
     * @param stream
     * @param path
     * @param fileName
     * @throws IOException
     */
    public static String saveFileFromInputStream(InputStream stream, String path, String fileName) throws IOException {
        String filePath = path + "/" + fileName;
        File newfiles = new File(filePath);
        if (!newfiles.getParentFile().exists()) {
            newfiles.getParentFile().mkdirs();
            newfiles.createNewFile();
        }
        FileOutputStream fs = new FileOutputStream(path + "/" + fileName);
        byte[] buffer = new byte[1024 * 1024];
        int bytesum = 0;
        int byteread = 0;
        while ((byteread = stream.read(buffer)) != -1) {
            bytesum += byteread;
            fs.write(buffer, 0, byteread);
            fs.flush();
        }
        fs.close();
        stream.close();
        return filePath;
    }

    /**
     * 保存文件
     *
     * @param stream
     * @param path
     * @param fileName
     * @throws IOException
     */
    public static String saveFileFromInputStream(InputStream stream, String filePath) throws IOException {
        File newfiles = new File(filePath);
        if (!newfiles.getParentFile().exists()) {
            newfiles.getParentFile().mkdirs();
            newfiles.createNewFile();
        }
        FileOutputStream fs = new FileOutputStream(filePath);
        byte[] buffer = new byte[1024 * 1024];
        int bytesum = 0;
        int byteread = 0;
        while ((byteread = stream.read(buffer)) != -1) {
            bytesum += byteread;
            fs.write(buffer, 0, byteread);
            fs.flush();
        }
        fs.close();
        stream.close();
        return filePath;
    }

    /**
     * 创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
     */
    public static boolean createDirecroty(String remote) throws IOException {
        boolean success = true;
        String directory = remote + "/";
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/") && !ftpClient.changeWorkingDirectory(new String(directory))) {
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
                        ftpClient.changeWorkingDirectory(subDirectory);
                    } else {
                        System.out.println("创建目录[" + subDirectory + "]失败");
                        ftpClient.changeWorkingDirectory(subDirectory);
                    }
                } else {
                    ftpClient.changeWorkingDirectory(subDirectory);
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

    /**
     * 判断ftp服务器文件是否存在
     */
    public static boolean existFile(String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }

    //创建目录
    public static boolean makeDirectory(String dir) {
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
     * 删除文件
     *
     * @param pathname FTP服务器保存目录
     * @param filename 要删除的文件名称
     * @return
     */
    public static boolean deleteFile(String pathname, String filename) {
        boolean flag = false;
        FTPClient ftpClient = connect();
        try {
            //验证FTP服务器是否登录成功
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                return flag;
            }
            //切换FTP目录
            ftpClient.changeWorkingDirectory(pathname);
            ftpClient.dele(filename);
            ftpClient.logout();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.logout();
                } catch (IOException e) {

                }
            }
        }
        return flag;
    }

    /**
     * 下载文件
     *
     * @param pathname  FTP服务器文件目录
     * @param filename  文件名称
     * @param localpath 下载后的文件路径
     * @return
     */
    public static boolean downloadFile(String pathname, String filename, String localpath) {
        boolean flag = false;
        FTPClient ftpClient = connect();
        try {
            //验证FTP服务器是否登录成功
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                return flag;
            }
            //切换FTP目录
            ftpClient.changeWorkingDirectory(pathname);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                if (filename.equalsIgnoreCase(file.getName())) {
                    File localFile = new File(localpath + "/" + file.getName());
                    OutputStream os = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(file.getName(), os);
                    os.close();
                }
            }
            ftpClient.logout();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.logout();
                } catch (IOException e) {

                }
            }
        }
        return flag;
    }


}
