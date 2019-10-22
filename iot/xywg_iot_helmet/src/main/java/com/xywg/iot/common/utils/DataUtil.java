package com.xywg.iot.common.utils;

public class DataUtil {
	
	public static byte[] subByteArray(byte[] data, int startIndex, int sublength) {
		byte[] code = new byte[sublength];
		System.arraycopy(data, startIndex, code, 0, sublength);
		return code;
	}
	
	public static String byteArrayToString(byte[] data) {
		StringBuilder result = new StringBuilder();
		char temp;
		int length = data.length;
		for (int i = 0; i < length; i++) {
			temp = (char) data[i];
			result.append(temp);
		}
		return result.toString();
	}
	
	public static int byteArrayToInt(byte[] data) {
		int a = ((data[0] & 0xf0) >> 4) * 4096;
		int b = ((data[0] & 0x0f)) * 256;
		int c = ((data[1] & 0xf0) >> 4) * 16;
		int d = ((data[1] & 0x0f));
		int sum = a + b + c + d;
		return sum;
	}
	
	public static byte[] intToByteArray(int n) {    
		byte[] b = new byte[4];    
		b[0] = (byte) (n & 0xff);    
		b[1] = (byte) (n >> 8 & 0xff);    
		b[2] = (byte) (n >> 16 & 0xff);    
		b[3] = (byte) (n >> 24 & 0xff);    
		return b;    
	}
	
	public static String toHexString(byte[] bytes) {
		final String HEX = "0123456789abcdef";
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			sb.append(HEX.charAt((b >> 4) & 0x0f));
			sb.append(HEX.charAt(b & 0x0f));
		}
		return sb.toString();
	}
	
	public static String toHexString(byte b) {
		char[] HexCode = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		StringBuffer buffer = new StringBuffer();
		buffer.append(HexCode[(b >>> 4) & 0x0f]);
		buffer.append(HexCode[b & 0x0f]);
		return buffer.toString();
	}
	
	public static byte[] addSingleByte(byte[] oldByteArray, byte singleByte) {
		byte[] bytes = new byte[1];
		bytes[0] = singleByte;
		return addByteArray(oldByteArray,bytes);
	}
	
	public static byte[] addByteArray(byte[] oldByteArray, byte[] addByteArray) {
		byte[] byteArray = new byte[oldByteArray.length + addByteArray.length];
		System.arraycopy(oldByteArray, 0, byteArray, 0, oldByteArray.length);
		System.arraycopy(addByteArray, 0, byteArray, oldByteArray.length, addByteArray.length);
		return byteArray;
	}
	
	public static byte[] flashBackByteArray(byte[] data) {
		for (int start = 0, end = data.length - 1; start < end; start ++, end --) {
			byte temp = data[end];
			data[end] = data[start];
			data[start] = temp;
		}
		return data;
	}
	
}
