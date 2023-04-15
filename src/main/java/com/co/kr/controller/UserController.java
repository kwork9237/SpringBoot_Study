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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	@PostMapping("create")
	public ModelAndView create(LoginVO loginVO, HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		ModelAndView mav = new ModelAndView();
		
		//session 처리 
		HttpSession session = request.getSession();
		
		//페이지 초기화
		String page = (String) session.getAttribute("page");
		if(page == null)page = "1";
		
		// 중복체크
		Map<String, String> map = new HashMap();
		map.put("mbId", loginVO.getId());
		map.put("mbPw", loginVO.getPw());
		
		// 중복체크
		int dupleCheck = userService.mbDuplicationCheck(map);
		System.out.println(dupleCheck);

		if(dupleCheck > 0) { // 가입되있으면  
			String alertText = "중복이거나 유효하지 않은 접근입니다";
			String redirectPath = "/main";
			System.out.println(loginVO.getAdmin());
			if(loginVO.getAdmin() != null) {
				redirectPath = "/main/mbList?page="+page;
			}
			CommonUtils.redirect(alertText, redirectPath, response);
		}
		else {
			//현재아이피 추출
			String IP = CommonUtils.getClientIP(request);
			
			//전체 갯수
			int totalcount = userService.mbGetAll();
			
			//db insert 준비
			LoginDomain loginDomain = LoginDomain.builder()
					.mbId(loginVO.getId())
					.mbPw(loginVO.getPw())
					.mbLevel((totalcount == 0) ? "3" : "2") // 최초가입자를 level 3 admin 부여
					.mbIp(IP)
					.mbUse("Y")
					.build();
			
			// 저장
			userService.mbCreate(loginDomain);

			if(loginVO.getAdmin() == null) { // 'admin'들어있을때는 alert 스킵이다
				// session 저장 
				session.setAttribute("ip",IP);
				session.setAttribute("id", loginDomain.getMbId());
				session.setAttribute("mbLevel", (totalcount == 0) ? "3" : "2");   // 최초가입자를 level 3 admin 부여
				mav.setViewName("redirect:/bdList");
			}else { // admin일때
				mav.setViewName("redirect:/mbList?page=1");
			}
		}
		
		return mav;
	}
	
	//AdminList
	@GetMapping("mbList")
	public ModelAndView mbList(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		String page = (String) session.getAttribute("page"); // session에 담고 있는 page 꺼냄
		if(page == null)page = "1"; // 없으면 1
		
		//클릭페이지 세션에 담아줌
		session.setAttribute("page", page);
		
		//페이지네이션
		mav = mbListCall(request);  //리스트만 가져오기
		
		mav.setViewName("admin/adminList.html");
		return mav; 
	}
	
	//Get Modify Page
	@GetMapping("/modify/{mbSeq}")
    public ModelAndView mbModify(@PathVariable("mbSeq") String mbSeq, RedirectAttributes re) throws IOException {
		ModelAndView mav = new ModelAndView();
		re.addAttribute("mbSeq", mbSeq);
		mav.setViewName("redirect:/mbEditList");
		return mav;
	}
	
	@GetMapping("mbEditList")
	public ModelAndView mbListEdit(@RequestParam("mbSeq") String mbSeq, HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView();
		// 해당리스트 가져옴
		mav = mbListCall(request);  
		Map map = new HashMap<String, String>();
		map.put("mbSeq", mbSeq);
		LoginDomain loginDomain = userService.mbSelectList(map);
		mav.addObject("item",loginDomain);
		mav.setViewName("admin/adminEditList.html");
		return mav; 
	}
	
	//Member Update
	@RequestMapping("/update")
	public ModelAndView mbModify(LoginVO loginVO, HttpServletRequest request, RedirectAttributes re) throws IOException {
		
		ModelAndView mav = new ModelAndView();
		
		//page 초기화
		HttpSession session = request.getSession();
		
		String page = "1"; // 업데이트 되면 가장 첫화면으로 갈 것이다.  
		
		//db 업데이트
		LoginDomain loginDomain = null; //초기화
		String IP = CommonUtils.getClientIP(request);
		loginDomain = LoginDomain.builder()
				.mbSeq(Integer.parseInt(loginVO.getSeq()))
				.mbId(loginVO.getId())
				.mbPw(loginVO.getPw())
				.mbLevel(loginVO.getLevel())
				.mbIp(IP)
				.mbUse("Y")
				.build();
		userService.mbUpdate(loginDomain);
		
		//첫 페이지로 이동
		re.addAttribute("page",page); // 리다이렉트시 파람으로 실어서 보냄
		mav.setViewName("redirect:/mbList");
		return mav;
	}
	
	//Member Remove
	@GetMapping("/remove/{mbSeq}")
    public ModelAndView mbRemove(@PathVariable("mbSeq") String mbSeq, RedirectAttributes re, HttpServletRequest request) throws IOException {
		ModelAndView mav = new ModelAndView();
		
		//db 삭제
		Map map = new HashMap<String, String>();
		map.put("mbSeq", mbSeq);
		userService.mbRemove(map);

		//page 초기화
		HttpSession session = request.getSession();
				
		//보고 있던 현재 페이지로 이동
		re.addAttribute("page",session.getAttribute("page")); // 리다이렉트시 파람으로 실어서 보냄
		mav.setViewName("redirect:/mbList");
		return mav;
	}
	
	//멤버 리스트 (admin/list.html)에 데이터 input하기 위함
	public ModelAndView mbListCall(HttpServletRequest request) { //클릭페이지 널이면 
		ModelAndView mav = new ModelAndView();
		//페이지네이션 쿼리 참고
    // SELECT * FROM jsp.member order by mb_update_at limit 1, 5; {offset}{limit}

		//전체 갯수
		int totalcount = userService.mbGetAll();
		int contentnum = 10; // 데이터 가져올 갯수 

		//데이터 유무 분기때 사용
		boolean itemsNotEmpty;
		
		if(totalcount > 0) { // 데이터 있을때
			
			// itemsNotEmpty true일때만, 리스트 & 페이징 보여주기
			itemsNotEmpty = true;
			//페이지 표현 데이터 가져오기
			Map<String,Object> pagination = Pagination.pagination(totalcount, request);
			
			Map map = new HashMap<String, Integer>();
	        map.put("offset",pagination.get("offset"));
	        map.put("contentnum",contentnum);
			
	        //페이지별 데이터 가져오기
			List<LoginDomain> loginDomain = userService.mbAllList(map);
			
			//모델객체 넣어주기
			mav.addObject("itemsNotEmpty", itemsNotEmpty);
			mav.addObject("items", loginDomain);
			mav.addObject("rowNUM", pagination.get("rowNUM"));
			mav.addObject("pageNum", pagination.get("pageNum"));
			mav.addObject("startpage", pagination.get("startpage"));
			mav.addObject("endpage", pagination.get("endpage"));
			
		}else {
			itemsNotEmpty = false;
		}
		
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
	
	//signin
	@GetMapping("signin")
    public ModelAndView signIn() throws IOException {
		ModelAndView mav = new ModelAndView();
        mav.setViewName("signin/signin.html"); 
        return mav;
    }
}