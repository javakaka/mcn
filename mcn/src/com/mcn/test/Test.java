package com.mcn.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;

import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.utility.Base64Util;
import com.ezcloud.utility.DateUtil;
import com.ezcloud.utility.StringUtil;

public class Test {

	public static void main(String[] args) throws UnsupportedEncodingException {
//		String decode =Base64Util.encode("10007".getBytes());
//		System.out.println("str --->>"+decode);
//		String end_date =DateUtil.getCurrentDate();
//		System.out.println("end_date --->>"+end_date.substring(0,10));
//		String cur_date =DateUtil.getCurrentDate();
//		cur_date ="2015-04-22";
//		try {
//			long minus=DateUtil.compare(cur_date+" 00:00:00", end_date+" 00:00:00");
//			System.out.println("minus --->>"+minus);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		String encode ="GDvI5ikkiDSL3HeancZI5A%3D%3D";
//		String decode =AesUtil.decode(URLDecoder.decode(encode));
//		System.out.println("decode --->>"+decode);
//		System.out.println("token --->>"+StringUtil.getRandKeys(6).toUpperCase());
//		System.out.println("token --->>"+Base64Util.encode("MJYC31".getBytes()));
		System.out.println("token --->>"+new String(Base64Util.decode("TUpZQzMx")));
		String cur_time =DateUtil.getCurrentDateTime();
		String year =cur_time.substring(0,4);
		String month =cur_time.substring(5,7);
		String day =cur_time.substring(8,10);
		System.out.println("year --->>"+year);
		System.out.println("month --->>"+month);
		System.out.println("day --->>"+day);
	}
}
