package com.xingyun.equipment.crane.core.util;

import java.math.BigDecimal;

/**
 * 工具类
 * 
 * @author lw
 * @date 2018年7月17日
 * @company jsxywg
 */
public class BigDecimalUtil {
	private BigDecimalUtil() {
	}

	/**
	 * 
	 * BigDecimal的加法运算封装
	 * 
	 * @param b1
	 * @param bn
	 * @return
	 * 
	 */
	public static BigDecimal safeAdd(BigDecimal b1, BigDecimal... bn) {
		if (null == b1) {
			b1 = BigDecimal.ZERO;
		}
		if (null != bn) {
			for (BigDecimal b : bn) {
				b1 = b1.add(null == b ? BigDecimal.ZERO : b);
			}
		}
		return b1;

	}

	/**
	 * 
	 * 计算金额方法
	 * @param b1
	 * 
	 * @param bn
	 * 
	 * @return
	 * 
	 */
	public static BigDecimal safeSubtract(BigDecimal b1, BigDecimal... bn) {
		return safeSubtract(false, b1, bn);

	}

	/**
	 * 
	 * BigDecimal的安全减法运算
	 * 
	 * @param isZero
	 *            减法结果为负数时是否返回0，true是返回0（金额计算时使用），false是返回负数结果
	 * @param b1
	 *            被减数
	 * @param bn
	 *            需要减的减数数组
	 * @return
	 * 
	 */
	public static BigDecimal safeSubtract(Boolean isZero, BigDecimal b1, BigDecimal... bn) {
		if (null == b1) {
			b1 = BigDecimal.ZERO;
		}
		BigDecimal r = b1;
		if (null != bn) {
			for (BigDecimal b : bn) {
				r = r.subtract((null == b ? BigDecimal.ZERO : b));
			}
		}
		return isZero ? (r.compareTo(BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : r) : r;
	}

	/**
	 * 金额除法计算，返回2位小数（具体的返回多少位大家自己看着改吧）
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 * 
	 */
	public static <T extends Number> BigDecimal safeDivide(T b1, T b2) {
		return safeDivide(b1, b2, BigDecimal.ZERO);

	}

	/**
	 * 
	 * BigDecimal的除法运算封装，如果除数或者被除数为0，返回默认值 默认返回小数位后2位，用于金额计算
	 * 
	 * @param b1
	 * @param b2
	 * @param defaultValue
	 * @return
	 * 
	 */
	public static <T extends Number> BigDecimal safeDivide(T b1, T b2, BigDecimal defaultValue) {
		if (null == b1 || null == b2) {
			return defaultValue;
		}
		try {
			return BigDecimal.valueOf(b1.doubleValue())
					.divide(BigDecimal.valueOf(b2.doubleValue()), 4, BigDecimal.ROUND_HALF_UP);
		} catch (Exception e) {
			return defaultValue;
		}
	}

}
