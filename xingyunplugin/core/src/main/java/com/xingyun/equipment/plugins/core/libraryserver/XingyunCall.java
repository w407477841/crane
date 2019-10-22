package com.xingyun.equipment.plugins.core.libraryserver;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

import java.io.File;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:15 2019/7/9
 * Modified By : wangyifei
 */
public class XingyunCall {
   public static final String filePath ;
    static {

        String path = System.getProperty("user.dir");
        path.replaceAll("/",File.separator);
        path.replaceAll("\\\\","");

        if(Platform.isWindows()){
            filePath = path+File.separator+"xingyun.dll";
        }else{
            filePath = path+File.separator+"libxingyun.so";
        }

    }
    public interface EnviromentPluginService extends Library {



        EnviromentPluginService INSTANCE = (EnviromentPluginService)Native.loadLibrary(filePath, EnviromentPluginService.class);

        /**
         *  解包函数
         * @param packet 原始协议串
         * @param packet_len 串长度
         * @param value 返回数据
         */
        void prase_packet(byte[] packet, int packet_len, byte[] value);

        /**
         * 拚包函数
         * @param packet 返回数据包
         * @param value  原始json
         * @return
         */
        int combination_packet(byte[] packet, byte[] value);
    }

}
