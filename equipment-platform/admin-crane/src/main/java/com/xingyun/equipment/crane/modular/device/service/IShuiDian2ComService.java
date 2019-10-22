package com.xingyun.equipment.crane.modular.device.service;

import com.xingyun.equipment.core.dto.ResultDTO;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:56 2018/10/22
 * Modified By : wangyifei
 */
public interface IShuiDian2ComService {


    /**
    * @author: wangyifei
    * Description: 水电表 正常总数 停用总数
     * @param uuid  项目uuid
    * Date: 11:08 2018/10/22
    */
    public ResultDTO shuiDianTotal(String uuid);

    /**
     * @author: wangyifei
     * Description: 获取当月有报警的 水电表数
     * @param uuid  项目uuid
     * Date: 14:12 2018/10/22
     */
    public ResultDTO shuiDianEx(String uuid);

    /**
    * @author: wangyifei
    * Description:  7日用水统计
    * Date: 9:56 2018/10/23
    */
    public ResultDTO  getWaterTotal(String uuid);


    /**
     * @author: wangyifei
     * Description:  7日用电统计
     * Date: 9:56 2018/10/23
     */
    public ResultDTO  getPowerTotal(String uuid);


    /**
    * @author: wangyifei
    * Description: 水 7日用量折线图
     * @param uuid  uuid
     * @param type 类别
    * Date: 15:27 2018/10/22
    */

    public  ResultDTO getWaterLineCharts(String uuid, Integer type);
    /**
     * @author: wangyifei
     * Description: 电 7日用量折线图
     * @param uuid  uuid
     * @param type 类型
     * Date: 15:27 2018/10/22
     */

    public  ResultDTO getPowerLineCharts(String uuid, Integer type);


}
