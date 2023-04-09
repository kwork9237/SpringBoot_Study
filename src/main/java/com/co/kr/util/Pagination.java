package com.co.kr.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class Pagination {
	public static Map<String, Object> pagination(int totalcount, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		//페이지넘버 초기화
		String pnum = request.getParameter("page");
		System.out.println("pnum : " + pnum);
		
		if(pnum == null)
			pnum = "1";
		
		//String to int
		int rowNUM = Integer.parseInt(pnum);
		if(rowNUM < 0) rowNUM = 1;
		
		//페이지네이션 범위 정함, 나머지가 없거나 있으면 +1
		int pageNum;
		if (totalcount % 10 == 0) pageNum = totalcount / 10;
		else pageNum = (totalcount / 10) + 1;
		
		if(rowNUM > pageNum) rowNUM = pageNum;
		
		//페이지네이션 중간범위 지정 (시작페이지 21 ~ 27 / 끝 30)
		int temp = (rowNUM - 1) % 10; //0, 1, 2 나머지값
		int startpage = rowNUM - temp; //temp = startpage - 1
		int endpage = startpage + (10 - 1);
		
		//startpage 기준 무조건 +9 한 것이여서 pagecount 비교 후 pagecount 적용
		if (endpage > pageNum) endpage = pageNum;
		
		//쿼리 범위 지정
		int offset = (rowNUM - 1) * 10;
		
		map.put("rowNUM", rowNUM);
		map.put("pageNum", pageNum);
		map.put("startpage", startpage);
		map.put("endpage", endpage);
		map.put("offset", offset);
		
		return map;
	}
}
