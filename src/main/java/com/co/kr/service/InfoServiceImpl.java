package com.co.kr.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.co.kr.domain.InfoDomain;
import com.co.kr.mapper.InfoMapper;

@Service
public class InfoServiceImpl implements InfoService{
	@Autowired
	private InfoMapper infoMapper;
	
	@Override
	public void infoCreate(Map<String, String> map) {
		infoMapper.infoCreate(map);
	}

	@Override
	public InfoDomain infoSelect(Map<String, String>map) {
		return infoMapper.infoSelect(map);
	}

	@Override
	public void infoUpdate(InfoDomain infoDomain) {
		infoMapper.infoUpdate(infoDomain);
	}

	@Override
	public void infoRemove(Map<String, String> map) {
		infoMapper.infoRemove(map);
	}

	@Override
	public void profileImgUpload(InfoDomain infoDomain) {
		infoMapper.profileImgUpload(infoDomain);
	}

	@Override
	public void profileImgUpdate(InfoDomain infoDomain) {
		infoMapper.profileImgUpdate(infoDomain);
	}
}
