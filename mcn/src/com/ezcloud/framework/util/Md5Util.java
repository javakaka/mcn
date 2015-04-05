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

	public static void main(String[] args) {

		String ssss = Md5("10004");
		String s3 = "21218CCA77804D2BA1922C33E0151105";
		// Md5("ezcloud");
		System.out.println(ssss);
		System.out.println(Md5Util.Md5("10004"));
		System.out.println(ssss.equals(s3));
	}

}
