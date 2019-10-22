package com.xingyun.equipment.plugins.core.callback;

import com.xingyun.equipment.plugins.core.dto.ProtocolDTO;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:52 2019/7/8
 * Modified By : wangyifei
 */
public interface CommandCallback {
        /**
         * 登录回调
         * @param protocolDTO 数据
         * @return 项目ID
         */
        Integer loginCallback(ProtocolDTO protocolDTO);

        /**
         * 登出回调
         * @param sn 设备号
         */
        void logoutCallback(String sn);

        /**
         * 心跳回调
         * @param protocolDTO 数据
         */
        void heartbeatCallback(ProtocolDTO protocolDTO);

        /**
         * 同步时间回调
         * @param protocolDTO 数据
         */
        void lockInCallback(ProtocolDTO protocolDTO);

        /**
         *  监控数据
         * @param protocolDTO 数据
         */
        void enviromentCallback(ProtocolDTO protocolDTO);
        /**
         *  离线监控数据
         * @param protocolDTO 数据
         */
        void offlineEnviromentCallback(ProtocolDTO protocolDTO);
        /**
         *  异常数据回调
         * @param protocolDTO
         */
        void exceptionCallback(ProtocolDTO protocolDTO);

        /**
         * 重启回调
         * @param protocolDTO
         */
        void rebootCallback(ProtocolDTO protocolDTO);

        /**
         * 基本信息
         * @param protocolDTO
         */
        void basicInfoCallback(ProtocolDTO protocolDTO);

        /**
         *  幅度校准回调
         * @param protocolDTO
         */
        void rangeCallback(ProtocolDTO protocolDTO);

        /**
         * 高度校准回调
         * @param protocolDTO
         */
        void heightCallback(ProtocolDTO protocolDTO);

        /**
         * 角度校准
         * @param protocolDTO
         */
        void angleCallback(ProtocolDTO protocolDTO);

        /**
         * 重量校准
         * @param protocolDTO
         */
        void weightCallback(ProtocolDTO protocolDTO);

        /**
         * 单机防碰撞
         * @param protocolDTO
         */
        void singleCallback(ProtocolDTO protocolDTO);

        /**
         * 离线工作循环
         * @param protocolDTO
         */
        void offlineCyclicCallback(ProtocolDTO protocolDTO);

        /**
         * 工作循环
         * @param protocolDTO
         */
        void cyclicCallback(ProtocolDTO protocolDTO);

        /**
         * 工作时长
         * @param protocolDTO
         */
        void workingHoursCallback(ProtocolDTO protocolDTO);

        /**
         * 多机防碰撞
         * @param protocolDTO
         */
        void multiCallback(ProtocolDTO protocolDTO);
}
