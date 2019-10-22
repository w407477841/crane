package com.xingyun.equipment.crane.modular.device.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.core.util.DateUtils;
import com.xingyun.equipment.crane.core.util.PdfUtils;
import com.xingyun.equipment.crane.modular.common.service.impl.PDFTest;
import com.xingyun.equipment.crane.modular.device.model.ProjectCrane;
import com.xingyun.equipment.crane.modular.device.model.ProjectCraneCyclicWorkDuration;
import com.xingyun.equipment.crane.modular.device.model.ProjectCraneStatisticsDaily;
import com.xingyun.equipment.crane.modular.device.service.IProjectCraneCyclicWorkDurationService;
import com.xingyun.equipment.crane.modular.device.service.IProjectCraneStatisticsDailyService;
import com.xingyun.equipment.crane.modular.device.service.ProjectCraneService;
import com.xingyun.equipment.crane.modular.device.service.impl.CraneDataServiceImpl;
import com.xingyun.equipment.crane.modular.device.service.impl.ProjectCraneServiceImpl;
import com.xingyun.equipment.crane.modular.device.vo.WeightPercentVO;
import com.xingyun.equipment.system.model.Organization;
import com.xingyun.equipment.system.service.IOrganizationService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 设备情况统计分析导出pdf
 */
@RestController
@RequestMapping("/admin-crane/craneToPdf")
public class CraneToPdfController {
    @Autowired
    private IProjectCraneStatisticsDailyService projectCraneStaticsDailyService;

    @Autowired
    private IOrganizationService organizationService;
    @Autowired
    private IProjectCraneCyclicWorkDurationService iProjectCraneCyclicWorkDurationService;

    @Value("${file.filePath}")
    private String filePath;

    @Autowired
    PDFTest pdfTest;

    @Autowired
    PdfUtils pdfUtils;


    @Autowired
    private ProjectCraneServiceImpl craneService;

    @Autowired
    private  CraneDataServiceImpl craneDataService;

    @RequestMapping(value = "report",method = RequestMethod.GET)
    public void reportPdf(Integer projectId,String startTime,String endTime,String deviceNo,String craneNo,String load,HttpServletResponse response){
     RequestDTO<WeightPercentVO> requestDTO = new RequestDTO();
     requestDTO.setEndTime(endTime);
     requestDTO.setStartTime(startTime);
     requestDTO.setCraneNo(craneNo);
     requestDTO.setDeviceNo(deviceNo);
     requestDTO.setProjectId(projectId);

     requestDTO.setLoad(load);
        requestDTO.setBody(new WeightPercentVO());
        requestDTO.getBody().setProjectId(projectId);
        requestDTO.getBody().setDeviceNo(deviceNo);
        ResultDTO<WeightPercentVO> resultDTO =craneDataService.selectMomentInfo(requestDTO);
//        List<ProjectCraneStatisticsDaily> craneStaticDailyList;
//        //查询满足条件的每日数据
//        Wrapper<ProjectCraneStatisticsDaily> wrapperInfo = new EntityWrapper<>();
//        wrapperInfo.setSqlSelect(
//                "project_id as projectId",
//                "project_name as projectName",
//                "crane_no as craneNo",
//                "device_no as deviceNo",
//                "builder as builder",
//                "sum(lift_frequency) as liftFrequency",
//                "sum(weight_alarm) as weightAlarm",
//                "sum(range_alarm) as rangeAlarm",
//                "sum(limit_alarm) as limitAlarm",
//                "sum(percentage0) as percentage0",
//                "sum(percentage40) as percentage40",
//                "sum(percentage60) as percentage60",
//                "sum(percentage80) as percentage80",
//                "sum(percentage90) as percentage90",
//                "sum(percentage110) as percentage110",
//                "sum(percentage120) as percentage120");
//
//        wrapperInfo.between("work_date", startTime, endTime).eq("crane_no",craneNo);
//        wrapperInfo.eq("project_id", projectId).eq("device_no",deviceNo);
//        craneStaticDailyList = projectCraneStaticsDailyService.selectList(wrapperInfo);

        EntityWrapper<ProjectCrane> wrapper =new EntityWrapper<>();
        wrapper.eq("device_no",deviceNo).eq("is_del",0);
        ProjectCrane crane =craneService.selectOne(wrapper);
        if(resultDTO.getSuccess()==true && resultDTO.getData() !=null){
            Map<String,Object> res = getParams(resultDTO.getData(),crane,requestDTO,1);
            boolean result =pdfUtils.pdfout(res,1);
            if(result){
                downLoadFile(response,1);
            }
        }



    }

    @RequestMapping(value = "print",method = RequestMethod.POST)
    public ResultDTO printPdf(@RequestBody RequestDTO<WeightPercentVO> requestDTO){
        requestDTO.setBody(new WeightPercentVO());
        requestDTO.getBody().setProjectId(requestDTO.getProjectId());
        requestDTO.getBody().setDeviceNo(requestDTO.getDeviceNo());

        ResultDTO<WeightPercentVO> resultDTO =craneDataService.selectMomentInfo(requestDTO);
        Wrapper<ProjectCraneCyclicWorkDuration>  versum = new EntityWrapper<>();
        versum.setSqlSelect("moment_percentage as momentPercentage");
        versum.eq("device_no", requestDTO.getDeviceNo());
        versum.between("create_time", requestDTO.getStartTime(), requestDTO.getEndTime());
        List<ProjectCraneCyclicWorkDuration> durationList = iProjectCraneCyclicWorkDurationService.selectList(versum);
        //载荷系数百分比
       BigDecimal sum = new BigDecimal(0);
        BigDecimal one;
        if (durationList != null && durationList.size() > 0) {

            for (ProjectCraneCyclicWorkDuration aDurationList : durationList) {
                one = aDurationList.getMomentPercentage().divide(new BigDecimal(100), 2);
                sum = sum.add(one.pow(3));
            }
        }
        requestDTO.setLoad(sum.setScale(2, BigDecimal.ROUND_UP).toString());
//        List<ProjectCraneStatisticsDaily> craneStaticDailyList;
//        //查询满足条件的每日数据
//        Wrapper<ProjectCraneStatisticsDaily> wrapperInfo = new EntityWrapper<>();
//        wrapperInfo.setSqlSelect(
//                "project_id as projectId",
//                "project_name as projectName",
//                "crane_no as craneNo",
//                "device_no as deviceNo",
//                "builder as builder",
//                "sum(lift_frequency) as liftFrequency",
//                "sum(weight_alarm) as weightAlarm",
//                "sum(range_alarm) as rangeAlarm",
//                "sum(limit_alarm) as limitAlarm",
//                "sum(percentage0) as percentage0",
//                "sum(percentage40) as percentage40",
//                "sum(percentage60) as percentage60",
//                "sum(percentage80) as percentage80",
//                "sum(percentage90) as percentage90",
//                "sum(percentage110) as percentage110",
//                "sum(percentage120) as percentage120");
//
//        wrapperInfo.between("work_date", requestDTO.getStartTime(), requestDTO.getEndTime()).eq("crane_no",requestDTO.getCraneNo());
//        wrapperInfo.eq("project_id", requestDTO.getProjectId()).eq("device_no",requestDTO.getDeviceNo());
//        craneStaticDailyList = projectCraneStaticsDailyService.selectList(wrapperInfo);
        EntityWrapper<ProjectCrane> wrapper =new EntityWrapper<>();
        wrapper.eq("device_no",requestDTO.getDeviceNo()).eq("crane_no",requestDTO.getCraneNo()).eq("is_del",0);
        ProjectCrane crane =craneService.selectOne(wrapper);
        if(resultDTO.getData() !=null){
            if(resultDTO.getData().getProjectName()!=null){
                Map<String,Object> res = getParams(resultDTO.getData(),crane,requestDTO,2);
                boolean result =pdfUtils.pdfout(res,2);
                if(result){
                    return new ResultDTO(true, filePath+"rectifyReport.pdf");
                }else{
                    return new ResultDTO(false,null,"生成失败");
                }
            }else{
                return new ResultDTO(false,null,"没有对应整改单,请切换日期查询");
            }

        }else{
            return new ResultDTO(false,null,"没有对应整改单,请切换日期查询");
        }


    }

    /**
     * 获取参数
     * @param daily
     * @param crane
     * @param requestDTO
     * @param flag
     * @return
     */
    public Map<String,Object> getParams(WeightPercentVO daily, ProjectCrane crane,RequestDTO requestDTO,int flag){
        Integer builder = daily.getBuilder();
        Organization organization = organizationService.selectById(builder);

        Map<String,String> map = new LinkedHashMap<>();
        Integer total = daily.getPercentage0()+daily.getPercentage40()+daily.getPercentage60()+daily.getPercentage80()
                        +daily.getPercentage90()+daily.getPercentage110()+daily.getPercentage120();
        map.put("address","9#");
        if(StrUtil.isNotBlank(requestDTO.getStartTime())){

            try {
                Date startTime =new SimpleDateFormat("yyyy-MM-dd").parse(requestDTO.getStartTime());
                map.put("beginDate",DateUtil.format(startTime,"YYYY年MM月dd日"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if(StrUtil.isNotBlank(requestDTO.getEndTime())){
            try {
                Date endTime = new SimpleDateFormat("yyyy-MM-dd").parse(requestDTO.getEndTime());
                map.put("endDate",DateUtil.format(endTime,"YYYY年MM月dd日"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("createDate",DateUtil.format(new Date(),"YYYY年MM月dd日"));
        map.put("projectName",daily.getProjectName()==null?"":daily.getProjectName());
        map.put("corpName",organization.getName()==null?"":organization.getName());
        map.put("craneNo",crane.getCraneNo()==null?"":crane.getCraneNo());
        if(crane.getProductionDate() !=null){
            if(StrUtil.isNotBlank(crane.getProductionDate().toString())){
                map.put("productionDate",DateUtil.format(crane.getProductionDate(),"YYYY年MM月dd日"));
            }

        }

        map.put("deviceNo",crane.getDeviceNo()==null?"":crane.getDeviceNo());
        map.put("model",crane.getModel()==null?"":crane.getModel());
        if(crane.getAssembleDate()!=null){
            if(StrUtil.isNotBlank(crane.getAssembleDate().toString())){
                map.put("craneAssembleDate",DateUtil.format(crane.getAssembleDate(),"YYYY年MM月dd日"));
            }
        }
       if(StringUtils.isNotBlank(requestDTO.getContent() )){
            map.put("content",requestDTO.getContent());
       }

        map.put("total",(daily.getWeightAlarm()==null?0:daily.getWeightAlarm())+(daily.getRangeAlarm()==null?0:daily.getRangeAlarm())+(daily.getLimitAlarm()==null?0:daily.getLimitAlarm())+"");
        map.put("weightAlarm",(daily.getWeightAlarm()==null?0:daily.getWeightAlarm())+"");
        map.put("momentAlarm",(daily.getRangeAlarm()==null?0:daily.getRangeAlarm())+"");
        map.put("limitAlarm",(daily.getLimitAlarm()==null?0:daily.getLimitAlarm())+"");
        map.put("times1",daily.getLiftFrequency()==null?"0":daily.getLiftFrequency().toString());

        try {
            Date startTime =new SimpleDateFormat("yyyy-MM-dd").parse(requestDTO.getStartTime());
            Date endTime = new SimpleDateFormat("yyyy-MM-dd").parse(requestDTO.getEndTime());
            long diff = (( endTime.getTime() - startTime.getTime())/(60*60*24*1000))+1;
            map.put("times2", String.valueOf(new BigDecimal(daily.getLiftFrequency()*30/diff).setScale(2, BigDecimal.ROUND_HALF_UP)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(requestDTO.getLoad() !=null){
            if(Double.parseDouble(requestDTO.getLoad())<0.125){
                map.put("content2","很少吊运额定载荷，经常吊运较轻载荷");
            }else if(Double.parseDouble(requestDTO.getLoad())<0.25){
                map.put("content2","较少吊运额定载荷，经常吊运中等载荷");
            }else if(Double.parseDouble(requestDTO.getLoad())<0.50){
                map.put("content2","有时吊运额定载荷，经常吊运较重载荷");
            }else if(Double.parseDouble(requestDTO.getLoad())<1){
                map.put("content2","经常吊运额定载荷");
            }else{
                map.put("content2","经常吊运额定载荷");
            }
        }else{
            map.put("content2","很少吊运额定载荷，经常吊运较轻载荷");
        }

        if(map.get("times2")!=null){
            if(Double.parseDouble(map.get("times2"))<125000){
                map.put("content1","很少使用");
            }else if(Double.parseDouble(map.get("times2"))<250000){
                map.put("content1","不频繁使用");
            }else if(Double.parseDouble(map.get("times2"))<500000){
                map.put("content1","中等频繁使用");
            }else{
                map.put("content1","频繁使用");
            }
        }



        map.put("count0",daily.getPercentage0()+"");
        map.put("count40",daily.getPercentage40()+"");
        map.put("count60",daily.getPercentage60()+"");
        map.put("count80",daily.getPercentage80()+"");
        map.put("count90",daily.getPercentage90()+"");
        map.put("count110",daily.getPercentage110()+"");
        map.put("count120",daily.getPercentage120()+"");
        if(total!=0){
            map.put("percent0",new BigDecimal((float)daily.getPercentage0()*100/total).setScale(2, BigDecimal.ROUND_HALF_UP)+"%");
            map.put("percent40",new BigDecimal((float)daily.getPercentage40()*100/total).setScale(2, BigDecimal.ROUND_HALF_UP)+"%");
            map.put("percent60",new BigDecimal((float)daily.getPercentage60()*100/total).setScale(2, BigDecimal.ROUND_HALF_UP)+"%");
            map.put("percent80",new BigDecimal((float)daily.getPercentage80()*100/total).setScale(2, BigDecimal.ROUND_HALF_UP)+"%");
            map.put("percent90",new BigDecimal((float)daily.getPercentage90()*100/total).setScale(2, BigDecimal.ROUND_HALF_UP)+"%");
            map.put("percent110",new BigDecimal((float)daily.getPercentage110()*100/total).setScale(2, BigDecimal.ROUND_HALF_UP)+"%");
            map.put("percent120",new BigDecimal((float)daily.getPercentage120()*100/total).setScale(2, BigDecimal.ROUND_HALF_UP)+"%");
        }else{
            map.put("percent0","0.00%");
            map.put("percent40","0.00%");
            map.put("percent60","0.00%");
            map.put("percent80","0.00%");
            map.put("percent90","0.00%");
            map.put("percent110","0.00%");
            map.put("percent120","0.00%");
        }
         if(requestDTO.getLoad() !=null){
             map.put("load",requestDTO.getLoad());
         }else{
             map.put("load","0.00");
         }


        DefaultCategoryDataset barDataSet = new DefaultCategoryDataset();

        //添加柱形图数据
        barDataSet.addValue(daily.getPercentage0(), "力矩百分比", "<40%");
        barDataSet.addValue(daily.getPercentage40(), "力矩百分比", "40%~60%");
        barDataSet.addValue(daily.getPercentage60(), "力矩百分比", "60%~80%");
        barDataSet.addValue(daily.getPercentage80(), "力矩百分比", "80%~90%");
        barDataSet.addValue(daily.getPercentage90(), "力矩百分比", "90%~110%");
        barDataSet.addValue(daily.getPercentage110(), "力矩百分比", "110%~120%");
        barDataSet.addValue(daily.getPercentage120(), "力矩百分比", ">120%");

       //创建柱形图，获取图片
        boolean imgFlag=pdfTest.createBarImg(barDataSet);
        Map<String,Object> o=new HashMap();
        Map<String,String> map2 = new HashMap();
        if(imgFlag){
            map2.put("img",filePath+"柱形图.jpg");
            o.put("imgmap",map2);
        }
        o.put("datemap",map);

     return o;
    }


    private void downLoadFile(HttpServletResponse response,int flag) {
       //要下载的文件名
        String fileNameNeedDown="";
         if(flag ==1){
             fileNameNeedDown = "report.pdf";
         }else {
             fileNameNeedDown = "rectifyReport.pdf";
         }

        //这里的路径是要下载的文件所在路径
        String realPath = filePath;
        //要下载的文件路径+文件名
        String aFilePath = realPath + fileNameNeedDown;
        File file;
        file = new File(aFilePath);


        if (file.exists()) { //判断文件父目录是否存在
            response.setContentType("application/force-download");
            response.setCharacterEncoding("UTF-8");
            try {
                response.setHeader("Content-Disposition", "attachment;fileName=" + new String(fileNameNeedDown.getBytes(),"iso-8859-1"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;
            OutputStream os; //输出流
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
            System.out.println("----------file download" + fileNameNeedDown);
            try {
                System.out.println(file.getName());
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }

//                FileUtil.del(file);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }
}
