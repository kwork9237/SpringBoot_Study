package com.co.kr.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.co.kr.domain.BoardListDomain;
import com.co.kr.domain.LoginDomain;
import com.co.kr.service.UploadService;
import com.co.kr.service.UserService;
import com.co.kr.util.CommonUtils;
import com.co.kr.util.Pagination;
import com.co.kr.vo.LoginVO;
import com.co.kr.vo.SigninVO;

//import lombok.extern.slf4j.Slf4j;

@Controller
//@Slf4j
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
		Map<String, String> map = new HashMap<>();
		map.put("mbId", loginDTO.getId());
		map.put("mbPw", loginDTO.getPw());
		
		//중복체크
		int dupleCheck = userService.mbDuplicationCheck(map);
		System.out.println("dupleCheck => " + dupleCheck);
		
		if(dupleCheck == 0) {
			String alertText = "없는 아이디이거나 패스워드가 잘못되었습니다. 가입해주세요.";
			String redirectPath = "/main/signin";
			
			CommonUtils.redirect(alertText, redirectPath, response);
			
			mav.setViewName("signin/signin.html");
			return mav;
		}
		
		if(dupleCheck >= 2) {
			String alertText = "중복된 ID가 2개 이상입니다. DB 확인을 해 주세요.";
			String redirectPath = "/main";
			
			CommonUtils.redirect(alertText, redirectPath, response);
			
			mav.setViewName("/index.html");
			return mav;
		}
		
		LoginDomain loginDomain = userService.mbGetId(map);		
		
		//현재 아이피 추출
		String IP = CommonUtils.getClientIP(request);
		
		//session 저장
		session.setAttribute("ip", IP);
		session.setAttribute("id", loginDomain.getMbId());
		session.setAttribute("mbLevel", loginDomain.getMbLevel());
		session.setAttribute("macAddr", CommonUtils.getLocalMacAddr()); //mac 주소 추가
		
		//seq data
		session.setAttribute("mbSeq", loginDomain.getMbSeq());
		
		List<BoardListDomain> items = uploadService.boardList();
		//System.out.println("items ==> " + items);
		
		mav.addObject("items", items);
		//mav.addObject("items", bdListCall(request));
		mav.setViewName("board/boardList.html");
		
		return mav;
	};
	
	//좌측 메뉴 클릭시 보드화면 이동 (로그인된 상태)
	@RequestMapping(value = "bdList")
	//public ModelAndView bdList() {
	public ModelAndView bdList(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		List<BoardListDomain> items = uploadService.boardList();
		//System.out.println("items ==> " + items);
		
		mav.addObject("items", items); //mav.addObject("items", bdListCall(request));
		
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
	
	//Logout
	@RequestMapping(value = "logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModelAndView mav = new ModelAndView();
		
		//세션 전부 제거
		HttpSession session = request.getSession();
		session.removeAttribute("ip");
		session.removeAttribute("id");
		session.removeAttribute("mbSeq");
		session.removeAttribute("mbLevel");
		
		//쿠키 제거
		Cookie[] cookies = request.getCookies();
		
		if(cookies != null) {
			for(int i = 0; i < cookies.length; i++) {
				cookies[i].setMaxAge(0);
				response.addCookie(cookies[i]);
			}				
		}
		
		//로그인 페이지로 이동시킨다.
		mav.setViewName("/index.html");
		
		return mav;
	}
	
	//Member Create
	@RequestMapping(value = "create")
	public ModelAndView mbCreate(SigninVO signinDTO, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModelAndView mav = new ModelAndView();
		
		String IP = CommonUtils.getClientIP(request);
				
		//중복체크
		Map<String, String> map = new HashMap<>();
		map.put("mbId", signinDTO.getId());
		map.put("mbPw", signinDTO.getPw());
		map.put("mbIp", IP);
		
		//의문 : mbLevel과 mbUse 지정이 안되서 들어가는것.
		//해결 : UserMapper에서 수정하면 된다.
		
		//멤버 확인
		int check = userService.mbDuplicationCheck(map);
	
		//디버그용
		//System.out.println("[DEBUG] check value : " + check);
		
		//정상 생성시 리다이렉트, 메인화면 이동
		//어드민리스트에서 강제로 메인화면으로 보내버리는 경우가 있음. (개선 필요)
		if(check == 0) {
			//멤버 생성
			userService.mbCreate(map);
			
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
	
	//AdminList
	@RequestMapping(value = "mbList")
	public ModelAndView mbList(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		mav = mbListCall(request);
		mav.setViewName("admin/adminList.html");
		
		return mav;
	}
	
	//Get Modify Page
	@GetMapping("modify/{mbSeq}")
	public ModelAndView modify(@PathVariable String mbSeq, HttpServletRequest request, HttpServletResponse response) throws IOException {
		//참고
		//GetMapping 값 read -> https://galid1.tistory.com/556
		//PathVariable와 {} 이름은 같아야 한다.
		ModelAndView mav = new ModelAndView();
		
		HttpSession session = request.getSession();
		Integer SessSeq = (Integer) session.getAttribute("mbSeq");
		
		//String로 할 경우 안 되서 Integer로 캐스팅
		if(Integer.parseInt(mbSeq) != SessSeq) {
			String alertText = "타인의 계정은 수정할 수 없습니다!";
			String redirectPath = "/main";
			
			//System.out.println("mbSeq : " + mbSeq );
			//System.out.println("SessionData : " + session.getAttribute("mbSeq").toString());
			
			CommonUtils.redirect(alertText, redirectPath, response);
		}

		mav = toAdminListRedirect(mbSeq, request);
		
		return mav;
	}
	
	//Member Update
	@RequestMapping(value = {"/modify/update"})
	public ModelAndView update(@RequestParam("pw") String password, @RequestParam("seq") String mbSeq,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		ModelAndView mav = new ModelAndView();
		
		//조회할 mbseq 입력, 선택 및 업데이트
		Map<String, String> map = new HashMap<String, String>();
		map.put("mbSeq", mbSeq);
		LoginDomain member = userService.mbSelectList(map);
		
		//마스터 계정 업데이트 방지
		if(Integer.parseInt(mbSeq) == 0) {
			String alertText = "마스터 계정은 변경할 수 없습니다.";
			String redirectPath = "/main";
			
			CommonUtils.redirect(alertText, redirectPath, response);
			
			//마스터 계정 코드로 리다이렉트
			mav = toAdminListRedirect(mbSeq, request);
			return mav;
		}
		
		//멤버가 null일 경우 예외처리
		if( member == null ) {
			String alertText = "사용자를 찾을 수 없습니다.";
			String redirectPath = "/main/mbList";
			
			CommonUtils.redirect(alertText, redirectPath, response);
			
			//마스터 계정 코드로 리다이렉트
			mav = toAdminListRedirect(mbSeq, request);
			return mav;
		}
		
		member.setMbPw(password);
		
		userService.mbUpdate(member);
		
		//Update 성공 메시지
		String alertText = member.getMbId() + "의 비밀번호가 갱신되었습니다.";
		String redirectPath = "/main";
		
		CommonUtils.redirect(alertText, redirectPath, response);
		
		mav = toAdminListRedirect(mbSeq, request);
		return mav;
	}
	
	//Member Remove
	@RequestMapping(value = {"/remove/{mbSeq}"})
	public ModelAndView remove(@PathVariable String mbSeq,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		ModelAndView mav = new ModelAndView();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("mbSeq", mbSeq);
		LoginDomain member = userService.mbSelectList(map);
		
		//마스터 계정 삭제 방지
		if(Integer.parseInt(mbSeq) == 0) {
			String alertText = "마스터 계정은 삭제할 수 없습니다.";
			String redirectPath = "/main";
			
			CommonUtils.redirect(alertText, redirectPath, response);
			
			//마스터 계정 코드로 리다이렉트
			mav = toAdminListRedirect(mbSeq, request);
			return mav;
		}
		
		HttpSession session = request.getSession();
		Integer SessSeq = (Integer) session.getAttribute("mbSeq");
		
		//String로 할 경우 안 되서 Integer로 캐스팅 (삭제방지)
		if(Integer.parseInt(mbSeq) != SessSeq) {
			String alertText = "타인의 계정은 삭제할 수 없습니다!";
			String redirectPath = "/main";
			
			CommonUtils.redirect(alertText, redirectPath, response);
			
			mav = toAdminListRedirect(mbSeq, request);
			return mav;
		}
		
		//멤버가 null일 경우 예외처리
		if( member == null ) {
			String alertText = "사용자를 찾을 수 없습니다.";
			String redirectPath = "/main/";
			
			CommonUtils.redirect(alertText, redirectPath, response);
			
			//마스터 계정 코드로 리다이렉트
			mav = toAdminListRedirect(mbSeq, request);
			return mav;
		}
		
		userService.mbRemove(map);
		
		//Remove 성공 메시지
		String alertText = "아이디 : " + member.getMbId() + "가 정상적으로 삭제되었습니다.";
		String redirectPath = "/main";
				
		CommonUtils.redirect(alertText, redirectPath, response);
		
		mav = toAdminListRedirect(mbSeq, request);
		return mav;
	}
	
	//멤버 리스트 (admin/list.html)에 데이터 input하기 위함
	public ModelAndView mbListCall(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		//멤버 카운트 확인, 비어있는지 확인
		Integer memberCount = userService.mbGetAll();
		Boolean isNotEmpty;
				
		//멤버 목록의 크기를 구하고, 0 이상이면 비어있지 않음 반환
		if(memberCount > 0) {
			isNotEmpty = true;
			mav.addObject("isNotEmpty", isNotEmpty);
		}
				
		else {
			isNotEmpty = false;
			mav.addObject("isNotEmpty", isNotEmpty);
		}
		
		//페이지네이션 지정
		Map<String, Object> pegmap;
		pegmap = Pagination.pagination(memberCount, request);
		mav.addAllObjects(pegmap);
		
		//Member Data Get
		Map<String, Integer> map = new HashMap<>();
		Integer content = 10;	//조회 개수 지정
		
		//페이지네이션 조회시작지점을 강제로 형변환 해서 가져옴
		map.put("offset", (int)pegmap.get("offset"));
		map.put("contentnum", content);
		
		List<LoginDomain> mbList = userService.mbAllList(map);
		mav.addObject("items", mbList);
		
		return mav;
	}
	
	public ModelAndView bdListCall(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		//게시글 개수 총합
		Integer boardCount = uploadService.boardCount();
		Boolean isNotEmpty;
		
		//멤버 목록의 크기를 구하고, 0 이상이면 비어있지 않음 반환
		if(boardCount > 0) {
			isNotEmpty = true;
			mav.addObject("isNotEmpty", isNotEmpty);
		}
				
		else {
			isNotEmpty = false;
			mav.addObject("isNotEmpty", isNotEmpty);
		}
		
		//페이지네이션 지정
		Map<String, Object> pegmap;
		pegmap = Pagination.pagination(boardCount, request);
		mav.addAllObjects(pegmap);
		
		//Member Data Get
		Map<String, Integer> map = new HashMap<>();
		Integer content = 10;	//조회 개수 지정
			
		//페이지네이션 조회시작지점을 강제로 형변환 해서 가져옴
		map.put("offset", (int)pegmap.get("offset"));
		map.put("contentnum", content);
		
		List<BoardListDomain> bdList = uploadService.boardAllList(map);
		mav.addObject("items", bdList);
		
		System.out.println(mav);
		
		return mav;
	}
	
	//멤버 리스트 조회
	public ModelAndView toAdminListRedirect(String mbSeq, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("mbSeq", mbSeq);

		LoginDomain member = userService.mbSelectList(hmap);
		
		mav = mbListCall(request);
		mav.addObject("item", member);

		//수정, 선택시
		if(member != null)
			mav.setViewName("admin/adminEditList.html");
		
		//삭제시
		else {
			hmap.remove("mbSeq");
			hmap.put("mbSeq", "0");
			
			member = userService.mbSelectList(hmap);
			mav.setViewName("admin/adminList.html");
		}
		
		return mav;
	}
}