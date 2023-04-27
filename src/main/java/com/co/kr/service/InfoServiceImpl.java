package com.co.kr.service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.co.kr.domain.InfoDomain;
import com.co.kr.domain.ProfileImgDomain;
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
	public void profileImgUpload(ProfileImgDomain profileImgDomain, HttpServletRequest req, MultipartHttpServletRequest mulreq) {
		HttpSession session = req.getSession();
		
		String mbId = profileImgDomain.getMbId();
		
		List<MultipartFile> multipartFiles = mulreq.getFiles("files");
		
		System.out.println(multipartFiles);
		
		
		//경로 지정 및 폴더 생성
		Path root = Paths.get(new File("C://").toString(), "userProfile", File.separator).toAbsolutePath().normalize();
		File chk = new File(root.toString());
		
		if(!chk.exists()) chk.mkdirs();
		
		
		
		
		infoMapper.profileImgUpload(profileImgDomain);
	}
}
