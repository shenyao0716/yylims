package lims.util;

import java.math.BigDecimal;
import java.util.*;

/**
 * 工具类
 * @author zhaoyj
 *
 */
public class CommUtil {
	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 10;

	// 这个类不能实例化
	protected CommUtil() {

	}

	public static String getValue(Object val){
		if( val == null )
			return null;
		return String.valueOf(val);
	}
	@SuppressWarnings("unchecked")
	public static void validate(Map<String, Object> eventParam) throws Exception{
		Iterator<Map.Entry<String, Object>> it = eventParam.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, Object> entry = it.next();
			Object object = entry.getValue();
			if(object == null)
				throw new Exception("请传入参数【" + entry.getKey() + "】");
			else if(object instanceof List){
				if(((List)object).isEmpty())
					throw new Exception(entry.getKey()+"列表不能为空数据");
			}
		}
	}

	/**
	 * 提供精确的加法运算。
	 *
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 *
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 *
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 *
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 *
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 *
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 获得‘当天’加上‘延长有效天数’的日期
	 * @return
	 */
	public static Date getPostExtendValidity(Long extendValidity) {
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, extendValidity.intValue());
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTime();
	}

	/**
	 * 获得‘date’加上‘延长有效天数’的日期
	 * @return
	 */
	public static Date getPostExtendValidity(Date date, Long extendValidity) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.DATE, extendValidity.intValue());
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTime();
	}

	/**
	 * 获取两个时间的相差的天数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getTowDay(Date date1, Date date2){
		return (int)(date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000);
	}

	/**
	 * 设置时间为23:59:59
	 * @param date
	 * @return
	 */
	public static Date setTime(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.set(Calendar.HOUR_OF_DAY, 23);
		ca.set(Calendar.MINUTE, 59);
		ca.set(Calendar.SECOND, 59);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTime();
	}
	/**
	 * 设置day天后的时间为23:59:59
	 * @param date 当天date为null或者new Date()
	 * @param day 天数
	 * @return
	 */
	public static Date setTime(Date date, Long day) {
		Calendar ca = Calendar.getInstance();
		if(date != null)
			ca.setTime(date);
		ca.add(Calendar.DATE, day.intValue());
		ca.set(Calendar.HOUR_OF_DAY, 23);
		ca.set(Calendar.MINUTE, 59);
		ca.set(Calendar.SECOND, 59);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTime();
	}
}


