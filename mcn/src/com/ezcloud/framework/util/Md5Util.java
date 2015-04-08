package com.ezcloud.framework.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5加密辅助类
 * 
 * @author Administrator
 */

public class Md5Util {

	// md5加密生成字符串
	public static String Md5(String plainText) {

		StringBuffer buf = new StringBuffer("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return buf.toString().toUpperCase();
	}
	
	/**
	 * MD5 32位加密方法一 小写
	 * 
	 * @param str
	 * @return
	 */
	public final static String get32LowercaseMD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			// 使用MD5创建MessageDigest对象
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = md[i];
				// 将没个数(int)b进行双字节加密
				str[k++] = hexDigits[b >> 4 & 0xf];
				str[k++] = hexDigits[b & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {

		String ssss = Md5("123456");
//		String s3 = "E10ADC3949BA59ABBE56E057F20F883E";
		// Md5("ezcloud");
		System.out.println(ssss);
		System.out.println(Md5Util.Md5("000000"));
//		System.out.println(ssss.equals(s3));
//		System.out.println(Md5Util.Md5("000000"));
//		System.out.println(Md5Util.Md5("723096"));
	}

}
