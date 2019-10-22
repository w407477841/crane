package com.xywg.attendance.modular.handler.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.attendance.common.global.GlobalStaticConstant;
import com.xywg.attendance.common.model.ResultDTO;
import com.xywg.attendance.common.utils.DataUtil;
import com.xywg.attendance.common.utils.RedisUtil;
import com.xywg.attendance.modular.command.model.AmInf;
import com.xywg.attendance.modular.command.model.BussDeviceCommand;
import com.xywg.attendance.modular.command.model.PerInf;
import com.xywg.attendance.modular.command.service.IAmInfService;
import com.xywg.attendance.modular.command.service.IBussDeviceCommandService;
import com.xywg.attendance.modular.command.service.IPerInfService;
import com.xywg.attendance.modular.handler.model.Command;
import com.xywg.attendance.modular.handler.model.CommandTypeEnum;
import com.xywg.attendance.modular.handler.model.WorkerInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * @author hjy
 * @date 2019/4/16
 */
@Service
public class CommandService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IBussDeviceCommandService deviceCommandService;
    @Autowired
    private IAmInfService amInfService;
    @Autowired
    private IPerInfService perInfService;

    /**
     * 下发指令
     */
    public ResultDTO issued(Command command) {

        if (command.getDeviceSnList().size() == 0 || command.getCommandType() == null) {
            return ResultDTO.resultError("设备编号和命令类型必需存在", null);
        }
        CommandTypeEnum typeEnum = CommandTypeEnum.getMethodName(command.getCommandType());
        if (typeEnum == null) {
            return ResultDTO.resultError("命令类型不存在", null);
        }
        switch (typeEnum) {
            case REBOOT:
                reboot(command);
                break;
            case CLEAR_ATTENDANCE_RECORD:
                clearAttendanceRecord(command);
                break;
            case DELETE_ALL_PERSON:
                deleteAllPerson(command);
                break;
            case DISPATCH_USER:
                dispatchUser(command);
                break;
            case DELETE_SEVERAL_PERSON:
                deleteSeveralPerson(command);
                break;
            default:
                break;
        }

        return ResultDTO.resultSuccess("OK", null);
    }

    /**
     * 重启
     *
     * @param command
     */
    private void reboot(Command command) {
        List<String> deviceList = command.getDeviceSnList();
        List<BussDeviceCommand> commandToDbList = new ArrayList<>();

        Map<String, List<String>> toRedisMap = new HashMap<>();
        for (String sn : deviceList) {
            String uuid = DataUtil.getUUID();
            StringBuilder cmdString = new StringBuilder();
            cmdString.append("C:").append(uuid).append(":REBOOT").append("\n");
            //存入redis
            String key = GlobalStaticConstant.DEVICE_GET_COMMAND_REDIS_KEY + sn;
            Object o = redisUtil.get(key);
            List<String> commandList;
            if (o == null) {
                commandList = new ArrayList<>();
            } else {
                commandList = (List<String>) o;
            }
            commandList.add(cmdString.toString());
            //redisUtil.set(key, commandList);
            toRedisMap.put(key, commandList);
            BussDeviceCommand deviceCommand = new BussDeviceCommand(
                    sn, command.getCommandType(), 0, null, cmdString.toString(),
                    new Date(), 0, uuid
            );
            commandToDbList.add(deviceCommand);
        }
        //必须数据先入库后再把命令放入redis
        if (commandToDbList.size() > 0) {
            deviceCommandService.insertBatchSqlServer(commandToDbList);
        }

        for (Map.Entry<String, List<String>> entry : toRedisMap.entrySet()) {
            redisUtil.set(entry.getKey(), entry.getValue());
        }

    }

    /**
     * 清楚全部数据
     *
     * @param command
     */
    private void clearAttendanceRecord(Command command) {
        List<String> deviceList = command.getDeviceSnList();
        List<BussDeviceCommand> commandToDbList = new ArrayList<>();

        Map<String, List<String>> toRedisMap = new HashMap<>();
        for (String sn : deviceList) {
            String uuid = DataUtil.getUUID();
            StringBuilder cmdString = new StringBuilder();
            cmdString.append("C:").append(uuid).append(":CLEAR ").append("DATA").append("\n");
            //存入redis
            String key = GlobalStaticConstant.DEVICE_GET_COMMAND_REDIS_KEY + sn;
            Object o = redisUtil.get(key);
            List<String> commandList;
            if (o == null) {
                commandList = new ArrayList<>();
            } else {
                commandList = (List<String>) o;
            }
            commandList.add(cmdString.toString());
            //redisUtil.set(key, commandList);
            toRedisMap.put(key, commandList);
            BussDeviceCommand deviceCommand = new BussDeviceCommand(
                    sn, command.getCommandType(), 0, null, cmdString.toString(),
                    new Date(), 0, uuid
            );
            commandToDbList.add(deviceCommand);
        }
        if (commandToDbList.size() > 0) {
            deviceCommandService.insertBatchSqlServer(commandToDbList);
        }
        //必须数据先入库后再把命令放入redis
        for (Map.Entry<String, List<String>> entry : toRedisMap.entrySet()) {
            redisUtil.set(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 删除该设备当前所属当前项目下所有人员
     */
    private void deleteAllPerson(Command command) {
        //根据设备编号查询项目编号
        List<String> deviceList = command.getDeviceSnList();
        List<BussDeviceCommand> commandToDbList = new ArrayList<>();
        Map<String, List<String>> toRedisMap = new HashMap<>();
        for (String deviceSn : deviceList) {
            String key = GlobalStaticConstant.DEVICE_GET_COMMAND_REDIS_KEY + deviceSn;
            Object o = redisUtil.get(key);
            if (o == null) {
                o = new ArrayList<>();
            }
            List<String> commandList = (List<String>) o;

            Wrapper<AmInf> wrapper = new EntityWrapper<>();
            wrapper.eq("AM_Code", deviceSn);
            AmInf amInf = amInfService.selectOne(wrapper);
            if (amInf == null || amInf.getProjectCode() == null) {
                continue;
            }
            Wrapper<PerInf> perWp = new EntityWrapper<>();
            perWp.eq("Project_Code", amInf.getProjectCode());
            //查询该项目下所有人员
            List<PerInf> perInfList = perInfService.selectList(perWp);

            StringBuilder cmdString = new StringBuilder();
            for (PerInf worker : perInfList) {
                String uuid = DataUtil.getUUID();
                cmdString.append("C:").append(uuid).append(":DATA ").append("DELETE ").append("USERINFO ");
                cmdString.append("PIN=").append(worker.getIdNo()).append("\n");
                BussDeviceCommand deviceCommand = new BussDeviceCommand(
                        deviceSn, command.getCommandType(), 0, null, cmdString.toString(),
                        new Date(), 0, uuid
                );
                commandToDbList.add(deviceCommand);
                commandList.add(cmdString.toString());
            }

            //redisUtil.set(key, commandList);
            toRedisMap.put(key, commandList);
        }
        if (commandToDbList.size() > 0) {
            deviceCommandService.insertBatchSqlServer(commandToDbList);
        }
        //必须数据先入库后再把命令放入redis
        for (Map.Entry<String, List<String>> entry : toRedisMap.entrySet()) {
            redisUtil.set(entry.getKey(), entry.getValue());
        }

    }

    /**
     * 下发人员信息
     *
     * @param command
     */
    private void dispatchUser(Command command) {
        if (command.getWorkerInfoList() == null || command.getWorkerInfoList().size() == 0) {
            return;
        }

        //根据设备编号查询项目编号
        List<String> deviceList = command.getDeviceSnList();
        List<WorkerInfo> workerInfoList = command.getWorkerInfoList();

        List<BussDeviceCommand> commandToDbList = new ArrayList<>();
        Map<String, List<String>> toRedisMap = new HashMap<>();
        for (String deviceSn : deviceList) {
            String key = GlobalStaticConstant.DEVICE_GET_COMMAND_REDIS_KEY + deviceSn;
            Object o = redisUtil.get(key);
            if (o == null) {
                o = new ArrayList<>();
            }
            List<String> commandList = (List<String>) o;

            for (WorkerInfo worker : workerInfoList) {

                String uuid1 = DataUtil.getUUID();
                StringBuilder userInfoBuilder = new StringBuilder();
                StringBuilder userFaceBuilder = new StringBuilder();
                StringBuilder userPhototBuilder = new StringBuilder();

                //用户信息命令
                userInfoBuilder.append("C:").append(uuid1).append(":DATA UPDATE USERINFO PIN=")
                        .append(worker.getIdCardNumber())
                        .append("\tName=").append(worker.getName()).append("\tPri=").append("0").append("\tPasswd=")
                        .append("\tCard=").append(worker.getIdCardNumber())
                        .append("\tGrp=").append("1").append("\tTZ=").append("0000000000000000")
                        .append("\tVerify=").append("\tViceCard=").append("\n");

                BussDeviceCommand deviceCommand = new BussDeviceCommand(
                        deviceSn, command.getCommandType(), 0, null, userInfoBuilder.toString(),
                        new Date(), 0, uuid1
                );
                commandToDbList.add(deviceCommand);
                commandList.add(userInfoBuilder.toString());

                if (StringUtils.isNotBlank(worker.getFaceTemplateUrl())) {
                    String uuid2 = DataUtil.getUUID();
                    String base64 = worker.getFaceTemplateUrl();
                    //用户人脸模板
                    userFaceBuilder.append("C:").append(uuid2).append(":DATA UPDATE BIOPHOTO PIN=")
                            .append(worker.getIdCardNumber()).append("\tType=").append(0).append("\tSize=")
                            .append(base64.length()).append("\tContent=").append(base64)
                            .append("\tFormat=").append(0).append("\tUrl=").append(worker.getFaceTemplateUrl()).append("\n");
                    BussDeviceCommand deviceCommand2 = new BussDeviceCommand(
                            deviceSn, command.getCommandType(), 0, null, userFaceBuilder.toString(),
                            new Date(), 0, uuid2
                    );
                    commandToDbList.add(deviceCommand2);
                    commandList.add(userFaceBuilder.toString());
                }

                if (StringUtils.isNotBlank(worker.getUserPhotosUrl())) {
                    String uuid3 = DataUtil.getUUID();
                    String base64 = worker.getUserPhotosUrl();
                    //用户照片命令(大头照)
                    userPhototBuilder.append("C:").append(uuid3).append(":DATA UPDATE USERPIC PIN=")
                            .append(worker.getIdCardNumber())
                            .append("\tSize=").append(base64.length()).append("\t").append("Content=")
                            .append(base64).append("\n");

                    BussDeviceCommand deviceCommand3 = new BussDeviceCommand(
                            deviceSn, command.getCommandType(), 0, null, userPhototBuilder.toString(),
                            new Date(), 0, uuid3
                    );
                    commandToDbList.add(deviceCommand3);
                    commandList.add(userPhototBuilder.toString());
                }


            }
            //redisUtil.set(key, commandList);
            toRedisMap.put(key, commandList);
        }

        if (commandToDbList.size() > 0) {
            deviceCommandService.insertBatchSqlServer(commandToDbList);
        }
        //必须数据先入库后再把命令放入redis
        for (Map.Entry<String, List<String>> entry : toRedisMap.entrySet()) {
            redisUtil.set(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 删除个别人员
     *
     * @param command
     */
    private void deleteSeveralPerson(Command command) {
        if (command.getWorkerInfoList() == null || command.getWorkerInfoList().size() == 0) {
            return;
        }
        //根据设备编号查询项目编号
        List<String> deviceList = command.getDeviceSnList();
        List<WorkerInfo> workerInfoList = command.getWorkerInfoList();
        List<BussDeviceCommand> commandToDbList = new ArrayList<>();
        Map<String, List<String>> toRedisMap = new HashMap<>();
        for (String deviceSn : deviceList) {
            String key = GlobalStaticConstant.DEVICE_GET_COMMAND_REDIS_KEY + deviceSn;
            Object o = redisUtil.get(key);
            if (o == null) {
                o = new ArrayList<>();
            }
            List<String> commandList = (List<String>) o;

            StringBuilder cmdString = new StringBuilder();
            for (WorkerInfo worker : workerInfoList) {
                String uuid = DataUtil.getUUID();
                cmdString.append("C:").append(uuid).append(":DATA ").append("DELETE ").append("USERINFO ");
                cmdString.append("PIN=").append(worker.getIdCardNumber()).append("\n");
                BussDeviceCommand deviceCommand = new BussDeviceCommand(
                        deviceSn, command.getCommandType(), 0, null, cmdString.toString(),
                        new Date(), 0, uuid
                );
                commandToDbList.add(deviceCommand);
                commandList.add(cmdString.toString());
            }
            //redisUtil.set(key, commandList);
            toRedisMap.put(key, commandList);
        }

        if (commandToDbList.size() > 0) {
            deviceCommandService.insertBatchSqlServer(commandToDbList);
        }


        //必须数据先入库后再把命令放入redis
        for (Map.Entry<String, List<String>> entry : toRedisMap.entrySet()) {
            redisUtil.set(entry.getKey(), entry.getValue());
        }


    }


}
