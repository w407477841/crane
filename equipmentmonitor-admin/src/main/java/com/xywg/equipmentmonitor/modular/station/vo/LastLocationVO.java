package com.xywg.equipmentmonitor.modular.station.vo;

import com.xywg.equipmentmonitor.modular.device.model.ProjectHelmetPositionDetail;
import com.xywg.equipmentmonitor.modular.station.dto.Coordinate;
import com.xywg.equipmentmonitor.modular.station.dto.GpsCoordinate;
import com.xywg.equipmentmonitor.modular.station.model.ProjectAccuratePositionData;
import com.xywg.equipmentmonitor.modular.station.utils.DistanceUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:23 2019/4/1
 * Modified By : wangyifei
 */
@Data
public class LastLocationVO {

       private BigDecimal xZhou;
       private BigDecimal yZhou;
       private String identityCode;
       private String name;


       public static LastLocationVO convert(ProjectAccuratePositionData data){
           LastLocationVO locationVO = new LastLocationVO();
           locationVO.setIdentityCode(data.getIdentityCode());
           locationVO.setXZhou(data.getXZhou());
           locationVO.setYZhou(data.getYZhou());
           locationVO.setName(data.getName());
           return locationVO;
       }

    public static LastLocationVO convert(GpsCoordinate o, ProjectHelmetPositionDetail data){
        LastLocationVO locationVO = new LastLocationVO();
        locationVO.setIdentityCode(data.getIdCardNumber());

        double weidu  =   data.getBdLat().doubleValue();
        double jingdu =   data.getBdLng().doubleValue();

        Coordinate coordinate = DistanceUtil.toCoordinate(o,new GpsCoordinate(jingdu,weidu));

        locationVO.setXZhou(new BigDecimal(coordinate.getX()));
        locationVO.setYZhou(new BigDecimal(coordinate.getY()));
        return locationVO;
    }

}
