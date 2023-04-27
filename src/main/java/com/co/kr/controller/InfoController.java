package com.co.kr.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.co.kr.domain.InfoDomain;
import com.co.kr.service.InfoService;
import com.co.kr.vo.InfoVO;

@Controller
public class InfoController {
	@Autowired
	private InfoService infoService;
	
	//생성, 삭제는 가입과 탈퇴시에 이루어지므로 UserController에서 제어한다.
	//개인정보 조회
	@RequestMapping(value = "myPage")
	public ModelAndView myPage(HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = req.getSession();
		
		Integer seq = (Integer) session.getAttribute("mbSeq");
		
		Map<String, Integer> map = new HashMap<>();
		map.put("mbSeq", seq);
		
		InfoDomain info = infoService.infoSelect(map);
		
		if(info == null)
			System.out.println("data is null");
		
		mav.addObject("item", info);
		mav.setViewName("information/infoList.html");
		return mav;
	}
	
	//개인정보 수정
	@RequestMapping(value = "infoEdit")
	public ModelAndView infoEdit(HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = req.getSession();
		
		Integer seq = (Integer) session.getAttribute("mbSeq");
		
		Map<String, Integer> map = new HashMap<>();
		map.put("mbSeq", seq);
		
		InfoDomain info = infoService.infoSelect(map);
		
		if(info == null)
			System.out.println("data is null");
		
		mav.addObject("item", info);		
		mav.setViewName("/information/infoEditList.html");
		return mav;
	}
	
	@RequestMapping("/submit")
	public ModelAndView infoEdit(@ModelAttribute InfoVO infoVO,
			HttpServletRequest req, HttpServletResponse res,
			RedirectAttributes re) throws IOException {
		ModelAndView mav = new ModelAndView();
		HttpSession session = req.getSession();
		
		System.out.println("SUBMIT" + infoVO.getMbAddr());
		
		//System.out.println(res.);
		//Map<String, String> map = new HashMap<String, String>();
		//map.put("mbSeq", mbSeq);
		
		InfoDomain info = InfoDomain.builder()
				.mbSeq((Integer)session.getAttribute("mbSeq"))
				.mbId(session.getAttribute("id").toString())
				.mbName(infoVO.getMbName())
				.mbAddr(infoVO.getMbAddr())
				.mbEmail(infoVO.getMbEmail())
				.mbZipCode(infoVO.getMbZipCode())
				.build();
				
		infoService.infoUpdate(info);
		
		//re.addAttribute("page", session.getAttribute(mbSeq));
		mav.setViewName("redirect:/myPage");
		return mav;
	}
}
