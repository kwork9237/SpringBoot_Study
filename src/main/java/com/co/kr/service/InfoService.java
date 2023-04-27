package com.co.kr.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.co.kr.domain.InfoDomain;
import com.co.kr.domain.ProfileDomain;

public interface InfoService {
	//정보 생성
	public void infoCreate(InfoDomain infoDomain);
		
	//특정 정보 조회
	public InfoDomain infoSelect(Map<String, String> map);
		
	//정보 UPDATE
	public void infoUpdate(InfoDomain infoDomain);
		
	//정보 삭제
	public void infoRemove(Map<String, String> map);
		
	//프로필 사진 업데이트
	public void profileImgUpdate(ProfileDomain profileDomain);
	
	public void profileImgProcess(MultipartHttpServletRequest request, HttpServletRequest httpReq);
}