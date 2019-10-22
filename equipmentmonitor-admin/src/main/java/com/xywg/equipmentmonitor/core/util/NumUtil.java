package com.xywg.equipmentmonitor.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 数字格式化的类
 *
 * @author wangcw
 * @date 2016年11月30日 下午5:58:40
 */
public class NumUtil {

    /**
     * @Description 保留指定位数的小数(少的位数不补零)
     * @author wangcw
     */
    public static String keepRandomPoint(Double value, int n) {
        if (value == null) {
            value = 0.00;
            return new BigDecimal(value).setScale(n, RoundingMode.HALF_UP).toString();
        } else {
            return new BigDecimal(value).setScale(n, RoundingMode.HALF_UP).toString();
        }
    }

    /**
     * @Description 浮点保留两位小数(少的位数不补零)
     * @author wangcw
     */
    public static String keep2Point(double value) {
        return keepRandomPoint(value, 2);
    }
    
    public static List<Integer> string2List(String ids) {
		List<Integer> list=Lists.newArrayList();
		if(ids.contains(",")) {
			String[] id = ids.split(",");
			// 声明long类型的数组
			for (int i = 0; i < id.length; i++) {
				// 将str转换为long类型，并赋值给thelong
				list.add(Integer.valueOf(id[i]));
			}
		}else {
			list.add(Integer.valueOf(ids));
		}
		return list;
	}
    
    public static List<String> stringToList(String ids) {
		List<String> list=Lists.newArrayList();
		if(ids.contains(",")) {
			String[] id = ids.split(",");
			// 声明long类型的数组
			for (int i = 0; i < id.length; i++) {
				// 将str转换为long类型，并赋值给thelong
				list.add(id[i]);
			}
		}else {
			list.add(ids);
		}
		return list;
	}

    /**
     * @Description 浮点保留1位小数(少的位数不补零)
     * @author wangcw
     */
    public static String keep1Point(double value) {
        return keepRandomPoint(value, 1);
    }

    /**
     * @Description 浮点保留任意位小数(少位补零)
     * @author wangcw
     */
    public static String keepRandomPointZero(double value, int n) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(Double.valueOf(keepRandomPoint(value, n)));
    }

    /**
     * @Description 浮点保留两位小数(少位补零)
     * @author wangcw
     */
    public static String keep2PointZero(double value) {
        return keepRandomPointZero(value, 2);
    }

    /**
     * @Description 获取任意小数点位的百分比表示
     * @author wangcw
     */
    public static String percentRandomPoint(double value, int n) {
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setGroupingUsed(false);
        percent.setMaximumFractionDigits(n);
        return percent.format(value);
    }

    /**
     * @Description 百分比保留两位小数
     * @author wangcw
     */
    public static String percent2Point(double value) {
        return percentRandomPoint(value, 2);
    }

    /**
     * @Description 获取格式化经纬度后的小数(保留3位)
     * @author wangcw
     */
    public static String latLngPoint(double value) {
        return keepRandomPoint(value, 3);
    }

}
