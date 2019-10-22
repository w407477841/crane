package com.xingyun.equipment.crane.core.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * @author : wangzhibin
 * Description
 * Modified By : caolj
 */
@Component
public class CreateExcel {
    @Value("${filePath}")
    private String filePath;
    //导出Excel公共方法
    public void export(HttpServletResponse response, List rows, String filename, String sendName) {
        ExcelWriter writer = new ExcelWriter(filePath + filename);
        //一次性写出内容
        writer.write(rows);
        //关闭writer，释放内存
        writer.close();
        ////////////////////////////////////
        //下载
        File dis = new File(filePath.substring(0, filePath.length() - 1));
        System.out.println(filePath.substring(0, filePath.length() - 1));
        if (!dis.exists()) {
            dis.mkdir();
        }
        File file = new File(filePath + "/" + filename);
        if (file.exists()) { //判断文件父目录是否存在
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + sendName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;
            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("----------file download" + filename);
            try {
                System.out.println(file.getName());
                bis.close();
                fis.close();
                FileUtil.del(file);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
