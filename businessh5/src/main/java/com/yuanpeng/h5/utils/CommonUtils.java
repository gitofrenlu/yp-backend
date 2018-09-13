package com.yuanpeng.h5.utils;

import lombok.extern.log4j.Log4j;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;

/**
 * Created by  on 2017/11/29.
 */
@Log4j
public class CommonUtils {

	/**
	 * 判断时间大小
	 * 
	 * @param date1
	 * @param date2
	 * @return 1 > 2 true
	 */
	public static boolean isBigger(Date date1, Date date2) {

		long time1 = date1.getTime();

		long time2 = date2.getTime();

		return time1 >= time2 ? true : false;
	}

	/**
	 * 对字符串md5加密
	 *
	 * @param str
	 * @return
	 */
	public static String getMD5(String str) {
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(str.getBytes());
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			return new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
			log.debug(e.getMessage());
		}
		return "";
	}

}
