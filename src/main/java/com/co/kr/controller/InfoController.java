package com.co.kr.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.co.kr.service.InfoService;
import com.co.kr.vo.SigninVO;

@Controller
public class InfoController {
	@Autowired
	private InfoService infoService;
	
	@RequestMapping(value = "myPage")
	public ModelAndView myPage() {
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("information/infoList.html");
		
		return mav;
	}
}
 