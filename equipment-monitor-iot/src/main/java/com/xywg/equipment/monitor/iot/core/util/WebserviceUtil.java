package com.xywg.equipment.monitor.iot.core.util;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xywg.equipment.monitor.iot.modular.alipush.service.PushService;
import com.xywg.equipment.monitor.iot.modular.base.dto.AlarmDTO;
import com.xywg.equipment.monitor.iot.modular.base.dto.ResultDTO;
import com.xywg.equipment.monitor.iot.modular.base.factory.AlarmDTOFactory;
import com.xywg.equipment.monitor.iot.modular.base.factory.BaseFactory;
import com.xywg.equipment.monitor.iot.modular.base.model.ProjectInfo;
import com.xywg.equipment.monitor.iot.modular.base.service.IProjectInfoService;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneAlarmMapper;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneDetailMapper;
import com.xywg.equipment.monitor.iot.modular.crane.dto.CurrentCraneData;
import com.xywg.equipment.monitor.iot.modular.crane.factory.CraneAlarmFactory;
import com.xywg.equipment.monitor.iot.modular.crane.factory.CraneFactory;
import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCrane;
import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCraneAlarm;
import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCraneDetail;
import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCraneHeartbeat;
import com.xywg.equipment.monitor.iot.modular.crane.service.IProjectCraneHeartbeatService;
import com.xywg.equipment.monitor.iot.modular.crane.service.IProjectCraneService;
import com.xywg.equipment.monitor.iot.modular.lift.dao.ProjectLiftAlarmMapper;
import com.xywg.equipment.monitor.iot.modular.lift.dao.ProjectLiftDetailMapper;
import com.xywg.equipment.monitor.iot.modular.lift.dto.CurrentLiftData;
import com.xywg.equipment.monitor.iot.modular.lift.factory.LiftAlarmFactory;
import com.xywg.equipment.monitor.iot.modular.lift.factory.LiftFactory;
import com.xywg.equipment.monitor.iot.modular.lift.model.ProjectLift;
import com.xywg.equipment.monitor.iot.modular.lift.model.ProjectLiftAlarm;
import com.xywg.equipment.monitor.iot.modular.lift.model.ProjectLiftDetail;
import com.xywg.equipment.monitor.iot.modular.lift.model.ProjectLiftHeartbeat;
import com.xywg.equipment.monitor.iot.modular.lift.service.IProjectLiftHeartbeatService;
import com.xywg.equipment.monitor.iot.modular.lift.service.IProjectLiftService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:47 2018/8/22
 * Modified By : wangyifei
 */
@Component
public class    WebserviceUtil {
    @Autowired
    private IProjectCraneService   craneService;
    @Autowired
    private IProjectLiftService   liftService;
    @Autowired
    private ProjectCraneDetailMapper craneDetailMapper;
    @Autowired
    private ProjectLiftDetailMapper   liftDetailMapper;
    @Autowired
    private ProjectCraneAlarmMapper  craneAlarmMapper;
    @Autowired
    private ProjectLiftAlarmMapper  liftAlarmMapper;
    @Autowired
    RedisUtil   redisUtil;
    @Autowired
    IProjectLiftHeartbeatService    projectLiftHeartbeatService;
    @Autowired
    IProjectCraneHeartbeatService projectCraneHeartbeatService;
    @Autowired
    IProjectInfoService   projectInfoService;
    @Autowired
    XywgProerties  xywgProerties;
    @Autowired
    protected SimpMessageSendingOperations simpMessageSendingOperations;
@Autowired
PushService pushService;



  /**
 * Author: wangyifei
 *
 * Description: 获取 塔吊 认证信息
 * Date: 16:56 2018/8/22
 * @return 认证令牌
*/
private   String   getForeignKey() {
    return getForeignKey(
            "南通三建海门大学",
            "123456",
            "http://0513.safe110.net:9994/webservice/MonitorSystemWS.asmx",
            "http://tempuri.org/",
            "userLogin",
            "userLogin");
}

 /**
 * @author: wangyifei
 *
 * Description: 获取 塔吊 认证信息
 * Date: 16:56 2018/8/22
 * @param username 用户名
 * @param password 密码
 * @param url 地址
 * @param namespace  命名空间
 * @param actionUri  action路径
 * @param op 请求方法
 * @return 认证令牌
*/
private   String   getForeignKey(String username,String password,String url,String namespace,String actionUri,String op){

    try {
    Service service = new Service();
    Call call = (Call) service.createCall();
    call.setTargetEndpointAddress(new URL(url));
    call.setUseSOAPAction(true);
    // action uri
    call.setSOAPActionURI(namespace + actionUri);
    // 设置要调用哪个方法
    call.setOperationName(new QName(namespace, op));
    // 设置参数名称，具体参照从浏览器中看到的
    //设置请求参数及类型
    call.addParameter(new QName(namespace, "username"), XMLType.XSD_STRING, ParameterMode.IN);
    //设置请求参数及类型
    call.addParameter(new QName(namespace, "userpwd"), XMLType.XSD_STRING, ParameterMode.IN);
    //call.setReturnType(new QName(namespace,"getinfo"),Model.class); //设置返回结果为是某个类
    //设置结果返回类型
    call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
    Object[] params = new Object[] {username,password};
    //方法执行后的返回值
  String  result = (String) call.invoke(params);
    JSONObject jsStr = JSONObject.fromObject(result);
   String  fk = jsStr.getString("obj");
    System.out.println("foreignKey:"+fk);
    return fk;
    }catch (Exception e){
        return null;
    }
}
/**
* @author: wangyifei
* Description: 模拟HTTP -get 请求
* Date: 19:33 2018/8/22
*/
    public  Map<String,Object> httpGet(String url, Map<String,Object> cMap){
        //get请求返回结果
        JSONObject jsonResult = null;
        Map<String,Object> map = new HashMap<>(16);
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            CookieStore cookieStore1 = (CookieStore) cMap.get("cookieStore");
            if(cookieStore1!=null){
                client.setCookieStore(cookieStore1);
            }
            //发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);

            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());
                /**把json字符串转换成json对象**/
                jsonResult = JSONObject.fromObject(strResult);
                url = URLDecoder.decode(url, "UTF-8");


                CookieStore cookieStore = client.getCookieStore();
                map.put("jsonResult", jsonResult);
                map.put("cookieStore", cookieStore);
            } else {
                System.out.println("get请求提交失败:" + url);
            }
        } catch (IOException e) {
            System.out.println("get请求提交失败:" + url);
        }
        return map;
    }
    @Transactional(rollbackFor = Exception.class)
    public  void selectDevices(){
        Map<String,Object> cMap = new HashMap<String,Object>(16);
        String fk = getForeignKey();
        String url="http://0513.safe110.net:9992/Default/Realtime/RealtimeList.aspx";
        String query = "ViewType=outcome&UserName=%e5%8d%97%e9%80%9a%e4%b8%89%e5%bb%ba%e6%b5%b7%e9%97%a8%e5%a4%a7%e5%ad%a6&ForeignKey="+fk+"&Datatype=json";

        Map<String,Object> map = httpGet(url+"?"+query,cMap);
        JSONObject result = (JSONObject) map.get("jsonResult");
        // 设备在线状态

             JSONArray list= (JSONArray) result.get("rows");
             StringBuilder snCreane  = new StringBuilder("%7b");
             StringBuilder snLift  = new StringBuilder("%7b");
             for(int i=0;i<list.size();i++){
                 JSONObject   object = list.getJSONObject(i);
                 //在线
                 if(object.getInt("IsOnline") == 1){

                     if(object.getInt("MonitorType") == 0){
                         //塔吊
                         ProjectCraneHeartbeat heartbeat  = new ProjectCraneHeartbeat();
                         heartbeat.setDeviceNo(object.getString("DeviceSN"));

                         ProjectCrane crane = craneService.selectOne( object.getString("DeviceSN"));
                         if(crane!=null&&1==crane.getStatus()){
                             heartbeat.setCraneId(crane.getId());
                             heartbeat.setCreateTime(new Date());
                             if(!redisUtil.exists(this.xywgProerties.getRedisHead()+":"+"head:"+"qzjht:"+object.getString("DeviceSN"))){
                                 //开机
                                 projectCraneHeartbeatService.doOpenBusiness(heartbeat);
                             }else{
                                 projectCraneHeartbeatService.updateEndTime(heartbeat);

                             }
                             redisUtil.setSec(this.xywgProerties.getRedisHead()+":"+"head:"+"qzjht:"+object.getString("DeviceSN"),1,45L);
                             snCreane.append("%22").append(object.getString("DeviceSN")).append("%22%2c");
                         }



                     }else if(object.getInt("MonitorType") == 1) {
                         //升降机
                         ProjectLiftHeartbeat heartbeat = new ProjectLiftHeartbeat();
                         heartbeat.setDeviceNo(object.getString("DeviceSN"));

                         ProjectLift lift = liftService.selectOne(object.getString("DeviceSN"));
                         if (lift != null&&1==lift.getStatus()) {
                             heartbeat.setLiftId(lift.getId());
                             heartbeat.setCreateTime(new Date());

                             if (!redisUtil.exists(this.xywgProerties.getRedisHead()+":"+"head:"+"sjjht:" + object.getString("DeviceSN"))) {

                                 projectLiftHeartbeatService.doOpenBusiness(heartbeat);

                             } else {
                                 projectLiftHeartbeatService.updateEndTime(heartbeat);

                             }
                             snLift.append("%22").append(object.getString("DeviceSN")).append("%22%2c");
                             redisUtil.setSec(this.xywgProerties.getRedisHead()+":"+"head:"+"sjjht:"+object.getString("DeviceSN"),1,45L);
                         }
                     }
                 }else{
                     //离线


                     //TODO  guo
                 }
             }
        snCreane.append("%7d");
        snLift.append("%7d");
        CookieStore cookieStore = (CookieStore) map.get("cookieStore");
        cMap.put("cookieStore", cookieStore);


            if(snCreane.length() > 6){

                insertCrane(snCreane.toString().replace("%2c%7d","%7d"),cMap);
            }
            if(snLift.length() > 6){
                insertLift(snLift.toString().replace("%2c%7d","%7d"),cMap);
            }


    }
    /**
    * @author: wangyifei
    * Description: 插入塔吊 实时数据
    * Date: 13:41 2018/8/23
    */
public  void   insertCrane(String sn, Map<String,Object> cMap){
  String  url = "http://0513.safe110.net:9992/App_Ajax/Flash.ashx";
  String query = "PostType=GetTowerCraneLiveDataCustom&SysOrgAreas=777be33f-0944-e211-95ea-bcaec5395ab5&DeviceSN=" + sn;
    Map<String,Object> map = httpGet(url + "?" + query, cMap);
    JSONObject   result = (JSONObject) map.get("jsonResult");
    System.out.println(result.toString());

    JSONArray  rows = result.getJSONArray("rows");
    for( int  i = 0 ; i<rows.size() ; i++ ){
        // 查询设备

        ProjectCrane crane = craneService.selectOne( rows.getJSONObject(i).getString("DeviceSN"));

        if(crane  != null&&1==crane.getStatus()){
            ProjectInfo projectInfo = projectInfoService.selectById(crane.getProjectId());
            if(projectInfo!=null){
                // 插入记录
                ProjectCraneDetail detail =   CraneFactory.factory(
                        crane,
                        rows.getJSONObject(i)
                );
                craneDetailMapper.createDetail(detail,BaseFactory.getTableName(ProjectCraneDetail.class));

                CurrentCraneData currentCraneData = CurrentCraneData.factory(detail,crane);

                this.push(redisUtil,detail.getDeviceNo(),"crane",projectInfo.getUuid(),currentCraneData,""+crane.getProjectId());
                List<AlarmDTO>   alarmInfo   = new ArrayList<>();
                //报警信息
                JSONArray  alarms =  rows.getJSONObject(i).getJSONArray("Alarm");
                for( int  j = 0 ; j<alarms.size(); j++){
                    ProjectCraneAlarm alarm  = CraneAlarmFactory.factory(crane,alarms.getJSONObject(j));
                    alarmInfo.add(AlarmDTOFactory.factory(alarm,projectInfo,"塔吊设备"));
                   craneAlarmMapper.createAlarm(alarm,BaseFactory.getTableName(ProjectCraneAlarm.class));

                    pushService.pushMob("注意",AlarmDTOFactory.factoryMessage(alarm,projectInfo,"塔吊设备"),projectInfo.getUuid());
                }
                ResultDTO redisData =  ResultDTO.factory(alarmInfo,21);
                //放入缓存
                this.push(redisUtil,crane.getDeviceNo(),"crane_alarm",projectInfo.getUuid(),redisData,""+crane.getProjectId());

            }

        }




    }
    
}
/**
* @author: wangyifei
* Description: 插入升降机实时数据
* Date: 17:39 2018/8/23
*/
private void insertLift(String sn, Map<String,Object> cMap){
   String url = "http://0513.safe110.net:9992/App_Ajax/Flash.ashx";
    String query = "PostType=GetElevatorLiveDataCustom&SysOrgAreas=777be33f-0944-e211-95ea-bcaec5395ab5&DeviceSN="+sn;
    Map<String,Object>  map = httpGet(url+"?"+query,cMap);

    JSONObject result =(JSONObject) map.get("jsonResult");
    System.out.println(result.toString());
     JSONArray rows =  (JSONArray) result.get("rows");

    for(int i=0 ; i<rows.size() ; i++){

        ProjectLift  lift = liftService.selectOne(rows.getJSONObject(i).getString("DeviceSN"));



        if(lift != null&&1==lift.getStatus()){
            ProjectInfo projectInfo = projectInfoService.selectById(lift.getProjectId());
            if(projectInfo != null){
                ProjectLiftDetail detail = LiftFactory.factory(lift,rows.getJSONObject(i));
                liftDetailMapper.createDetail(
                        detail,
                        BaseFactory.getTableName(ProjectLiftDetail.class)
                );

                CurrentLiftData currentLiftData = CurrentLiftData.factory(detail,lift);

                this.push(redisUtil,detail.getDeviceNo(),"lift",projectInfo.getUuid(),currentLiftData,""+lift.getProjectId());
                JSONArray  alarms =  rows.getJSONObject(i).getJSONArray("Alarm");
                List<AlarmDTO>   alarmInfo   = new ArrayList<>();
                for( int  j = 0 ; j<alarms.size(); j++){
                    ProjectLiftAlarm  alarm = LiftAlarmFactory.factory(lift,alarms.getJSONObject(j));
                    alarmInfo.add(AlarmDTOFactory.factory(alarm,projectInfo,"升降机设备"));
                    liftAlarmMapper.createAlarm(alarm,BaseFactory.getTableName(ProjectLiftAlarm.class));
                    pushService.pushMob("注意",AlarmDTOFactory.factoryMessage(alarm,projectInfo,"升降机设备"),projectInfo.getUuid());
                }

                ResultDTO redisData = ResultDTO.factory(alarmInfo,22);
                //放入缓存
                this.push(redisUtil, lift.getDeviceNo(), "lift_alarm", projectInfo.getUuid(), redisData,""+lift.getProjectId());

            }

        }
       }
}
    /**
     * 实时数据 推入redis
     * @author:  wangyifei
     * @param redisUtil 工具
     * @param deviceNo  设备编号
     * @param deviceType  设备类型
     * @param uuid   项目编号
     * @param data   数据
     */
    public  void push(RedisUtil redisUtil,String deviceNo,String deviceType,String uuid,Object data,String projectId){
       String redisKey =xywgProerties.getRedisHead()+":current:"+uuid+":"+deviceType+":"+deviceNo;

        String wsTopic = "";
        if(deviceType.endsWith("alarm")){
            //webservice 主题
            wsTopic="/topic/current/"+deviceType+"/"+uuid;

        }else{
            redisUtil.set(redisKey, JSONUtil.toJsonStr(data));
            wsTopic="/topic/current/"+deviceType+"/"+uuid+"/"+deviceNo;
            //增加对大屏的支持
            if(StringUtils.isNotEmpty(projectId)){
                String tempKey  = xywgProerties.getRedisHead()+":current:"+projectId+":"+deviceType+":"+deviceNo;
                String tempTopic="/topic/current/"+deviceType+"/"+projectId+"/"+deviceNo;
                redisUtil.set(tempKey, JSONUtil.toJsonStr(data));
                simpMessageSendingOperations.convertAndSend(tempTopic,data);
            }
        }
        System.out.println("订阅路径："+wsTopic);
        simpMessageSendingOperations.convertAndSend(wsTopic,data);

    }

}
