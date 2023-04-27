package com.co.kr.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.co.kr.domain.InfoDomain;
import com.co.kr.domain.ProfileDomain;

@Mapper
public interface InfoMapper {
	//정보 생성
	//public void infoCreate(InfoDomain infoDomain);
	public void infoCreate(Map<String, String> map);
	
	//특정 정보 조회
	public InfoDomain infoSelect(Map<String, Integer> map);
	
	//정보 UPDATE
	public void infoUpdate(InfoDomain infoDomain);
	
	//정보 삭제
	public void infoRemove(Map<String, String> map);
	
	//프로필 사진 업데이트
	public void profileImgUpdate(ProfileDomain profileDomain);
}
