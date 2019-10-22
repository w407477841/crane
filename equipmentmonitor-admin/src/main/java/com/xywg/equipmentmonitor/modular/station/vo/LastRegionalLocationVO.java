package com.xywg.equipmentmonitor.modular.station.vo;

import com.xywg.equipmentmonitor.modular.station.model.ProjectRegionalPositionData;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:41 2019/7/4
 * Modified By : wangyifei
 */
@Data
public class LastRegionalLocationVO {
    private Long total;
    private List<WorkVO> works;


    public static Map<String,LastRegionalLocationVO> regionalLocationVOMap(List<ProjectRegionalPositionData> regionalPositionData){
        Map<String,LastRegionalLocationVO> map = new HashMap<>(128);
        LastRegionalLocationVO vo;
        for(ProjectRegionalPositionData item:regionalPositionData){
            if(map.containsKey(item.getStationNo())){
                vo = map.get(item.getStationNo());
            }else{
                vo =new LastRegionalLocationVO();
                vo.setTotal(0L);
                vo.setWorks(new ArrayList<>());
            }
            vo.setTotal(vo.getTotal()+1);
            WorkVO workVO=new WorkVO();
            workVO.setName(item.getName());
            workVO.setIdentityCode(item.getIdentityCode());
            vo.getWorks().add(workVO);
            map.put(item.getStationNo(),vo);
        }

        return map;
    }

}
