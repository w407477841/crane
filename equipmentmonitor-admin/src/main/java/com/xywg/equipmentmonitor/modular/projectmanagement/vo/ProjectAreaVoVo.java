package com.xywg.equipmentmonitor.modular.projectmanagement.vo;

import com.xywg.equipmentmonitor.core.util.TreeI;
import lombok.Data;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2018/9/8
 *TIME:17:21
 */
@Data
public class ProjectAreaVoVo implements TreeI {
    /**
     * id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     *
     */
    private Integer text;

    /**
     * pid
     */
    private Integer parentId;

    /**
     * 区域编码
     */
    private Integer areaCode;

    /**
     * 子集
     */
    private List<? extends TreeI> children;

    @Override
    public Integer getPid() {
        return this.getParentId();
    }

    @Override
    public void setChildren(List<? extends TreeI> children) {
        this.children = children;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getPcode() {
        return null;
    }
}
