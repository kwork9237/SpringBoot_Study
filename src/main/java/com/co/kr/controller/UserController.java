package com.co.kr.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.co.kr.domain.BoardListDomain;
import com.co.kr.domain.LoginDomain;
import com.co.kr.service.UploadService;
import com.co.kr.service.UserService;
import com.co.kr.util.CommonUtils;
import com.co.kr.vo.LoginVO;
import com.co.kr.vo.SigninVO;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping(value = "/")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private UploadService uploadService;
	
	@RequestMapping(value = "board")
	public ModelAndView login(LoginVO loginDTO, HttpServletRequest request, HttpServletResponse response) throws IOException {
		//session 처리
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView();
		
		//중복체크
		Map<String, String> map = new HashMap();
		map.put("mbId", loginDTO.getId());
		map.put("mbPw", loginDTO.getPw());
		
		//중복체크
		int dupleCheck = userService.mbDuplicationCheck(map);
		LoginDomain loginDomain = userService.mbGetId(map);
		
		System.out.println("dupleCheck => " + dupleCheck);
		
		if(dupleCheck == 0) {
			String alertText = "없는 아이디이거나 패스워드가 잘못되었습니다. 가입해주세요.";
			String redirectPath = "/main/signin";
			
			CommonUtils.redirect(alertText, redirectPath, response);
			
			mav.setViewName("signin/signin.html");
			return mav;
		}
		
		//현재 아이피 추출
		String IP = CommonUtils.getClientIP(request);
		
		//session 저장
		session.setAttribute("ip", IP);
		session.setAttribute("id", loginDomain.getMbId());
		session.setAttribute("mbLevel", loginDomain.getMbLevel());
		
		List<BoardListDomain> items = uploadService.boardList();
		//System.out.println("items ==> " + items);
		
		mav.addObject("items", items);
		mav.setViewName("board/boardList.html");
		
		return mav;
	};
	
	//좌측 메뉴 클릭시 보드화면 이동 (로그인된 상태)
	@RequestMapping(value = "bdList")
	public ModelAndView bdList() {
		ModelAndView mav = new ModelAndView();
		List<BoardListDomain> items = uploadService.boardList();
		//System.out.println("items ==> " + items);
		
		mav.addObject("items", items);
		mav.setViewName("board/boardList.html");
		
		return mav;
	}
	
	//Sign in
	@RequestMapping(value = "signin")
	public ModelAndView signin() {
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("signin/signin.html");
		
		return mav;
	}
	
	
	//Member Create
	@RequestMapping(value = "create")
	public ModelAndView mbCreate(SigninVO signinDTO, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModelAndView mav = new ModelAndView();
		
		String IP = CommonUtils.getClientIP(request);
				
		//중복체크
		Map<String, String> map = new HashMap();
		map.put("mbId", signinDTO.getId());
		map.put("mbPw", signinDTO.getPw());
		map.put("mbIp", IP);
		
		//의문 : mbLevel과 mbUse 지정이 안되서 들어가는것.
		//해결 : UserMapper에서 수정하면 된다.
		
		//멤버 생성
		userService.mbCreate(map);
		
		//정상적으로 생성됐나 확인.
		int check = userService.mbDuplicationCheck(map);
		
		//디버그용
		//System.out.println("[DEBUG] check value : " + check);
		
		//정상 생성시 리다이렉트, 메인화면 이동
		if(check == 1) {
			String alertText = "아이디가 성공적으로 생성되었습니다. 로그인해 주세요.";
			String redirectPath = "/main";
			
			CommonUtils.redirect(alertText, redirectPath, response);
			
			mav.setViewName("/index.html");
			return mav;
		}
		
		//이미 ID가 존재하는 경우, 다른 ID 생성요청, 다시 페이지 보여줌
		else {
			String alertText = "이미 존재하는 아이디입니다. 다른 아이디를 생성해 주세요.";
			String redirectPath = "/main/signin";
			
			CommonUtils.redirect(alertText, redirectPath, response);
			
			mav.setViewName("/signin/signin.html");
			return mav;
		}
	}
}