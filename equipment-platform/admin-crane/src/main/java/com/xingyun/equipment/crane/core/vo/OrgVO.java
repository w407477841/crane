package com.xingyun.equipment.crane.core.vo;

import com.xingyun.equipment.crane.core.util.TreeI;
import com.xingyun.equipment.crane.core.util.TreeObject;

import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:51 2018/9/4
 * Modified By : wangyifei
 */
public class OrgVO   implements TreeI {

  private  Integer id;
  private  Integer pid;
    private  List<? extends  TreeI> children;
    private  String code;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    @Override
    public List<? extends TreeI> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<? extends TreeI> children) {
        this.children = children;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    private String pcode;

}
