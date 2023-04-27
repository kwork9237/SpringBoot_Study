package com.co.kr.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.co.kr.domain.InfoDomain;
import com.co.kr.domain.ProfileImgDomain;

@Mapper
public interface InfoMapper {
	//기본 crud 정의
	public void infoCreate(Map<String, String> map);
	public InfoDomain infoSelect(Map<String, String>map);
	public void infoUpdate(InfoDomain infoDomain);
	public void infoRemove(Map<String, String> map);
	
	//프로필 이미지
	public void profileImgUpload(ProfileImgDomain profileImgDomain);
}
