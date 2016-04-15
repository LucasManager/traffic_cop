package com.apl.traffic.cop.util;

import java.text.DecimalFormat;

public class TrafficCopUtils {

	/**
	 * blno 的数字部分格式化
	 */
	public static DecimalFormat blFormate = new DecimalFormat("000000000");
	
	/**
	 * blno 匹配的正则表达式，以APLU开头，中间有9位数字，后有0到2个字母
	 * */
	public static String blNoReg = "^APLU[0-9]{9}[a-zA-Z]{0,2}$";
	
	
}
