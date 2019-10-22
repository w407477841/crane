package com.xywg.iot.modules.netty.handler;

import com.xywg.iot.common.utils.DataUtil;
import com.xywg.iot.modules.crane.model.ProjectCraneAlarm;
import com.xywg.iot.modules.crane.model.ProjectCraneDetail;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xywg.iot.common.global.GlobalStaticConstant.*;

/**
 * @author hjy
 * @date 2018/12/29
 * 处理报警转换类
 */
@Service
public class HandleWarningMessage {

    /**
     * 解析报警信息
     *
     * @param data
     * @param detail
     * @return
     */
    public Map<String, Object> getHandleWarningMessage(byte[] data, ProjectCraneDetail detail) {
        List<ProjectCraneAlarm> toDbList = new ArrayList<>();
        StringBuffer sbf = new StringBuffer();
        //高度限位 (二进制字符串)
        String heightWarning = DataUtil.byteToBit(data[31]);
        List<Object> heightWarningList = getHeightWarning(heightWarning, detail);
        toDbList.add((ProjectCraneAlarm) heightWarningList.get(0));
        sbf.append(heightWarningList.get(1));

        //幅度回转限位(二进制字符串)
        String rangeWarning = DataUtil.byteToBit(data[32]);
        List<Object> rangeWarningList = getRangeWarning(rangeWarning, detail);
        toDbList.add((ProjectCraneAlarm) rangeWarningList.get(0));
        sbf.append(rangeWarningList.get(1));
        toDbList.add((ProjectCraneAlarm) rangeWarningList.get(2));
        sbf.append(rangeWarningList.get(3));

        //载重限制(二进制字符串)
        String weightWarning = DataUtil.byteToBit(data[33]);
        List<Object> weightWarningList = getWeightWarning(weightWarning, detail);
        toDbList.add((ProjectCraneAlarm) weightWarningList.get(0));
        sbf.append(weightWarningList.get(1));

        //风速倾斜限制(二进制字符串)
        String windSpeedWarning = DataUtil.byteToBit(data[34]);
        List<Object> windSpeedList = getWindSpeedWarning(windSpeedWarning, detail);
        toDbList.add((ProjectCraneAlarm) windSpeedList.get(0));
        sbf.append(windSpeedList.get(1));
        toDbList.add((ProjectCraneAlarm) windSpeedList.get(2));
        sbf.append(windSpeedList.get(3));

        //干涉上下限位(二进制字符串)
        String interveneUpDownLimit = DataUtil.byteToBit(data[35]);
        List<Object> interveneUpDownLimitList = getWarningList(interveneUpDownLimit, detail, "干涉下限位", "干涉上限位");
        toDbList.add((ProjectCraneAlarm) interveneUpDownLimitList.get(0));
        sbf.append(interveneUpDownLimitList.get(1));
        toDbList.add((ProjectCraneAlarm) interveneUpDownLimitList.get(2));
        sbf.append(interveneUpDownLimitList.get(3));


        //干涉前后限位(二进制字符串)
        String interveneFrontBackLimit = DataUtil.byteToBit(data[36]);
        List<Object> interveneFrontBackLimitList = getWarningList(interveneFrontBackLimit, detail, "干涉后限位", "干涉前限位");
        toDbList.add((ProjectCraneAlarm) interveneFrontBackLimitList.get(0));
        sbf.append(interveneFrontBackLimitList.get(1));
        toDbList.add((ProjectCraneAlarm) interveneFrontBackLimitList.get(2));
        sbf.append(interveneFrontBackLimitList.get(3));


        //干涉左右限位(二进制字符串)
        String interveneLeftRightLimit = DataUtil.byteToBit(data[37]);
        List<Object> interveneLeftRightLimitList = getWarningList(interveneLeftRightLimit, detail, "干涉右限位", "干涉左限位");
        toDbList.add((ProjectCraneAlarm) interveneLeftRightLimitList.get(0));
        sbf.append(interveneLeftRightLimitList.get(1));
        toDbList.add((ProjectCraneAlarm) interveneLeftRightLimitList.get(2));
        sbf.append(interveneLeftRightLimitList.get(3));


        //碰撞上下限位(二进制字符串)
        String collideUpDownLimit = DataUtil.byteToBit(data[38]);
        List<Object> collideUpDownLimitList = getWarningList(collideUpDownLimit, detail, "碰撞下限位", "碰撞上限位");
        toDbList.add((ProjectCraneAlarm) collideUpDownLimitList.get(0));
        sbf.append(collideUpDownLimitList.get(1));
        toDbList.add((ProjectCraneAlarm) collideUpDownLimitList.get(2));
        sbf.append(collideUpDownLimitList.get(3));


        //碰撞前后限位(二进制字符串)
        String collideFrontBackLimit = DataUtil.byteToBit(data[39]);
        List<Object> collideFrontBackLimitList = getWarningList(collideFrontBackLimit, detail, "碰撞后限位", "碰撞前限位");
        toDbList.add((ProjectCraneAlarm) collideFrontBackLimitList.get(0));
        sbf.append(collideFrontBackLimitList.get(1));
        toDbList.add((ProjectCraneAlarm) collideFrontBackLimitList.get(2));
        sbf.append(collideFrontBackLimitList.get(3));


        //碰撞左右限位(二进制字符串)
        String collideLeftRightLimit = DataUtil.byteToBit(data[40]);
        List<Object> collideLeftRightLimitList = getWarningList(collideLeftRightLimit, detail, "碰撞右限位", "碰撞左限位");
        toDbList.add((ProjectCraneAlarm) collideLeftRightLimitList.get(0));
        sbf.append(collideLeftRightLimitList.get(1));
        toDbList.add((ProjectCraneAlarm) collideLeftRightLimitList.get(2));
        sbf.append(collideLeftRightLimitList.get(3));

        //继电器状态(二进制字符串)
        String relayStatus = DataUtil.byteToBit(data[41]);
        String relayStatusStr = handleRelayStatus(relayStatus);
        sbf.append(relayStatusStr);

        //工作状态 1工作  2,空闲
        String workStatus = DataUtil.byteToHex(data[42]);
        if (Integer.parseInt(workStatus, 16) == 1) {
            sbf.append(" 工作状态:工作,");
        } else {
            sbf.append(" 工作状态:空闲,");
        }

        //传感器状态(二进制字符串)
        String sensorStatus = DataUtil.byteToBit(data[43]);
        String sensorStatusStr =sensorStatusStatus(sensorStatus);
        sbf.append(sensorStatusStr);

        Map<String, Object> returnMap = new HashMap<>(2);
        returnMap.put("list", toDbList);
        returnMap.put("message", sbf.toString());
        return returnMap;
    }

    /**
     * 继电器状态 制动状态
     *
     * @return
     */
    private String handleRelayStatus(String relayStatus) {
        StringBuffer sbd = new StringBuffer(" 继电器制动状态:");
        //上行制动  Up, Down, Left,Right  front  back
        String up = relayStatus.substring(relayStatus.length() - 1, relayStatus.length());
        if ("0".equals(up)) {
            sbd.append("上行未制动 ");
        } else {
            sbd.append("上行制动 ");
        }
        String down = relayStatus.substring(relayStatus.length() - 2, relayStatus.length() - 1);
        if ("0".equals(down)) {
            sbd.append("下行未制动 ");
        } else {
            sbd.append("下行制动 ");
        }
        String front = relayStatus.substring(relayStatus.length() - 3, relayStatus.length() - 2);
        if ("0".equals(front)) {
            sbd.append("前行未制动 ");
        } else {
            sbd.append("前行制动 ");
        }
        String back = relayStatus.substring(relayStatus.length() - 4, relayStatus.length() - 3);
        if ("0".equals(back)) {
            sbd.append("后行未制动 ");
        } else {
            sbd.append("后行制动 ");
        }
        String left = relayStatus.substring(relayStatus.length() - 5, relayStatus.length() - 4);
        if ("0".equals(left)) {
            sbd.append("左行未制动 ");
        } else {
            sbd.append("左行制动 ");
        }
        String right = relayStatus.substring(relayStatus.length() - 6, relayStatus.length() - 5);
        if ("0".equals(right)) {
            sbd.append("右行未制动 ");
        } else {
            sbd.append("右行制动 ");
        }
        return sbd.append(",").toString();
    }


    /**
     * 传感器状态
     *
     * @return
     */
    private String sensorStatusStatus(String sensorStatus) {
        StringBuffer sbf = new StringBuffer(" 传感器状态:");
        //高度 幅度 回转 重量  风速 倾斜
        String height = sensorStatus.substring(sensorStatus.length() - 1, sensorStatus.length());
        if ("0".equals(height)) {
            sbf.append("高度传感器未连接 ");
        }
        String range = sensorStatus.substring(sensorStatus.length() - 2, sensorStatus.length() - 1);
        if ("0".equals(range)) {
            sbf.append("幅度传感器未连接 ");
        }
        String rotation = sensorStatus.substring(sensorStatus.length() - 3, sensorStatus.length() - 2);
        if ("0".equals(rotation)) {
            sbf.append("回转传感器未连接 ");
        }
        String weight = sensorStatus.substring(sensorStatus.length() - 4, sensorStatus.length() - 3);
        if ("0".equals(weight)) {
            sbf.append("重量传感器未连接 ");
        }
        String windSpeed = sensorStatus.substring(sensorStatus.length() - 5, sensorStatus.length() - 4);
        if ("0".equals(windSpeed)) {
            sbf.append("风速传感器未连接 ");
        }
        String incline = sensorStatus.substring(sensorStatus.length() - 6, sensorStatus.length() - 5);
        if ("0".equals(incline)) {
            sbf.append("倾斜传感器未连接 ");
        }
        return sbf.append(",").toString();
    }


    /**
     * 通用预警报警处理
     *
     * @param warning
     * @param detail
     * @return
     */
    @SuppressWarnings("all")
    private List<Object> getWarningList(String warning, ProjectCraneDetail detail, String high, String low) {
        List<Object> list = new ArrayList<>();
        String rangeWarningHigh = warning.substring(0, 4);
        String rangeWarningLow = warning.substring(4);

        //高位
        if (COMPARE_FLAG_0001.equals(rangeWarningHigh)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), high + "预警,", 99);
            list.add(alarm);
            list.add(high + "预警,");
            //高度限位报警
        } else if (COMPARE_FLAG_0002.equals(rangeWarningHigh)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), high + "报警,", 99);
            list.add(alarm);
            list.add(high + "报警,");
        }

        //低位
        if (COMPARE_FLAG_0001.equals(rangeWarningLow)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), low + "预警,", 99);
            list.add(alarm);
            list.add(low + "预警,");
            //高度限位报警
        } else if (COMPARE_FLAG_0002.equals(rangeWarningLow)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), low + "报警,", 99);
            list.add(alarm);
            list.add(low + "报警,");
        }
        return list;
    }


    /**
     * 风速倾斜限制
     *
     * @param warning
     * @param detail
     * @return
     */
    private List<Object> getWindSpeedWarning(String warning, ProjectCraneDetail detail) {
        List<Object> list = new ArrayList<>();
        String rangeWarningHigh = warning.substring(0, 4);
        String rangeWarningLow = warning.substring(4);

        //高位
        if (COMPARE_FLAG_0001.equals(rangeWarningHigh)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), "倾斜预警,", 99);
            list.add(alarm);
            list.add("幅度左限位预警,");
            //高度限位报警
        } else if (COMPARE_FLAG_0002.equals(rangeWarningHigh)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), "倾斜报警,", 16);
            list.add(alarm);
            list.add("倾斜报警,");
        }

        //低位
        if (COMPARE_FLAG_0001.equals(rangeWarningLow)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), "风速预警,", 99);
            list.add(alarm);
            list.add("风速预警,");
            //高度限位报警
        } else if (COMPARE_FLAG_0002.equals(rangeWarningLow)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), "风速报警,", 15);
            list.add(alarm);
            list.add("风速报警,");
        }
        return list;
    }

    /**
     * 高度
     *
     * @param heightWarning
     * @param detail
     * @return
     */
    private List<Object> getHeightWarning(String heightWarning, ProjectCraneDetail detail) {
        List<Object> list = new ArrayList<>();
        //高度报警处理:
        String heightWarningFlag = heightWarning.substring(heightWarning.length() - 4, heightWarning.length());
        //高度限位预警
        if (COMPARE_FLAG_0001.equals(heightWarningFlag)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), "高度限位预警,", 8);
            list.add(alarm);
            list.add("高度限位预警,");
            //高度限位报警
        } else if (COMPARE_FLAG_0002.equals(heightWarningFlag)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), "高度限位报警,", 7);
            list.add(alarm);
            list.add("高度限位报警,");
        }
        return list;
    }

    /**
     * 载重限制
     *
     * @param warning
     * @param detail
     * @return
     */
    private List<Object> getWeightWarning(String warning, ProjectCraneDetail detail) {
        List<Object> list = new ArrayList<>();
        //载重限制报警处理:
        String weightWarningFlag = warning.substring(warning.length() - 4, warning.length());
        //载重预警
        if (COMPARE_FLAG_0001.equals(weightWarningFlag)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), "载重预警,", 2);
            list.add(alarm);
            list.add("载重预警,");
            //载重报警
        } else if (COMPARE_FLAG_0002.equals(weightWarningFlag)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), "载重报警,", 1);
            list.add(alarm);
            list.add("载重报警,");
        } else if (COMPARE_FLAG_0003.equals(weightWarningFlag)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), "载重违章,", 99);
            list.add(alarm);
            list.add("载重违章,");
        }
        return list;
    }

    /**
     * 幅度回转限位
     *
     * @param warning
     * @param detail
     * @return
     */
    private List<Object> getRangeWarning(String warning, ProjectCraneDetail detail) {
        List<Object> list = new ArrayList<>();
        String rangeWarningHigh = warning.substring(0, 4);
        String rangeWarningLow = warning.substring(4);

        //高位
        if (COMPARE_FLAG_0001.equals(rangeWarningHigh)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), "幅度左限位预警,", 99);
            list.add(alarm);
            list.add("幅度左限位预警,");
            //高度限位报警
        } else if (COMPARE_FLAG_0002.equals(rangeWarningHigh)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), "幅度左限位报警,", 99);
            list.add(alarm);
            list.add("幅度左限位报警,");
        } else if (COMPARE_FLAG_0003.equals(rangeWarningHigh)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), "幅度右限位预警,", 99);
            list.add(alarm);
            list.add("幅度右限位预警,");
        } else if (COMPARE_FLAG_0004.equals(rangeWarningHigh)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), "幅度右限位报警,", 99);
            list.add(alarm);
            list.add("幅度右限位报警,");
        }

        //低位
        if (COMPARE_FLAG_0001.equals(rangeWarningLow)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), "幅度近端限位预警,", 99);
            list.add(alarm);
            list.add("幅度近端限位预警,");
            //高度限位报警
        } else if (COMPARE_FLAG_0002.equals(rangeWarningLow)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), "幅度近端限位报警,", 99);
            list.add(alarm);
            list.add("幅度近端限位报警,");
        } else if (COMPARE_FLAG_0003.equals(rangeWarningLow)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), "幅度远端限位预警,", 99);
            list.add(alarm);
            list.add("幅度远端限位预警,");
        } else if (COMPARE_FLAG_0004.equals(rangeWarningLow)) {
            ProjectCraneAlarm alarm = new ProjectCraneAlarm(detail.getCraneId(), detail.getId(), detail.getDeviceNo(), "幅度远端限位报警,", 99);
            list.add(alarm);
            list.add("幅度远端限位报警,");
        }
        return list;
    }

}
