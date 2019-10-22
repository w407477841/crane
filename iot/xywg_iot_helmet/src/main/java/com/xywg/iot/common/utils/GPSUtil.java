package com.xywg.iot.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GPSUtil {
	

	public static double[] getBdGps(double east,double north) {
		String commString = "";
		String coords = String.valueOf(east)+","+String.valueOf(north);
		String dest_url = "http://api.map.baidu.com/geoconv/v1/?coords=" + coords
				+ "&from=1&to=5&output=json&ak=y4gOxuETyZwfOIYUpNcGdq6guAHMM1gs";
		double[] gps2bd =new double[2];
		URL url = null;
		HttpURLConnection urlconn = null;
		OutputStream out = null;
		BufferedReader rd = null;
		try {
			url = new URL(dest_url);
			urlconn = (HttpURLConnection) url.openConnection();
			urlconn.setReadTimeout(1000 * 30);
			urlconn.setRequestMethod("POST");
			urlconn.setDoInput(true);
			urlconn.setDoOutput(true);
			out = urlconn.getOutputStream();
			out.write(commString.getBytes("UTF-8"));
			out.flush();
			out.close();
			rd = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
			StringBuffer sb = new StringBuffer();
			int ch;
			while ((ch = rd.read()) > -1) {
				sb.append((char) ch);
			}
			String result = sb.toString();
			JSONObject jsonData = JSONObject.parseObject(result);
			JSONArray jsonObject = jsonData.getJSONArray("result");

			double x = jsonObject.getJSONObject(0).getDoubleValue("x");
			double y = jsonObject.getJSONObject(0).getDoubleValue("y");

			gps2bd = new double[2];
			gps2bd[0] = Math.floor(x * 10000000) / 10000000;
			gps2bd[1] = Math.floor(y * 10000000) / 10000000;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (urlconn != null) {
					urlconn.disconnect();
				}
				if (rd != null) {
					rd.close();
				}
			} catch (Exception e) {
			}
		}
		return gps2bd;
	}

}