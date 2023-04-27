package com.co.kr.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.co.kr.domain.InfoDomain;
import com.co.kr.domain.ProfileImgDomain;
import com.co.kr.service.InfoService;
import com.co.kr.vo.SigninVO;

@Controller
public class InfoController {
	@Autowired
	private InfoService infoService;
	
	@RequestMapping(value = "myPage")
	public ModelAndView myPage(HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();
		
		//세션에서 id 가져오기
		HttpSession session = req.getSession();
		String uid = (String) session.getAttribute("id");
		
		//개인 유저정보 가져오기
		Map <String, String> map = new HashMap<>();
		map.put("mbId", uid);
		System.out.println(uid);
		
		InfoDomain id = infoService.infoSelect(map);
		System.out.println(id);
		
		mav.addObject("item", id);
		mav.setViewName("information/infoList.html");
		
		return mav;
	}
	
	@PostMapping(value="profileUpload")
	public ModelAndView profileUpload(ProfileImgDomain profileImgDomain, HttpServletRequest req, MultipartHttpServletRequest mulreq) throws IOException {
		ModelAndView mav = new ModelAndView();
		
		infoService.profileImgUpload(profileImgDomain, req, mulreq);
		
		mav.setViewName("information/infoList.html");
		return mav;
	}
}