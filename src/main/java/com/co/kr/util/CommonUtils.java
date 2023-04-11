package com.co.kr.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class CommonUtils {
	public static String currentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
		Date currentDate = new Date();
		return sdf.format(currentDate);
	}
	
	//get IP
	public static String getClientIP(HttpServletRequest req) {
		String ip = req.getHeader("X-Forward-For");
		
		if(ip == null) ip = req.getHeader("Proxy-Client-IP");
		if(ip == null) ip = req.getHeader("WL-Proxy-Client-IP");
		if(ip == null) ip = req.getHeader("HTTP_CLIENT_IP");
		if(ip == null) ip = req.getHeader("HTTP_X_FORWARDED_FOR");
		if(ip == null) ip = req.getRemoteAddr();
		
		if(ip.equals("0:0:0:0:0:0:0:1")) ip = ip.replace("0:0:0:0:0:0:0:1", "127.0.0.1");
		
		return ip;
	}
	
	//로컬 맥 주소 번환
	public static String getLocalMacAddr() {
		try {
			InetAddress IP = InetAddress.getLocalHost();
			NetworkInterface NI = NetworkInterface.getByInetAddress(IP);
			byte[] macAddr = NI.getHardwareAddress();
			String[] temp = new String[macAddr.length];
			
			for (int i = 0; i < macAddr.length; i++)
				temp[i] = String.format("%02X", macAddr[i]);

			String sMacAddr = String.join(":", temp);
				
			//System.out.println("sMacAddr Value : " + temp);
				
			return sMacAddr;
		}
		catch (UnknownHostException e) {
			System.out.println("UnknownHost Exception : " + e);
		}
		catch (SocketException e) {
			System.out.println("Socket Exception : " + e);
		}
			
		//오류일 경우 null 반환
		return null;
	}
	
	//Auth redirect
	//public static void redirect(String alertText, String redirectPath, HttpServletResponse response) throws IOException {
	public static void redirect(String alertText, String redirectPath, HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		ModelAndView mav = new ModelAndView();
		
		//개발용 리다이렉트
		out.println("<script>alert('" + alertText + "'); loaction.href='" + redirectPath + "'</script>");
		out.flush();
	}
}
