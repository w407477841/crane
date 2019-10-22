package com.xingyun.equipment.admin.modular.common.service.impl;


import cn.hutool.system.SystemUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import javafx.application.Platform;
import lombok.Data;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import sun.awt.PlatformFont;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
@Data
public class PDFTest {
    @Value("${file.filePath}")
   private  String filePath;
//    private  String  filePath="d:/template/";

    public    boolean createBarImg(DefaultCategoryDataset barDataSet) {
        boolean flag=false;
        Document document = new Document();
        try {

            //设置中文样式（不设置，中文将不会显示）
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font fontChinese_title = new Font(bfChinese, 20, Font.BOLD, BaseColor.BLACK);
            Font fontChinese_content = new Font(bfChinese, 10, Font.NORMAL, BaseColor.BLACK);


            JFreeChart barChart = ChartFactory.createBarChart("",
                    "",
                    "",
                    barDataSet,
                    PlotOrientation.VERTICAL,
                    true,
                    false,
                    true
            );
            //设定阈值线
//            barChart.getCategoryPlot().addRangeMarker(new ValueMarker(30.0));

            barChart.getLegend().setItemFont(new java.awt.Font("宋体", java.awt.Font.PLAIN, 16));
            //获取title
            barChart.getTitle().setFont(new java.awt.Font("宋体", java.awt.Font.PLAIN, 16));

            //获取绘图区对象
            CategoryPlot barPlot = barChart.getCategoryPlot();
            //设定网格横线颜色
//            barPlot.setRangeGridlinePaint(Color.black);
            //设定网格横线为实线
//            barPlot.setRangeGridlineStroke(new BasicStroke());
            //显示
//            barPlot.setRangeGridlinesVisible(true);
            barPlot.setBackgroundAlpha(0.1f);
            BarRenderer render = new BarRenderer();
            //取消柱子阴影效果
            render.setShadowVisible(false);
            //设置宽度
            render.setMaximumBarWidth(10);
            //取消边框线
            render.setDrawBarOutline(false);
            //取消渐变色
            render.setBarPainter( new StandardBarPainter() );
            //设置柱子颜色，从第一根开始
            render.setSeriesPaint(0,Color.gray);
            render.setSeriesPaint(1,Color.gray);
            render.setSeriesPaint(2,Color.gray);
            render.setSeriesPaint(3,Color.gray);
            render.setSeriesPaint(4,Color.gray);


            barPlot.setRenderer(render);
            //获取坐标轴对象
            CategoryAxis barAxis = barPlot.getDomainAxis();
            barAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
            //设置坐标轴字体
            barAxis.setLabelFont(new java.awt.Font("宋体", java.awt.Font.PLAIN, 13));
            //设置坐标轴标尺值字体（x轴）
            barAxis.setTickLabelFont(new java.awt.Font("宋体", java.awt.Font.PLAIN, 13));
            //获取数据轴对象（y轴）
            ValueAxis rangeAxis_bar = barPlot.getRangeAxis();
            rangeAxis_bar.setLabelFont(new java.awt.Font("宋体", java.awt.Font.PLAIN, 13));


            FileOutputStream bar_fos = null;
            try {
                bar_fos = new FileOutputStream(filePath+"柱形图.jpg");
                ChartUtils.writeChartAsJPEG(bar_fos, 0.9f, barChart, 550, 500);
                bar_fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("over");
            document.close();
            flag =true;
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
            flag =false;
        }
        return flag;
    }





}