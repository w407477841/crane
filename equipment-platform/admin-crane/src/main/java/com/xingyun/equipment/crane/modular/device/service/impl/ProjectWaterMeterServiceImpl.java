package com.xingyun.equipment.crane.modular.device.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.bean.BeanUtil;
import com.xingyun.equipment.Const;
import com.xingyun.equipment.core.enums.OperationEnum;
import com.xingyun.equipment.crane.modular.projectmanagement.model.ProjectInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.crane.core.aop.ZbusProducerHolder;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.core.util.DateUtils;
import com.xingyun.equipment.cache.RedisUtil;
import com.xingyun.equipment.crane.core.util.StringCompress;
import com.xingyun.equipment.crane.modular.device.dao.ProjectWaterMeterMapper;
import com.xingyun.equipment.crane.modular.device.model.ProjectWaterMeter;
import com.xingyun.equipment.crane.modular.device.model.ProjectWaterMeterAlarm;
import com.xingyun.equipment.crane.modular.device.service.IProjectWaterMeterService;
import com.xingyun.equipment.crane.modular.device.vo.DataVO;
import com.xingyun.equipment.crane.modular.device.vo.MonitorAlarmVO;
import com.xingyun.equipment.crane.modular.device.vo.ProjectWaterMeterAlarmVo;
import com.xingyun.equipment.crane.modular.device.vo.ProjectWaterMeterDetailVo;
import com.xingyun.equipment.crane.modular.device.vo.ProjectWaterMeterHeartbeatVo;
import com.xingyun.equipment.crane.modular.device.vo.ProjectWaterMeterVo;
import com.xingyun.equipment.crane.modular.device.vo.WaterAlarmVO;
import com.xingyun.equipment.crane.modular.infromation.dao.ProjectTargetSetWaterMapper;
import com.xingyun.equipment.crane.modular.projectmanagement.service.IProjectInfoService;
import com.xingyun.equipment.system.service.IOrganizationService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yy
 * @since 2018-09-27
 */
@Service
public class ProjectWaterMeterServiceImpl extends ServiceImpl<ProjectWaterMeterMapper, ProjectWaterMeter> implements IProjectWaterMeterService {
    @Autowired
    private ProjectWaterMeterMapper projectWaterMeterMapper;
    @Autowired
    private IOrganizationService organizationService;
    @Autowired
    private ProjectTargetSetWaterMapper projectTargetSetWaterMapper;
    @Autowired
    ZbusProducerHolder zbusProducerHolder;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<ProjectWaterMeterVo> selectWaterData(Page<ProjectWaterMeterVo> page, RequestDTO<ProjectWaterMeterVo> requestDTO) throws Exception {
        Map<String, Object> map = new HashMap<>(10);
        map.put("orgIds", requestDTO.getOrgIds());
        map.put("key", requestDTO.getKey());
        map.put("status", requestDTO.getBody().getStatus());
        return baseMapper.selectWaterData(page, map);
    }

    @Override
    public List<ProjectWaterMeterHeartbeatVo> selectMonitorStatus(Page<ProjectWaterMeterHeartbeatVo> page, RequestDTO<ProjectWaterMeterHeartbeatVo> requestDTO) throws Exception {
        Map<String, Object> map = new HashMap<>(10);
        map.put("electricId", requestDTO.getId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (new Integer(1).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin", DateUtil.offsetDay(new Date(), -7).toString("yyyy-MM-dd"));
            map.put("end", sdf.format(new Date()));
        } else if (new Integer(2).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin", DateUtil.offsetDay(new Date(), -15).toString("yyyy-MM-dd"));
            map.put("end", sdf.format(new Date()));
        } else if (new Integer(3).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin", DateUtil.offsetDay(new Date(), -30).toString("yyyy-MM-dd"));
            map.put("end", sdf.format(new Date()));
        } else if (new Integer(4).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin", sdf.format(requestDTO.getBody().getBeginDate()));
            map.put("end", sdf.format(requestDTO.getBody().getEndDate()));
        }
        return baseMapper.selectMonitorStatus(page, map);
    }

    @Override
    public List<ProjectWaterMeterDetailVo> selectRunData(Page<ProjectWaterMeterDetailVo> page, RequestDTO<ProjectWaterMeterDetailVo> requestDTO) throws Exception {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMM");
        Map<String, Object> map = new HashMap<>(10);
        map.put("tableName", "t_project_water_meter_detail_" + sdf1.format(new Date()));
        map.put("electricId", requestDTO.getId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (new Integer(1).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin", DateUtil.offsetDay(new Date(), -7).toString("yyyy-MM-dd"));
            map.put("end", sdf.format(new Date()));
        } else if (new Integer(2).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin", DateUtil.offsetDay(new Date(), -15).toString("yyyy-MM-dd"));
            map.put("end", sdf.format(new Date()));
        } else if (new Integer(3).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin", DateUtil.offsetDay(new Date(), -30).toString("yyyy-MM-dd"));
            map.put("end", sdf.format(new Date()));
        } else if (new Integer(4).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin", sdf.format(requestDTO.getBody().getBeginDate()));
            map.put("end", sdf.format(requestDTO.getBody().getEndDate()));
            map.put("tableName", "t_project_water_meter_detail_" + sdf1.format(requestDTO.getBody().getBeginDate()));
        }
        return baseMapper.selectRunData(page, map);
    }

    @Override
    public List<ProjectWaterMeterAlarmVo> selectWarningAlarm(Page<ProjectWaterMeterAlarmVo> page, RequestDTO<ProjectWaterMeterAlarmVo> requestDTO) throws Exception {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMM");
        Map<String, Object> map = new HashMap<>(10);
        map.put("tableName", "t_project_water_meter_alarm_" + sdf1.format(new Date()));
        map.put("electricId", requestDTO.getId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (new Integer(1).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin", DateUtil.offsetDay(new Date(), -7).toString("yyyy-MM-dd"));
            map.put("end", sdf.format(new Date()));
        } else if (new Integer(2).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin", DateUtil.offsetDay(new Date(), -15).toString("yyyy-MM-dd"));
            map.put("end", sdf.format(new Date()));
        } else if (new Integer(3).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin", DateUtil.offsetDay(new Date(), -30).toString("yyyy-MM-dd"));
            map.put("end", sdf.format(new Date()));
        } else if (new Integer(4).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin", sdf.format(requestDTO.getBody().getBeginDate()));
            map.put("end", sdf.format(requestDTO.getBody().getEndDate()));
            map.put("tableName", "t_project_water_meter_alarm_" + sdf1.format(requestDTO.getBody().getBeginDate()));
        }
        return baseMapper.selectWarningAlarm(page, map);
    }

    @Override
    public List<ProjectWaterMeterAlarm> selectAlarmData(Page<ProjectWaterMeterAlarm> page, RequestDTO<ProjectWaterMeterAlarmVo> requestDTO) throws Exception {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMM");
        Map<String, Object> map = new HashMap<>(10);
        map.put("tableName", "t_project_water_meter_alarm_" + sdf1.format(new Date()));
        map.put("electricId", requestDTO.getId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (new Integer(1).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin", DateUtil.offsetDay(new Date(), -7).toString("yyyy-MM-dd"));
            map.put("end", sdf.format(new Date()));
        } else if (new Integer(2).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin", DateUtil.offsetDay(new Date(), -15).toString("yyyy-MM-dd"));
            map.put("end", sdf.format(new Date()));
        } else if (new Integer(3).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin", DateUtil.offsetDay(new Date(), -30).toString("yyyy-MM-dd"));
            map.put("end", sdf.format(new Date()));
        } else if (new Integer(4).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin", sdf.format(requestDTO.getBody().getBeginDate()));
            map.put("end", sdf.format(requestDTO.getBody().getEndDate()));
            map.put("tableName", "t_project_water_meter_alarm_" + sdf1.format(requestDTO.getBody().getBeginDate()));
        }
        return baseMapper.selectAlarmData(page, map);
    }

    @Override
    public List<ProjectWaterMeterVo> selectWater(Page<ProjectWaterMeterVo> page, RequestDTO<ProjectWaterMeterVo> requestDTO) throws Exception {
        Map<String, Object> map = new HashMap<>(10);
        map.put("key", requestDTO.getKey());
        map.put("orgIds", requestDTO.getOrgIds());
        return baseMapper.selectWater(page, map);
    }

    @Override
    public List<ProjectWaterMeterVo> selectWaterList(RequestDTO<ProjectWaterMeterVo> requestDTO) throws Exception {
        Map<String, Object> map = new HashMap<>(10);
        map.put("orgIds", requestDTO.getOrgIds());
        return baseMapper.selectWaterList(map);
    }

///**
// * 首页水表接口
// */
//	@Override
//	public ResultDTO<ProjectWaterMeterInfoVo> getWaterInfo(RequestDTO request) {
//		ProjectWaterMeterInfoVo res = new ProjectWaterMeterInfoVo();
////		List<Integer>   orgIds=organizationService.getOrgsByParent(request.getOrgId());
////		request.setOrgIds(orgIds);
//		
//		 /**
//	     * 安全数据   
//	     */
////	 ProjectWaterMeterDeviceSumVo deviceSum = projectWaterMeterMapper.selectDeviceSum(request);
//	    /**
//	     * 曲线图数据 
//	     */
//	 List<ProjectWaterMeterLineDataVo> lineData=new ArrayList<ProjectWaterMeterLineDataVo>(8); 
//	 List<BigDecimal> re = new ArrayList<>(8);
//	 try {
//		    Calendar cal = Calendar.getInstance();
//		    
//			List<String> yearList= DateUtils.get8DaysBefore(cal.getTime());
//			for(int i=0;i<yearList.size();i++) {
//				ProjectWaterMeterLineDataVo temp = new ProjectWaterMeterLineDataVo();
//		    	request.setYearMonth(yearList.get(i));
//		    	
////		    	re.set(i, projectWaterMeterMapper.selectLineData(request).get(0).getAmount());
//		    	String[] strs=request.getYearMonth().split("-");
//		    	
//		    	String tableName = "t_project_water_meter_detail"+"_"+strs[0].toString()+strs[1].toString();
//		    	System.out.println(tableName);
//		    	BigDecimal tmp = projectWaterMeterMapper.selectLineData(request.getYearMonth(),tableName).getAmount();
//		    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
////		    	temp.setName(String.valueOf(sdf.parse(yearList.get(i))));
//		    	temp.setName(yearList.get(i));
//		    	temp.setAmount(tmp);
//		    	System.out.println(temp);
//		    	lineData.add(temp);
//		    	
//		    	
//		    }
//			for(int i=7;i>0;i--) {
//				double a =lineData.get(i).getAmount().subtract(lineData.get(i-1).getAmount()).doubleValue();
//				lineData.get(i).setAmount(BigDecimal.valueOf(a));
//			}
//         System.out.println(lineData);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	 
//	    	
//	 
//	    /**
//	     * 饼图数据  
//	     */ 
////	 List<ProjectWaterMeterPieDataVo> pieData= projectWaterMeterMapper.selectPieData(request);
//	 
//	    /**
//	     * 安全数据   
//	     */
////	  ProjectWaterMeterUseDataVo useData= projectWaterMeterMapper.selectUseData(request);
//	  
//	  
//	  
////	  res.setDeviceSum(deviceSum);
//	  res.setLineData(lineData);
////	  res.setPieData(pieData);
////	  res.setUseData(useData);
//	  return new ResultDTO<>(true,res);
//	}
    /**
     * 主页水表接口
     */
//	@Override
//	public ResultDTO<Map<String, Object>> getWaterInfo1(Integer orgId) throws Exception {
//		ResultDTO<Map<String, Object>> result = new ResultDTO<>();
//		//格式化数字
//		DecimalFormat df=new DecimalFormat("00");
//		//水表总数
//		int all = 0;
//		//正常
//		int normal = 0;
//		//停用
//		int discontinuation = 0;
//		//异常
//		int abnormal = 0;
//		//同比
//		int tb = 0;
//		//环比
//		int hb = 0;
//		//本月
//		String now="";
//		//上月
//		String now1="";
//		//上上月
//		String now2="";
//		//去年的本月
//		String last="";
//		//去年的上月
//		String last1="";
//		//数量
//		BigDecimal amount = new BigDecimal(0);
//		//生活用水
//		BigDecimal shyd = new BigDecimal(0);
//		//生产用水
//		BigDecimal scyd = new BigDecimal(0);
//		//消防用水
//		BigDecimal xfys = new BigDecimal(0);
//		//本月总量
//		BigDecimal b = new BigDecimal(0);
//		//去年的本月总量
//		BigDecimal tb1 = new BigDecimal(0);
//		//上月总量
//		BigDecimal hb1= new BigDecimal(0);
//		//设备id的集合
//		List<Integer> ids = new ArrayList<>();
//		//曲线图数据
//		List<DataVO> trendList = new ArrayList<>();
//		//饼图数据
//		List<DataVO> pieList = new ArrayList<>();
//		//生活用水
//		DataVO pie1 = new DataVO();
//		//生产用水
//		DataVO pie2 = new DataVO();
//		//消防用水
//		DataVO pie3 = new DataVO();
//		//获取前8天的日期
//		List<String> dateList = DateUtils.get8DaysBefore(new Date());
//		
//		//处理跨年
//		if("1".equals(DateUtils.getMonth())){
//        	now = (Integer.valueOf(DateUtils.getYear())-1)+"12";
//        	now1 = (Integer.valueOf(DateUtils.getYear())-1)+"11";
//        	now2 = (Integer.valueOf(DateUtils.getYear())-1)+"10";
//        	
//        	last = (Integer.valueOf(DateUtils.getYear())-2)+"12";
//        	last1 = (Integer.valueOf(DateUtils.getYear())-2)+"11";
//        }else if("2".equals(DateUtils.getMonth())){
//        	now = DateUtils.getYear()+"01";
//        	now1 = (Integer.valueOf(DateUtils.getYear())-1)+"12";
//        	now2 = (Integer.valueOf(DateUtils.getYear())-1)+"11";
//        	
//        	last = (Integer.valueOf(DateUtils.getYear())-1)+"01";
//        	last1 = (Integer.valueOf(DateUtils.getYear())-2)+"12";
//        }else if("3".equals(DateUtils.getMonth())){
//        	now = DateUtils.getYear()+"02";
//        	now1 = DateUtils.getYear()+"01";
//        	now2 = (Integer.valueOf(DateUtils.getYear())-1)+"12";
//        	
//        	last = (Integer.valueOf(DateUtils.getYear())-1)+"02";
//        	last1 = (Integer.valueOf(DateUtils.getYear())-1)+"01";
//        }else{
//        	now = DateUtils.getYear()+df.format(Integer.valueOf(DateUtils.getMonth())-1);
//        	now1 = DateUtils.getYear()+df.format(Integer.valueOf(DateUtils.getMonth())-2);
//        	now2 = DateUtils.getYear()+df.format(Integer.valueOf(DateUtils.getMonth())-3);
//        	
//        	last = (Integer.valueOf(DateUtils.getYear())-1)+df.format(Integer.valueOf(DateUtils.getMonth())-1);
//        	last1 = (Integer.valueOf(DateUtils.getYear())-1)+df.format(Integer.valueOf(DateUtils.getMonth())-2);
//        }
//		
//		
//		Page<ProjectWaterMeter> page = new Page<>();
//        
//		EntityWrapper<RequestDTO<ProjectWaterMeter>> ew = new EntityWrapper<>();
//        ew.eq("a.is_del", 0);
//        List<Integer>   orgIds=organizationService.getOrgsByParent(orgId);
//        ew.in("a.org_id",orgIds);
//        List<ProjectWaterMeter> list = baseMapper.selectPageList(page, ew);
//        
//        for(ProjectWaterMeter p : list){
//        	all++;
//        	if(p.getStatus()==0){
//        		discontinuation++;	
//        	}
//        	if(p.getStatus()==1){
//        		normal++;	
//        		ids.add(p.getId());
//        		for(int i=0;i<7;i++) {
//        			//如果设备正常，则算月用量
//        			BigDecimal amount1 = new BigDecimal(0);
//            		String begin = dateList.get(0).replace("-","").substring(0,6);
//            		String end = dateList.get(7).replace("-","").substring(0,6);
//            		amount1 = baseMapper.getAmountByDevice(begin,end,dateList.get(0),dateList.get(7),p.getId());
//            		
//            		amount = amount.add(amount1);
//        		}
//        		
//        		if(p.getType()==1){
//        			shyd = shyd.add(amount);
//        		}
//        		if(p.getType()==2){
//        			scyd = scyd.add(amount);
//        		}
//        		if(p.getType()==3){
//        			xfys = xfys.add(amount);
//        		}
//        		
//        		
//        	}
//        }
//        pie1.setName("生活用水");
//        pie1.setAmount(shyd);
//        pie2.setName("生产用水");
//        pie2.setAmount(scyd);
//        pie3.setName("消防用水");
//        pie3.setAmount(xfys);
//        pieList.add(pie1);
//        pieList.add(pie2);
//        pieList.add(pie3);
//        //有正常的设备才去计算
//        if(ids.size()>0){
//        	
//        	for(int i = 0;i<dateList.size()-1;i++){
//        		String month1 = dateList.get(i).replace("-","").substring(0,6);
//        		String month2 = dateList.get(i+1).replace("-","").substring(0,6);
//        		amount = baseMapper.getAmountByDays(month1,month2,dateList.get(i),dateList.get(i+1),ids);
//        		DataVO trend = new DataVO();
//        		trend.setName(String.valueOf(DateUtils.parseDate(dateList.get(i+1)).getTime()));
//        		trend.setAmount(amount);
//        		trendList.add(trend);
//        	}
//        	
//        	b = baseMapper.getAmountByMonth(now1,now,ids);
//            tb1 = baseMapper.getAmountByMonth(last1,last,ids);
//            hb1 = baseMapper.getAmountByMonth(now2,now1,ids);
//            if(tb1.compareTo(BigDecimal.ZERO)!=0){
//            	tb = b.subtract(tb1).divide(tb1,10,RoundingMode.UP).multiply(new BigDecimal(100)).intValue();
//            }
//            if(hb1.compareTo(BigDecimal.ZERO)!=0){
//                hb = b.subtract(hb1).divide(hb1,10,RoundingMode.UP).multiply(new BigDecimal(100)).intValue();
//            }
//        }
//        
//        
//        
//        Map<String, Object> map = new HashMap<>();
//        Map<String, Object> map1 = new HashMap<>();
//        Map<String, Object> map2 = new HashMap<>();
//        map1.put("all", all);
//        map1.put("normal", normal);
//        map1.put("discontinuation", discontinuation);
//        map1.put("abnormal", abnormal);
//        
//        map2.put("tb", tb);
//        map2.put("hb", hb);
//        
//        map.put("deviceSum", map1);
//        map.put("lineData", trendList);
//        map.put("pieData", pieList);
//        map.put("useData", map2);
//        result.setSuccess(true);
//        result.setData(map);
//        result.setCode(200);
//		return result;
//	}
//	

    /**
     * 主页水表接口
     */
    @Override
    public ResultDTO<Map<String, Object>> getWaterInfo(Integer orgId) throws Exception {
        ResultDTO<Map<String, Object>> result = new ResultDTO<>();
        //水表总数
        int all = 0;
        //正常
        int normal = 0;
        //停用
        int discontinuation = 0;
        //异常
        int abnormal = 0;
        //同比
        String tb = "0";
        //环比
        String hb = "0";
        //数量
        BigDecimal amount = new BigDecimal(0);
        //生活用水
        BigDecimal shyd = new BigDecimal(0);
        //生产用水
        BigDecimal scyd = new BigDecimal(0);
        //消防用水
        BigDecimal xfys = new BigDecimal(0);
        //本月总量
        BigDecimal b = new BigDecimal(0);
        //去年的本月总量
        BigDecimal tb1 = new BigDecimal(0);
        //上月总量
        BigDecimal hb1 = new BigDecimal(0);
        //设备id的集合
        List<Integer> ids = new ArrayList<>();
        //曲线图数据
        List<DataVO> trendList = new ArrayList<>();
        List<DataVO> trendListSh = new ArrayList<>();
        List<DataVO> trendListSc = new ArrayList<>();
        List<DataVO> trendListXf = new ArrayList<>();
        //饼图数据
        List<DataVO> pieList = new ArrayList<>();
        //生活用水
        DataVO pie1 = new DataVO();
        //生产用水
        DataVO pie2 = new DataVO();
        //消防用水
        DataVO pie3 = new DataVO();
        //获取前7天的日期
        List<String> dateList = DateUtils.get7DaysBefore(new Date());
        Page<ProjectWaterMeter> page = new Page<>();
        EntityWrapper<RequestDTO<ProjectWaterMeter>> ew = new EntityWrapper<>();
        ew.eq("a.is_del", 0);
        List<Integer> orgIds = organizationService.getOrgsByParent(orgId);
        ew.in("a.org_id", orgIds);
        List<ProjectWaterMeter> list = baseMapper.selectPageList(page, ew);

        for (ProjectWaterMeter p : list) {
            all++;
            if (p.getStatus() == 0) {
                discontinuation++;
            }
            if (p.getStatus() == 1) {
                normal++;
            }
            ids.add(p.getId());
            //算月用量
            BigDecimal amount1 = new BigDecimal(0);
            amount1 = baseMapper.getAmountByDevice("'" + dateList.get(0).toString() + "'", "'" + dateList.get(6).toString() + "'", p.getId());

//            		amount = amount.add(amount1);
            if (p.getType() == null) {
                continue;
            }
            if (p.getType() == 1) {
                shyd = shyd.add(amount1);
            }
            if (p.getType() == 2) {
                scyd = scyd.add(amount1);
            }
            if (p.getType() == 3) {
                xfys = xfys.add(amount1);
            }


        }
        pie1.setName("生活用水");
        pie1.setAmount(shyd);
        pie2.setName("生产用水");
        pie2.setAmount(scyd);
        pie3.setName("消防用水");
        pie3.setAmount(xfys);
        pieList.add(pie1);
        pieList.add(pie2);
        pieList.add(pie3);
        //有设备才去计算
        if (ids.size() > 0) {
            String month = DateUtils.getYear() + DateUtils.getMonth();
            abnormal = baseMapper.selectAbnormal(month, ids);
            for (int i = 0; i < dateList.size(); i++) {

                amount = baseMapper.getAmountByDays("'" + dateList.get(i).toString() + "'", "'" + dateList.get(i).toString() + "'", ids);
                DataVO trend = new DataVO();
                trend.setName(String.valueOf(DateUtils.parseDate(dateList.get(i)).getTime()));
                trend.setAmount(amount);
                trendList.add(trend);
            }
            //计算生活用水曲线
            List<Integer> idsSh = new ArrayList<>();
            for (ProjectWaterMeter p : list) {
                if (p.getType()!=null && p.getType() == 1) {
                    idsSh.add(p.getId());
                }
            }
            for (int i = 0; i < dateList.size(); i++) {

                amount = baseMapper.getAmountByDays("'" + dateList.get(i).toString() + "'", "'" + dateList.get(i).toString() + "'", idsSh);
                DataVO trend = new DataVO();
                trend.setName(String.valueOf(DateUtils.parseDate(dateList.get(i)).getTime()));
                trend.setAmount(amount);
                trendListSh.add(trend);
            }
            //计算生产用水曲线
            List<Integer> idsSc = new ArrayList<>();
            for (ProjectWaterMeter p : list) {
                if (p.getType()!=null && p.getType() == 2) {
                    idsSc.add(p.getId());
                }
            }
            for (int i = 0; i < dateList.size(); i++) {

                amount = baseMapper.getAmountByDays("'" + dateList.get(i).toString() + "'", "'" + dateList.get(i).toString() + "'", idsSc);
                DataVO trend = new DataVO();
                trend.setName(String.valueOf(DateUtils.parseDate(dateList.get(i)).getTime()));
                trend.setAmount(amount);
                trendListSc.add(trend);
            }
            //计算消防用水曲线
            List<Integer> idsXf = new ArrayList<>();
            for (ProjectWaterMeter p : list) {
                if (p.getType()!=null && p.getType() == 3) {
                    idsXf.add(p.getId());
                }
            }
            for (int i = 0; i < dateList.size(); i++) {

                amount = baseMapper.getAmountByDays("'" + dateList.get(i).toString() + "'", "'" + dateList.get(i).toString() + "'", idsXf);
                DataVO trend = new DataVO();
                trend.setName(String.valueOf(DateUtils.parseDate(dateList.get(i)).getTime()));
                trend.setAmount(amount);
                trendListXf.add(trend);
            }
            String beforMonth = (DateUtils.getYear() + "-" + (Integer.valueOf(DateUtils.getMonth()) - 1)) + "%";
            ;
            String nowMonth = DateUtils.getYear() + "-" + DateUtils.getMonth() + "%";
            String beforYear = (Integer.valueOf(DateUtils.getYear()) - 1) + "-" + DateUtils.getMonth() + "%";
            b = baseMapper.getAmountByMonth(nowMonth, ids);
            tb1 = baseMapper.getAmountByMonth(beforYear, ids);
            hb1 = baseMapper.getAmountByMonth(beforMonth, ids);
            DecimalFormat df = new DecimalFormat("#.00");
            if (tb1.compareTo(BigDecimal.ZERO) != 0) {
                tb = df.format(b.subtract(tb1).divide(tb1, 10, RoundingMode.UP).multiply(new BigDecimal(100)).doubleValue());
            }
            if (hb1.compareTo(BigDecimal.ZERO) != 0) {
                hb = df.format(b.subtract(hb1).divide(hb1, 10, RoundingMode.UP).multiply(new BigDecimal(100)).doubleValue());
            }

        }


        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map1.put("all", all);
        map1.put("normal", normal);
        map1.put("discontinuation", discontinuation);
        map1.put("abnormal", abnormal);

        map2.put("tb", Double.parseDouble(tb));
        map2.put("hb", Double.parseDouble(hb));

        map.put("deviceSum", map1);
        map.put("lineData", trendList);
        map.put("lineDataSh", trendListSh);
        map.put("lineDataSc", trendListSc);
        map.put("lineDataXf", trendListXf);
        map.put("pieData", pieList);
        map.put("useData", map2);
        result.setSuccess(true);
        result.setData(map);
        result.setCode(200);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(ProjectWaterMeter entity) {
        Map<String, Object> map = new HashMap(10);
        map.put("specification", entity.getSpecification());
        map.put("manufactor", entity.getManufactor());
        projectTargetSetWaterMapper.plusCallTimes(map);
        String uuid = projectInfoService.selectById(entity.getProjectId()).getUuid();
        entity.setIsOnline(0);
        boolean isSuccess = super.insert(entity);
        try {
            zbusProducerHolder.modifyDevice(uuid, JSONUtil.toJsonStr(entity), "add", "water");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(ProjectWaterMeter entity) {
        ProjectWaterMeter projectWaterMeter = selectById(entity.getId());
        List<ProjectWaterMeter> list = new ArrayList(10);
        list.add(projectWaterMeter);
        projectTargetSetWaterMapper.minusCallTimes(list);
        Map<String, Object> map = new HashMap(10);
        map.put("specification", entity.getSpecification());
        map.put("manufactor", entity.getManufactor());
        String uuid = projectInfoService.selectById(entity.getProjectId()).getUuid();
        try {
            zbusProducerHolder.modifyDevice(uuid, JSONUtil.toJsonStr(entity), "edit", "water");
        } catch (Exception e) {
            e.printStackTrace();
        }
        projectTargetSetWaterMapper.plusCallTimes(map);
        boolean isSuccess = super.updateById(entity);
        String key = Const.DEVICE_INFO_PREFIX + "water:" + entity.getDeviceNo();
        redisUtil.remove(key);
        return isSuccess;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatchIds(List<? extends Serializable> idList) {
        Wrapper<ProjectWaterMeter> wrapper = new EntityWrapper<>();
        wrapper.in("id", idList);
        List<ProjectWaterMeter> list = baseMapper.selectList(wrapper);
        projectTargetSetWaterMapper.minusCallTimes(list);
        for (int i = 0; i < idList.size(); i++) {
            Object id = idList.get(i);
            ProjectWaterMeter entity = new ProjectWaterMeter();
            entity.setId((Integer) id);
            String uuid = projectInfoService.selectById(baseMapper.selectOne(entity).getProjectId()).getUuid();
            try {
                zbusProducerHolder.modifyDevice(uuid, JSONUtil.toJsonStr(baseMapper.selectOne(entity)), "delete", "water");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        boolean isSuccess = super.deleteBatchIds(idList);
        for (int i = 0; i < list.size(); i++) {
            String key = Const.DEVICE_INFO_PREFIX + "water:" + list.get(i).getDeviceNo();
            redisUtil.remove(key);
        }
        return isSuccess;
    }

    /**
     * 智慧工地用
     */
    @Override
    public byte[] getWaterDetailInfo(RequestDTO<ProjectWaterMeter> res) {
        Page<ProjectWaterMeter> page = new Page<>(res.getPageNum(), res.getPageSize());
        HashMap<String, Object> map = new HashMap<>();

        map.put("tableName", "t_project_water_meter_detail_" + res.getYearMonth());
        Integer year = Integer.valueOf(res.getYearMonth().substring(0, 4));
        Integer month = Integer.valueOf(res.getYearMonth().substring(4, 6));
        String month1 = "";
        if (month == 1) {
            year = year - 1;
            month = 12;
        } else {
            month = month - 1;
        }
        if (month < 10) {
            month1 = "0" + month;
        } else {
            month1 = month.toString();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar1.set(Calendar.DAY_OF_MONTH, 1);
        String lastMonthDay = dateFormat.format(calendar.getTime());
        String fristDay = dateFormat.format(calendar1.getTime());
        System.out.println("23322");
        System.out.println(lastMonthDay);
        map.put("lastMonthDay", lastMonthDay);
        map.put("fristDay", fristDay);
        map.put("tableName1", "t_project_water_meter_detail_" + (year.toString() + month1));
        map.put("deviceNo", res.getDeviceNo());
        map.put("uuid", res.getUuid());

        System.out.println(res.getDeviceNo());
        List<ProjectWaterMeterVo> list = baseMapper.getWaterDetailInfo(page, map);

        Map<String, Object> resultMap = new HashMap<>(10);

        if (list != null && !list.isEmpty() && list.get(0) != null) {
            System.out.println("时间" + list);
            resultMap.put("waterList", list);
            resultMap.put("total", page.getTotal());
        } else {
            resultMap.put("waterList", new ArrayList<>());
            resultMap.put("total", 0);
        }


        ResultDTO<Map<String, Object>> resultDTO = new ResultDTO<>(true, resultMap);
        String resultStr = JSONUtil.toJsonStr(resultDTO);
        return StringCompress.compress(resultStr);

    }

    /**
     * 智慧工地用
     */
    @Override
    public byte[] getAlarmInfo(RequestDTO<WaterAlarmVO> res) {
        Page<WaterAlarmVO> page = new Page<>(res.getPageNum(), res.getPageSize());
        HashMap<String, Object> map = new HashMap<>();
        map.put("tableName", "t_project_water_meter_alarm_" + res.getYearMonth());

        String[] uuids = res.getUuids().toString().split(",");
        map.put("alarmId", res.getAlarmId());
        map.put("deviceNo", res.getDeviceNo());
        map.put("uuids", uuids);
        List<WaterAlarmVO> alarmList = baseMapper.getAlarmInfo(page, map);
        Map<String, Object> resultMap = new HashMap<>(10);
        resultMap.put("total", page.getTotal());

        resultMap.put("alarmList", alarmList);
        ResultDTO<Map<String, Object>> resultDTO = new ResultDTO<>(true, resultMap);
        String resultStr = JSONUtil.toJsonStr(resultDTO);
        return StringCompress.compress(resultStr);
    }

    /**
     * 智慧工地拉取报警明细信息
     */
    @Override
    public byte[] getAlarmDetail(RequestDTO<WaterAlarmVO> res) {
        Page<WaterAlarmVO> page = new Page<>(res.getPageNum(), res.getPageSize());
        HashMap<String, Object> map = new HashMap<>();
        map.put("tableName", "t_project_water_meter_alarm_" + res.getYearMonth());
        map.put("alarmId", res.getAlarmId());
        map.put("deviceNo", res.getDeviceNo());
        List<WaterAlarmVO> waters = baseMapper.getAlarmDetail(page, map);
        Map<String, Object> resultMap = new HashMap<>(10);
        resultMap.put("total", page.getTotal());
        resultMap.put("infoList", waters);
        ResultDTO<Map<String, Object>> resultDTO = new ResultDTO<>(true, resultMap);
        String resultStr = JSONUtil.toJsonStr(resultDTO);
        return StringCompress.compress(resultStr);
    }

    @Override
    public boolean setUse(RequestDTO<ProjectWaterMeter> requestDTO) throws Exception {
        List<ProjectWaterMeter> list = new ArrayList<>(10);
        Wrapper<ProjectWaterMeter> wrapper = new EntityWrapper<>();
        List<ProjectWaterMeter> allWaterMeter = this.selectList(wrapper);
        wrapper.in("id", requestDTO.getIds());
        List<ProjectWaterMeter> projectWaterMeters = this.selectList(wrapper);
        for (int i = 0; i < allWaterMeter.size(); i++) {
            for (int j = 0; j < projectWaterMeters.size(); j++) {
                if (allWaterMeter.get(i).getDeviceNo().equals(projectWaterMeters.get(j).getDeviceNo()) && !allWaterMeter.get(i).getId().equals(projectWaterMeters.get(j).getId()) && new Integer(1).equals(allWaterMeter.get(i).getStatus())) {
                    throw new RuntimeException("有设备在其他项目中已启用");
                }
            }
        }
        for (int i = 0; i < requestDTO.getIds().size(); i++) {
            ProjectWaterMeter projectWaterMeter = new ProjectWaterMeter();
            projectWaterMeter.setId(Integer.parseInt(requestDTO.getIds().get(i).toString()));
            projectWaterMeter.setStatus(1);
            list.add(projectWaterMeter);
        }
        boolean isSuccess = false;
        if (this.updateBatchById(list)) {
            isSuccess = true;
            for (int i = 0; i < projectWaterMeters.size(); i++) {
                String key = Const.DEVICE_INFO_PREFIX + "water:" + projectWaterMeters.get(i).getDeviceNo();
                redisUtil.remove(key);
            }
        }
        return isSuccess;
    }

    @Override
    public ProjectWaterMeterVo selectWaterById(Serializable id) {
        ProjectWaterMeter projectWaterMeter = baseMapper.selectById(id);
        ProjectInfo projectInfo = projectInfoService.selectById(projectWaterMeter.getProjectId());
        ProjectWaterMeterVo projectWaterMeterVo = new ProjectWaterMeterVo();
        BeanUtil.copyProperties(projectWaterMeter, projectWaterMeterVo);
        projectWaterMeterVo.setProjectName(projectInfo.getName());
        return projectWaterMeterVo;
    }
}
