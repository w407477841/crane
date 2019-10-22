package com.xywg.equipmentmonitor.modular.station.vo;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 20:27 2019/3/22
 * Modified By : wangyifei
 */
@Data
public class FloorVO {

        private Integer id;
        private String name;
        private Integer status;


        public FloorVO(Integer id, String name,Integer status) {
                this.id = id;
                this.name = name;
                this.status =status ;
        }
}
