package com.zkteco;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author hjy
 */
public class ZKCryptoClientService {

    static {
        InputStream is = null;
        try {
            //获取当前类加载器
            ClassLoader classLoader = ZKCryptoClientService.class.getClassLoader();
            //通过当前累加载器方法获得 文件db.properties的一个输入流
            is = classLoader.getResourceAsStream("application.properties");
            //创建一个Properties 对象
            Properties properties = new Properties();
            properties.load(is);
            //加载输入流
            String osName = System.getProperties().getProperty("os.name");
            //Windows 环境读取路径
            if (osName.contains("Windows")) {
                System.load(properties.getProperty("password-bank-file-windows"));
                //Linux 环境读取路径
            } else {
                System.load(properties.getProperty("password-bank-file-linux"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(is !=null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static native long init();

    public static native int free(long handle);

    public static native int setParameter(long handle, int paramCode, byte[] paramValue, int size);

    public static native int getParameter(long handle, int paramCode, byte[] paramValue, int[] size);

    public static native int encryptData(long handle, byte[] src, int cbSrc, byte[] dst, int[] cbDst);

    public static native int decryptData(long handle, byte[] src, int cbSrc, byte[] dst, int[] cbDst);

    public static native int encryptFile(long handle, String srcFilePathName, String dstFilePathName);

    public static native int decryptFile(long handle, String srcFilePathName, String dstFilePathName);

    public static native int encryptDataBase64(long handle, byte[] src, int cbSrc, byte[] dst, int[] cbDst);

    public static native int decryptDataBase64(long handle, byte[] src, int cbSrc, byte[] dst, int[] cbDst);

}
