package com.xingyun.xingyunplugin.demo.impl;


import cn.hutool.json.JSONUtil;
import com.xingyun.equipment.plugins.core.action.CommandAction;
import com.xingyun.equipment.plugins.core.callback.CommandCallback;
import com.xingyun.equipment.plugins.core.dto.ProtocolDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:19 2019/7/8
 * Modified By : wangyifei
 */
@Component
@Slf4j
public class XingyunCallback implements CommandCallback {
    @Autowired
    private CommandAction commandAction;


    @Override
    public Integer loginCallback(ProtocolDTO protocolDTO) {
        System.out.println(JSONUtil.toJsonStr(protocolDTO));

        return 1;
    }

    @Override
    public void logoutCallback(String sn) {
        System.out.println("登出 sn:"+sn);
    }

    @Override
    public void heartbeatCallback(ProtocolDTO protocolDTO) {
        System.out.println(JSONUtil.toJsonStr(protocolDTO));

    }

    @Override
    public void lockInCallback(ProtocolDTO protocolDTO) {
        System.out.println(JSONUtil.toJsonStr(protocolDTO));
    }

    @Override
    public void enviromentCallback(ProtocolDTO protocolDTO) {
        System.out.println(JSONUtil.toJsonStr(protocolDTO));
    }

    @Override
    public void offlineEnviromentCallback(ProtocolDTO protocolDTO) {

    }

    @Override
    public void exceptionCallback(ProtocolDTO protocolDTO) {
        System.out.println(JSONUtil.toJsonStr(protocolDTO));
    }

    @Override
    public void rebootCallback(ProtocolDTO protocolDTO) {
        System.out.println(JSONUtil.toJsonStr(protocolDTO));
    }

    @Override
    public void basicInfoCallback(ProtocolDTO protocolDTO) {
        System.out.println(JSONUtil.toJsonStr(protocolDTO));
    }

    @Override
    public void rangeCallback(ProtocolDTO protocolDTO) {
        System.out.println(JSONUtil.toJsonStr(protocolDTO));
    }

    @Override
    public void heightCallback(ProtocolDTO protocolDTO) {
        System.out.println(JSONUtil.toJsonStr(protocolDTO));
    }

    @Override
    public void angleCallback(ProtocolDTO protocolDTO) {
        System.out.println(JSONUtil.toJsonStr(protocolDTO));
    }

    @Override
    public void weightCallback(ProtocolDTO protocolDTO) {
        System.out.println(JSONUtil.toJsonStr(protocolDTO));
    }

    @Override
    public void singleCallback(ProtocolDTO protocolDTO) {
        System.out.println(JSONUtil.toJsonStr(protocolDTO));
    }

    @Override
    public void offlineCyclicCallback(ProtocolDTO protocolDTO) {
        System.out.println(JSONUtil.toJsonStr(protocolDTO));
    }

    @Override
    public void cyclicCallback(ProtocolDTO protocolDTO) {
        System.out.println(JSONUtil.toJsonStr(protocolDTO));
    }

    @Override
    public void workingHoursCallback(ProtocolDTO protocolDTO) {
        System.out.println(JSONUtil.toJsonStr(protocolDTO));
    }

    @Override
    public void multiCallback(ProtocolDTO protocolDTO) {
        System.out.println(JSONUtil.toJsonStr(protocolDTO));
    }
}
