package com.xywg.equipmentmonitor.modular.station.vo;

import com.xywg.equipmentmonitor.modular.device.model.ProjectHelmetPositionDetail;
import com.xywg.equipmentmonitor.modular.station.dto.Coordinate;
import com.xywg.equipmentmonitor.modular.station.dto.GpsCoordinate;
import com.xywg.equipmentmonitor.modular.station.model.ProjectAccuratePositionData;
import com.xywg.equipmentmonitor.modular.station.model.ProjectMap;
import com.xywg.equipmentmonitor.modular.station.utils.DistanceUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:24 2019/3/21
 * Modified By : wangyifei
 */
@Data
public class PostionVO {

    private BigDecimal xZhou;
    private BigDecimal yZhou;
    private Date collectTime;


    public static PostionVO convert(ProjectAccuratePositionData positionData){

        PostionVO postionVO  =new PostionVO();
        postionVO.setXZhou(positionData.getXZhou());
        postionVO.setYZhou(positionData.getYZhou());
        postionVO.setCollectTime(positionData.getCollectTime());
        return postionVO;
    }
    public static PostionVO convert(GpsCoordinate o, ProjectHelmetPositionDetail data){

        PostionVO postionVO  =new PostionVO();
        double weidu  =   data.getBdLat().doubleValue();
        double jingdu =   data.getBdLng().doubleValue();
        Coordinate coordinate = DistanceUtil.toCoordinate(o,new GpsCoordinate(jingdu,weidu));
        postionVO.setXZhou(new BigDecimal(coordinate.getX()));
        postionVO.setYZhou(new BigDecimal(coordinate.getY()));
        postionVO.setCollectTime(data.getCollectTime());
        return postionVO;
    }


}
