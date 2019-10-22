package com.xingyun.equipment.admin.config;

/**
 * @author zhouyujie
 * @create_date 2019-06-25
 * 阿里支付接口参数配置
 */
public class AliPayConfig {



    /**
     * 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
     */
    public static String APP_ID = "2019062465641976";


    /**
     * 商户私钥，您的PKCS8格式RSA2私钥
     */
    public static String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCW+36z04CvlHxEhTLCwcOU1+N8G+lLa3hrNy586TnWkB/RUB3I6klijRbg5jss095ted2BXxysssvgTktNMSy4gVvpRBsF4HXkLiXcjgoZcSFVkvxxl7+/r3OHrzKf709KuBulGUb3jItlnz22+2EdgpZrNLVZ6mPc21/Q0uKdiJVotaF4m66sXbtfqXdLjgXcWrOqzhSVFHtWC8J+6XfkOlS1Buta5ErEC/GseZC2IDdQumT2ysCqx4+C7S27QBSoQI9ouzIQch0Da2Iv80Vf654qexNwhABRf6UvV+Fbh+xyJxPPxrzwMSXY0uCc6WGRRyULz3Pf7Xm4NgOBBUp/AgMBAAECggEAOMkkcWwmJWOajYbvfZK1BeFg/QfeafBk3SZnBLIPf+S7ItWYLj34Fens5e+R6B1Ttic0qXjlZwrkZVvtNH4/TLHSqNxlsYMntAerU9XUdJc5K19aNTsW8niB4lGBn+e5EehG9YSZTXf94d3Pr2fCsf+wpWewob3ni2SzF+6JLyhgfMLPWS03J7/mEOZA9tA9exO7BZaZmKEzlvbzIrmuX3Yh6YBN/TXT64eRgESgMEr0MU1yVJIktRlbSbilUPXRzuZBCJFAiWOtbkDMMs6sOFyDXBQkTGZFqxmFtWfBPX8a6aeevfI8IPnblvUO/1THNQpYnpBLOc7tIUlIavtRIQKBgQDUa3Pd4LlHY2MuLmwvVNl1qRHETw1jHu5LMHrzIdFUEqp3R+hnegesD7dejvf7ICAaADL5ydPeZg94q90wlFu+r/QCrFb3X/uhmO+SujY2KJOiz4rgHUwbHLq8qscClWfCGFfpVE05OnPuAcCnoimGkFl2U9ppc2mXxfeHpDVocQKBgQC19UcJ9RSy0kynQKC6CnJXGzAHXfqs4QV1yCu5TBLnia/WyHshelBrojZh/faQvzsMie9lFuEUxasyR1LM6fT57wjDINcVvYP8S1WOAznQRxyz171LsekI/eYfxWj+CwLc4AeucvK6ATkwECUYFWQx707wVkzdiQsd8WNd8DHZ7wKBgCFyNdE2xBWQeZpY5uHg+0iCcXC2fcYgpaajIIjNx1b0hXC4mshIN5MI+ndiDXu2CDfv1hbsh3aaHU+DyBURFw0ESWhuWFck/srn+LjipwyKAQz+nsBA/K28LU7tmN44RhkSmiz8ABjFsmFEV2BNsTol2mA+PcKi0Hp8SkhKO7qRAoGAUMWEt2LXwVmhNU13C56/ivNCqsKJGFz7M4GcBzFB4w43xDxSjFXjhSQIyA8YeMCq0bIZVuKFH9dvW25Vzyi72obFePkHAVuTT1eRruy3qUEn+J14JjXfoGI02T2kkcEJkErSnhngrbkFTdSc4sf8Jj546C8nB+Bh0vToizaoqnkCgYEAiREcFJhc6PPt4Vv7t6VtIFHS9MQnaflgRNJ3FBZ6eQ80f4HRjNE3AjTgnM/dqTwZbE4/l0Pj782o9mwjlKdW84L4qyQ3deQuROqCBGNtCPfY7HfpwYf0yOf3xjSmhKVoO3gZi17yvPW0i9xFxhw7JdEpOZREKj+oov9nhcioVuo=";

    /**
     * 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
     */
    public static String   ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy8xZpWF8tXkDvGCemaHPQRNMpuOP9Tf99O7D7FUlpTS/lPdd+U6zFD26Zvy/GB8d7ZBXZvzbJwy4TOmTxrULncabEt5kh1VbBgWkv0TQJ/KCsl1i9hKCIV+0KTwLi1qf45pwaQepuJV2H7JpG+59lfvGBHPwmg/KstpW8jc0L5a+kLkPIYozMg4sUtTq61T/LHYX4xeVrx/F6LW4LHXgniqnrZTh6NPkMsOGOqHjxJZPDWdgXiTKjKoA8aK572113y19Q15NcPPHU2W37cAzkUG/+09FWXF7McjP2u82K2d30RkgcDPJh/3KSBYKL+xwpxGicu92wffx9JNppefKSQIDAQAB";

    /**
     *服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     */
    public static String NOTIFY_URL = "http://crane.jsxywg.cn/ssdevice/alipay/notifyUrl";

    /**
     * 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     */
    public static String RETURN_URL = "http://crane.jsxywg.cn/ssdevice/alipay/returnUrl";

    /**
     *   签名方式
     */
    public static String SIGN_TYPE = "RSA2";

    /**
     * 字符编码格式
     */
    public static String CHARSET = "utf-8";
    /**
     * 支付宝网关
     */
    public static String GATEWAY_URL = "https://openapi.alipay.com/gateway.do";
    /**
     * 日志路径
     */

    public static String LOG_PATH = "C:\\";

    /**
     * SIM卡验证
     */
    public static String SIM_BASE64="Basic eWlueWFucWluMzIyMjo1Mzg2NzJhYS01MzExLTQzY2EtYTVkYS0zNzliMjViNzc0MzY=";
}
