
package com.ezcloud.framework.util;

import java.io.IOException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.ezcloud.framework.vo.IVO;
import com.ezcloud.framework.vo.VOConvert;


/**
 * description:
 * 
 * 
 * @author Franklin.Zhang --zhangyixuan
 * @date May 23, 2013 10:35:59 AM
 */
public class AesUtil {
//	private static final byte[] aesKey="@365@gl@@shi@ke@".getBytes();
	private static final byte[] aesKey="fang@zhu@bao@app".getBytes();
	private static SecretKey secretKey=new SecretKeySpec(aesKey,"AES");
	
	static{
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}
	
 
	
	public static byte[] aesEncrypt(byte[] plainDaa) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
		secretKey=new SecretKeySpec(aesKey,"AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte encryptedData[] = cipher.doFinal(plainDaa);
		return encryptedData;
	}

	public static byte[] aesDecrypt(byte[] encryptedData) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte decryptedData[] = cipher.doFinal(encryptedData);
		return decryptedData;
	}

	
	public static final String encrypt(String input) throws Exception {

			//Base64加密
			String rBase64Encode=base64Encode(input.getBytes("UTF-8"));
			//AES加密
			byte[] rAESEncode=aesEncrypt(rBase64Encode.getBytes("UTF-8"));
			//Base64加密
			return base64Encode(rAESEncode);
	}

	public static final String decrypt(String input) throws Exception {
		//Base64解密
		byte[] rBase64Bytes = base64Decode(input);
		//AES解密
		String rAESDecode=new String(aesDecrypt(rBase64Bytes),"UTF-8");
		//Base64解密
		return new String(base64Decode(rAESDecode),"UTF-8");
	}

	public static String base64Encode(byte[] s) throws Exception {
		if (s == null)
			return  null;
		return new String(Base64.encodeBase64(s),"UTF-8");
	}

	public static byte[] base64Decode(String s) throws IOException {
		if (s == null)
			return null;
		byte[] b = Base64.decodeBase64(s.getBytes());
		return b;
	}

	/**
	 * encode
	 * @param input
	 * @return
	 * @throws Exception 
	 */
	public static String encode(String input) throws Exception{
		if(input==null || "".equals(input) || "null".equals(input)){
			input = "";
		}
			if(null!=input&&!"".equals(input)){
				return encrypt(input);
			}else{
				return "";
			}

	}
	
	/**
	 * decode
	 * @param input	
	 * @return
	 * @throws Exception 
	 */
	public static String decode(String input) throws Exception{
			if(null!=input&&!"".equals(input)){
				return decrypt(input);
			}else{
				return "";
			}
	}
	
	/**/
	public static void main(String[] args) throws Exception {
		
//		String input = "10004";
		String input = "{\"SERVICE\":{\"TYPE\":\"0\"},\"HEADER\":{\"COMPANY\":\"易之云科技有限公司\",\"VERSION\":\"4.0\",\"COPYRIGHT\":\"COPYRIGHT2010-2020\"},\"REQUEST\":{\"ROW\":[],\"DEFAULT\":{\"VERSION\":\"1.0\",\"USERNAME\":\"13826531136\",\"PASSWORD\":\"E10ADC3949BA59ABBE56E057F20F883E\"},\"DATASET\":[]}}";
		String encrptStr =  encode(input);
		System.out.println("Encode:" +encrptStr);
		System.out.println("Decode:" + decode(encrptStr));
		String encode ="Ngn1aWQ+u1siNR+rQkMMkJbWYIGCZ1TsoMykxSMNwq5o1DFiS1vO3s59YYanc+CASepqMi7TfTfpN2MsyvLyPPk9VokIKztnrCqk6Z57gy9HqH6DxmEDF+xD2kUbry/i5T/0K2KmvJWWIBxhJi+4TU0xPNTQKFtTMRH/1IhEXbGGdYOFCmh917e+70Lj2QzNzL6VZN7t/bTTMi++DMEFD+qOkAX1QzpfWqFe9j+fzHxmkKYkRHCSWsdbujFzceojuozpAUQlYrff7vjk4MHV4uRQ2JpT3Xe7o8YhROpO872VHqpqYVbSR6pplF8B/YC0YAai6iMUNldyIPQxcw1HMX9sx3qbPuESrcj2ouXqP2eqjzzs9GfNTUy3sOnkcnjyvukUxrymScxO3FjHCUjxSjmosN7PbvWAEShAo9Rh2f5pXemtUniIji55hX+ieI2ufh3WWgB6/QPc/1r+wNPZfmedQt1Cr9vji7n1KmokDUQzcviL0Hb0he4mNajH+Otlf86s8wmkvKca7w3CX9GMkSmbotWHsXw1IwQzw/UUxHWq4jmjjJYcr9czsVFd3ftXPNhi+vDKNpHHSicGITKqsoOh0tkUKV1X+VgJHfLSnXcvYh5WglX+4hCjsfocgSgC";
		String strr =decode(encode);
		System.out.println("Decode:" +strr );
		IVO ivo=VOConvert.jsonToIvo(strr);
		System.out.println("name===>>"+ivo.getString("username"));
		
		String sss="430528198902101311x";
		encrptStr =  encode(sss);
		System.out.println("Encode:" +encrptStr);
		System.out.println("decode:" +decode("fENe/+al176MS1OIvQ7zsQ=="));
		
	}
	
	
}

