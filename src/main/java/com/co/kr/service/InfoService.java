package com.co.kr.service;

import java.util.Map;

import com.co.kr.domain.InfoDomain;

public interface InfoService {
	//기본 crud 정의
	public void infoCreate(Map<String, String> map);
	public InfoDomain infoSelect(Map<String, String>map);
	public void infoUpdate(InfoDomain infoDomain);
	public void infoRemove(Map<String, String> map);
		
	//프로필 이미지
	public void profileImgUpload(InfoDomain infoDomain);
	public void profileImgUpdate(InfoDomain infoDomain);
}
