//package com.xywg.attendance.common.utils;
//
//
//import org.apache.commons.net.util.Base64;
//import org.csource.util.FastDFSFile;
//import org.csource.util.FastDFSUtil;
//import org.springframework.stereotype.Component;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
///**
// * @author hjy
// * @date 2019/3/5
// * FastDfs 分布式文件管理系统 工具类
// */
//@Component
//public class FastDfsUtil {
//
//
//    /**
//     * 上传文件
//     *
//     * @param base64Str base64字符串
//     * @param suffix    文件后缀
//     * @return
//     * @throws IOException
//     */
//    public String uploadFile(String base64Str, String suffix) {
//        byte[] bytes = Base64.decodeBase64(base64Str);
//        FastDFSFile fastDFSFile = new FastDFSFile();
//        fastDFSFile.setExt(suffix);
//        fastDFSFile.setContent(bytes);
//        return FastDFSUtil.upload(fastDFSFile);
//    }
//
//
//    public static byte[] toByteArray(InputStream in) throws IOException {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        byte[] buffer = new byte[4096];
//        int n;
//        while((n = in.read(buffer)) != -1) {
//            out.write(buffer, 0, n);
//        }
//
//        return out.toByteArray();
//    }
//
//}
