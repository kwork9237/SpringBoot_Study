package com.co.kr.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.co.kr.domain.InfoDomain;
import com.co.kr.domain.ProfileImgDomain;

public interface InfoService {
	//기본 crud 정의
	public void infoCreate(Map<String, String> map);
	public InfoDomain infoSelect(Map<String, String>map);
	public void infoUpdate(InfoDomain infoDomain);
	public void infoRemove(Map<String, String> map);
		
	//프로필 이미지
	public void profileImgUpload(ProfileImgDomain profileImgDomain, HttpServletRequest req, MultipartHttpServletRequest mulreq);
}
