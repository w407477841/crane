package com.xingyun.equipment.system.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.xingyun.equipment.system.model.Operation;
import lombok.Data;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2018/9/6
 *TIME:11:22
 */
@Data
public class OperationVo extends Operation {
    /**
     * 子级集合
     */
    @TableField(exist = false)
    List<OperationVo> children;
}
