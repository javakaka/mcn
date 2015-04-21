package com.mcn.test;

import java.text.ParseException;

import com.ezcloud.utility.Base64Util;
import com.ezcloud.utility.DateUtil;

public class Test {

	public static void main(String[] args) {
//		String decode =Base64Util.encode("10007".getBytes());
//		System.out.println("str --->>"+decode);
		String end_date =DateUtil.getCurrentDate();
		System.out.println("end_date --->>"+end_date.substring(0,10));
		String cur_date =DateUtil.getCurrentDate();
		cur_date ="2015-04-22";
		try {
			long minus=DateUtil.compare(cur_date+" 00:00:00", end_date+" 00:00:00");
			System.out.println("minus --->>"+minus);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
