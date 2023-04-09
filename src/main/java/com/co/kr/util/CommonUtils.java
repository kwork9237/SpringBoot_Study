package com.co.kr.util;

import java.io.IOException;
import java.io.PrintWriter;
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
	};
	
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
