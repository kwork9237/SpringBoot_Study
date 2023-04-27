package com.co.kr.service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.co.kr.domain.InfoDomain;
import com.co.kr.domain.ProfileDomain;
import com.co.kr.mapper.InfoMapper;

@Service
public class InfoServiceImpl implements InfoService{
	@Autowired
	private InfoMapper infoMapper;

	@Override
	public void infoCreate(InfoDomain infoDomain) {
		infoMapper.infoCreate(infoDomain);
	}

	@Override
	public InfoDomain infoSelect(Map<String, String> map) {
		System.out.println("INFOSERVICE IMPL : " + map);
		System.out.println(infoMapper.infoSelect(map));
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
	public void profileImgUpdate(ProfileDomain profileDomain) {
		infoMapper.profileImgUpdate(profileDomain);
	}
	
	public void profileImgProcess(MultipartHttpServletRequest mulreq, HttpServletRequest req) {
		Path root = Paths.get(new File("C://data").toString(), "profile", File.separator).toAbsolutePath().normalize();
		File pathChk = new File(root.toString());
		
		if(!pathChk.exists()) pathChk.mkdirs();

		
	}
}