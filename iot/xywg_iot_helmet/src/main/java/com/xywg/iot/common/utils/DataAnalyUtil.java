package com.xywg.iot.common.utils;


import com.xywg.iot.modules.helmet.model.DataDomain;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

/**
 * @author chupp
 * @description 报文解析类
 * @date 2018年9月19日
 */
public class DataAnalyUtil {

    public static int getCommand(byte[] data) {
        return data[0];
    }

    public static DataDomain getDataDomain(byte[] data) {
        DataDomain dataDomain = new DataDomain();
        if (getCommand(data) == 0x10) {
            dataDomain.setImei(DataUtil.byteArrayToString(DataUtil.subByteArray(data, 16, 15)));
        } else if (getCommand(data) == 0x30) {
            int needIndex = (data[2] & 0xFF) * 256 + (data[3] & 0xFF) + 4;
            if (data[needIndex] == 0x07) {
                dataDomain.setPositionHealthCommand(0x07);
                byte[] thirdData = DataUtil.subByteArray(data, needIndex, data.length - needIndex);
                analysisPositon(thirdData, dataDomain);
            } else if (data[needIndex] == 0x0f) {
                dataDomain.setPositionHealthCommand(0x0f);
                byte[] thirdData = DataUtil.subByteArray(data, needIndex + 1, data.length - needIndex - 1);
                analysisHealth(thirdData, dataDomain);
            } else if (data[needIndex] == 0x11) {
                dataDomain.setPositionHealthCommand(0x11);
                byte[] thirdData = DataUtil.subByteArray(data, needIndex + 1, data.length - needIndex - 1);
                dataDomain = indoorPositionData(thirdData, dataDomain);
            }
        }
        return dataDomain;
    }

    public static void analysisPositon(byte[] data, DataDomain dataDomain) {
        byte[] edataArr = DataUtil.subByteArray(data, 1, 5);
        byte[] ndataArr = DataUtil.subByteArray(data, 6, 5);
        String newEdataArr = "";
        for (int i = 0; i < 5; i++) {
            String Edata = DataUtil.toHexString(edataArr[i]);
            String newEdata = Edata.substring(1, 2) + Edata.substring(0, 1);
            newEdataArr += newEdata;
        }
        newEdataArr = newEdataArr.substring(0, 3) + "." + newEdataArr.substring(3, newEdataArr.length());
        double east = Double.valueOf(newEdataArr);
        dataDomain.setLng(east);
        String newNdaraArr = "";
        newNdaraArr += Integer.toHexString(ndataArr[0]).substring(0, 1);
        for (int i = 1; i < 5; i++) {
            String Ndata = DataUtil.toHexString(ndataArr[i]);
            String newNdata = Ndata.substring(1, 2) + Ndata.substring(0, 1);
            newNdaraArr += newNdata;
        }
        newNdaraArr = newNdaraArr.substring(0, 2) + "." + newNdaraArr.substring(2,
                newNdaraArr.length());
        double north = Double.valueOf(newNdaraArr);
        dataDomain.setLat(north);
        double[] bdGps = GPSUtil.getBdGps(east, north);
        double bdEast = bdGps[0];
        double bdNorth = bdGps[1];
        dataDomain.setBaiduLng(bdEast);
        dataDomain.setBaiduLat(bdNorth);
    }


    /**
     * 室内定位数据转换
     *
     * @return
     */
    public static DataDomain indoorPositionData(byte[] data, DataDomain dataDomain) {
        String[] lngLat = new String(data).split(",");
        Double lat = Double.valueOf(lngLat[0]);
        Double lng = Double.valueOf(lngLat[1]);
        dataDomain.setLng(lng);
        dataDomain.setLat(lat);
        double[] bdGps = GPSUtil.getBdGps(lng, lat);
        dataDomain.setBaiduLat(bdGps[1]);
        dataDomain.setBaiduLng(bdGps[0]);

        return dataDomain;
    }


    public static void analysisHealth(byte[] data, DataDomain dataDomain) {
        String thirdString = DataUtil.byteArrayToString(data);
        String xy = String.valueOf(Integer.parseInt(thirdString.substring(0, 3)));
        String xl = String.valueOf(Integer.parseInt(thirdString.substring(3, 6)));
        int wds = Integer.parseInt(thirdString.substring(6, 8));
        String wde = thirdString.substring(8, 10);
        String wd = "" + wds + "." + wde;
        String lz = thirdString.substring(10, 12);
        dataDomain.setBloodOxygen(Integer.parseInt(xy));
        dataDomain.setHeartRate(Integer.parseInt(xl));
        dataDomain.setTemperature(Double.parseDouble(wd));
        dataDomain.setSixAxis(lz);
    }

    public static byte[] addField(byte[] data, byte messageByte, String field) {
        int length = DataUtil.byteArrayToInt(DataUtil.subByteArray(data, 7, 2));
        length += field.length() + 3;
        byte[] bs = DataUtil.intToByteArray(length);
        data[7] = bs[1];
        data[8] = bs[0];
        data = DataUtil.addSingleByte(data, (byte) 0);
        data = DataUtil.addSingleByte(data, (byte) (field.length() + 3));
        data = DataUtil.addSingleByte(data, messageByte);
        byte[] bytes = field.getBytes();
        DataUtil.addByteArray(data, bytes);
        return data;
    }

    public static byte[] addField(byte[] data, byte messageByte, byte[] bytes) {
        int length = DataUtil.byteArrayToInt(DataUtil.subByteArray(data, 7, 2));
        length += bytes.length + 3;
        byte[] bs = DataUtil.intToByteArray(length);
        data[7] = bs[1];
        data[8] = bs[0];
        data = DataUtil.addSingleByte(data, (byte) 0);
        data = DataUtil.addSingleByte(data, (byte) (bytes.length + 3));
        data = DataUtil.addSingleByte(data, messageByte);
        DataUtil.addByteArray(data, bytes);
        return data;
    }

    public static byte[] response(byte[] data) {
        data[6] = 0;
        return data;
    }

    public static byte[] getDateBCD(String date) {
        byte[] data = new byte[7];
        data[0] = (byte) (Integer.parseInt(date.substring(0, 2), 16));
        data[1] = (byte) (Integer.parseInt(date.substring(2, 4), 16));
        data[2] = (byte) (Integer.parseInt(date.substring(5, 7), 16));
        data[3] = (byte) (Integer.parseInt(date.substring(8, 10), 16));
        data[4] = (byte) (Integer.parseInt(date.substring(11, 13), 16));
        data[5] = (byte) (Integer.parseInt(date.substring(14, 16), 16));
        data[6] = (byte) (Integer.parseInt(date.substring(17, 19), 16));
        return data;
    }

    public static byte[] responseLogin(byte[] data, String projectId) {
        return response(addField(data, (byte) 0x11, projectId));
    }

    public static byte[] responseLogin(boolean flag) {
        byte[] connack = {0x20, 0x02, 0x00, (byte) (flag ? 0x00 : 0x01)};
        return connack;
    }

    public static byte[] responseTimeSynch(byte[] data) {
        return response(addField(data, (byte) 0x0E, DataAnalyUtil.getDateBCD(DateUtil.getCurrentFullDate())));
    }

    public static byte[] responseCheckProjectPerson(byte[] data, boolean flag) {
        byte[] byteArray = new byte[1];
        byteArray[0] = flag ? (byte) 0 : (byte) 1;
        return response(addField(data, (byte) 0x0A, byteArray));
    }

    public static Timestamp timeData2Timestamp(byte[] data) {
        byte[] dataTime = DataUtil.flashBackByteArray(DataUtil.subByteArray(data, 0, 2));
        String yyyy = Integer.toHexString(dataTime[1]) + Integer.toHexString(dataTime[0]);
        String MM = Integer.toHexString(data[2]);
        String dd = Integer.toHexString(data[3]);
        String HH = Integer.toHexString(data[4]);
        String mm = Integer.toHexString(data[5]);
        String ss = Integer.toHexString(data[6]);
        String date = yyyy + "-" + MM + "-" + dd + " " + HH + ":" + mm + ":" + ss;
        return Timestamp.valueOf(date);
    }

    public static String getUpCodeCommandVersionFromPC(byte[] data) {
        return DataUtil.byteArrayToString(DataUtil.subByteArray(data, 13, (((int) data[11]) - 3)));
    }

    public static List<String> getSnListFromPC(byte[] data) {
        int versionLength = (int) data[11];
        int length1 = data[versionLength + 10] & 0xFF;
        int length2 = data[versionLength + 11] & 0xFF;
        int snListLength = length1 * 256 + length2;
        String str = DataUtil.byteArrayToString(DataUtil.subByteArray(data, versionLength + 13, snListLength - 3));
        String[] strs = str.replace(" ", "").replace("[", "").replace("]", "").split(",");
        return Arrays.asList(strs);
    }

    public static byte[] getFileFromPC(byte[] data) {
        int versionLength = (int) data[11];
        int length1 = data[versionLength + 10] & 0xFF;
        int length2 = data[versionLength + 11] & 0xFF;
        int snListLength = length1 * 256 + length2;
        int length5 = data[9] & 0xFF;
        int length3 = data[snListLength + versionLength + 10] & 0xFF;
        int length4 = data[snListLength + versionLength + 11] & 0xFF;
        int fileLength = length3 * 256 + length4 + length5 * 256 * 256;
        return DataUtil.subByteArray(data, snListLength + versionLength + 13, fileLength - 3);
    }

    public static boolean saveUpCodeFile2Localhost(String version, byte[] fileByteArray) {
        FileUtil.getFileByBytes(fileByteArray, DataAnalyUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("QR.jar", ""), version + ".bin");
        return true;
    }

    public static byte[] upCodeCommand2Terminal(String upCodeVersion, int codeLength) {
        byte[] versionBytes = upCodeVersion.getBytes();
        byte b1 = (byte) (((codeLength) >> 16) & 0xFF);
        byte b2 = (byte) (((codeLength) >> 8) & 0xFF);
        byte b3 = (byte) ((codeLength) & 0xFF);
        byte[] bs = {(byte) 0x00, (byte) 0x06, (byte) 0x12, b1, b2, b3};
        byte b4 = (byte) (((versionBytes.length + 3) >> 8) & 0xFF);
        byte b5 = (byte) ((versionBytes.length + 3) & 0xFF);
        int length = 10 + 3 + versionBytes.length + 6;
        byte[] byteArrayPrefix = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFE, (byte) 0x01, (byte) 0x00, (byte) 0x0B,
                (byte) 0x01, (byte) ((length >> 8) & 0xFF), (byte) (length & 0xFF), (byte) 0x00,
                b4, b5, (byte) 0x15};
        byte[] bytes = new byte[length];
        System.arraycopy(byteArrayPrefix, 0, bytes, 0, 13);
        System.arraycopy(versionBytes, 0, bytes, 13, versionBytes.length);
        System.arraycopy(bs, 0, bytes, 13 + versionBytes.length, 6);
        return bytes;
    }

    public static int getHasLength(byte[] bytes) {
        int sum = 0;
        for (int i = 0; i < bytes.length; i++) {
            sum += (bytes[bytes.length - 1 - i] & 0xFF) * (int) Math.pow(256, i);
        }
        return sum;
    }

    public static byte[] getUpCodeFileFromLocalhost(String version) {
        return FileUtil.getBytesByFile(DataAnalyUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("QR.jar", "") + version + ".bin");
    }

    public static byte[] responseUpCodeing(byte[] data, int hasLength, byte[] upCodeFileByteArray) {
        if (hasLength != upCodeFileByteArray.length) {
            int chLength = 256;
            if ((upCodeFileByteArray.length - hasLength) <= chLength) {
                chLength = upCodeFileByteArray.length - hasLength;
            }
            byte[] sendBytes = DataUtil.subByteArray(upCodeFileByteArray, hasLength, chLength);
            data[6] = 0;
            int length = DataUtil.byteArrayToInt(DataUtil.subByteArray(data, 7, 2));
            length += sendBytes.length + 8;
            byte[] bs = DataUtil.intToByteArray(length);
            data[7] = bs[1];
            data[8] = bs[0];
            byte[] chBs = DataUtil.intToByteArray(sendBytes.length + 3);
            byte[] b = DataUtil.intToByteArray(sendBytes.length);
            data = DataUtil.addSingleByte(data, chBs[1]);
            data = DataUtil.addSingleByte(data, chBs[0]);
            data = DataUtil.addSingleByte(data, (byte) 0x13);
            data = DataUtil.addByteArray(data, sendBytes);
            data = DataUtil.addSingleByte(data, (byte) 0x00);
            data = DataUtil.addSingleByte(data, (byte) 0x05);
            data = DataUtil.addSingleByte(data, (byte) 0x16);
            data = DataUtil.addSingleByte(data, b[1]);
            data = DataUtil.addSingleByte(data, b[0]);
            return data;
        } else {
            byte[] bytes = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFE, (byte) 0x01, (byte) 0x00, (byte) 0x0D,
                    (byte) 0x01, (byte) 0x00, (byte) 0x0A, (byte) 0x00};
            return bytes;
        }
    }

}
