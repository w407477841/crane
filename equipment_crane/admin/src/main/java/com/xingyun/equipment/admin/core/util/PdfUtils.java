package com.xingyun.equipment.admin.core.util;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by zhouyujie on 2019/07/04
 */
@Component
@Data
public class PdfUtils {
   @Value("${file.filePath}")
   private String filePath;
    @Value("${ftp.host}")
   private String host;
    @Value("${ftp.port}")
    private int port;
    @Value("${ftp.username}")
    private String username;
    @Value("${ftp.password}")
    private String password;
    @Value("${ftp.root-path}")
    private String rootPath;

//    private  String filePath="d:/template/";
    // 利用模板生成pdf
    public  boolean pdfout(Map<String,Object> o,int type) {
        boolean flag =false;
        // 模板路径

          String  templatePath="";
        if(type ==1){
              templatePath=  this.getClass().getClassLoader().getResource("templates/device.pdf").getPath();
          }else{
              templatePath=  this.getClass().getClassLoader().getResource("templates/rectify.pdf").getPath();
          }



        // 生成的新文件路径
        String newPDFPath = "";
          if(type ==1){
              newPDFPath =filePath+"report.pdf";
          }else{
              newPDFPath =filePath+"rectifyReport.pdf";
          }


        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font FontChinese = new Font(bf, 16, Font.BOLD, BaseColor.BLACK);
            out = new FileOutputStream(newPDFPath);// 输出流
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            //文字类的内容处理
            Map<String,String> datemap = (Map<String,String>)o.get("datemap");

            for(String key : datemap.keySet()){
                String value = datemap.get(key);
                form.setFieldProperty(key,"textfont",bf,null);
                form.setFieldProperty(key,"textsize",10f,null);
                form.addSubstitutionFont(bf);
                form.setField(key,value);
            }
            //图片类的内容处理
            Map<String,String> imgmap = (Map<String,String>)o.get("imgmap");
            for(String key : imgmap.keySet()) {
                String value = imgmap.get(key);
                String imgpath = value;
                int pageNo = form.getFieldPositions(key).get(0).page;
                Rectangle signRect = form.getFieldPositions(key).get(0).position;
                float x = signRect.getLeft();
                float y = signRect.getBottom();
                //根据路径读取图片
                Image image = Image.getInstance(imgpath);
                //获取图片页面
                PdfContentByte under = stamper.getOverContent(pageNo);
                //图片大小自适应
                image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                //添加图片
                image.setAbsolutePosition(x, y);
                under.addImage(image);
            }
            stamper.setFormFlattening(true);// 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
            stamper.close();
            Document doc = new Document();

            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();
            flag=true;
        } catch (IOException e) {
            System.out.println(e);
            flag =false;
        } catch (DocumentException e) {
            System.out.println(e);
            flag =false;
        }
          return flag;
    }


}