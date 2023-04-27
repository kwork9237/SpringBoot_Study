package com.co.kr.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.co.kr.domain.InfoDomain;
import com.co.kr.service.InfoService;

@Controller
public class InfoController {
	@Autowired
	private InfoService infoService;
	
	@RequestMapping(value = "myPage")
	public ModelAndView myPage(HttpServletRequest req) {
		HttpSession session = req.getSession();
		ModelAndView mav = new ModelAndView();
		
		Map<String, String> map = new HashMap<>();
		map.put("mbSeq", session.getAttribute("mbSeq").toString());
		
		InfoDomain info = infoService.infoSelect(map);
		mav.addObject("item", info);
		
		mav.setViewName("information/infoList.html");
		return mav;
	}
}
