package com.xywg.iot.common.utils;

import java.io.*;

/**
 * 文件工具类
 *
 * @author suyunlong
 */
@SuppressWarnings("all")
public class FileUtil {
    /**
     * 获得指定文件的byte数组
     *
     * @param filePath 文件绝对路径
     * @return
     */
    public static byte[] file2Byte(File file) {
        ByteArrayOutputStream bos = null;
        BufferedInputStream in = null;
        try {
            if (!file.exists()) {
                throw new FileNotFoundException("file not exists");
            }
            bos = new ByteArrayOutputStream((int) file.length());
            in = new BufferedInputStream(new FileInputStream(file));
            int bufSize = 1024;
            byte[] buffer = new byte[bufSize];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, bufSize))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据byte数组，生成文件
     *
     * @param bfile    文件数组
     * @param filePath 文件存放路径
     * @param fileName 文件名称
     */
    public static void byte2File(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && !dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }


    public static void writeFile(InputStream is, String fileName) {
        try {
            BufferedInputStream in = null;
            BufferedOutputStream out = null;
            in = new BufferedInputStream(is);

            out = new BufferedOutputStream(new FileOutputStream(fileName));

            int len = -1;
            byte[] b = new byte[1024];
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
