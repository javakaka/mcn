package com.mcn.test;

import com.ezcloud.utility.Base64Util;

public class Test {

	public static void main(String[] args) {
		String decode =Base64Util.encode("10007".getBytes());
		System.out.println("str --->>"+decode);
	}
}
