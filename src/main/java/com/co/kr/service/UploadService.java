package com.co.kr.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.co.kr.domain.BoardFileDomain;
import com.co.kr.domain.BoardListDomain;
import com.co.kr.vo.FileListVO;

public interface UploadService {
	public List<BoardListDomain> boardList();
	
	public List<BoardListDomain> boardAllList(Map<String, Integer> map);
	
	public int fileProcess(FileListVO fileListVO, MultipartHttpServletRequest request, HttpServletRequest httpReq);
	
	public void bdContentRemove(HashMap<String, Object> map);
	
	public void bdFileRemove(BoardFileDomain boardFileDomain);
	
	public BoardListDomain boardSelectOne(HashMap<String, Object> map);
	
	//Added
	public List<BoardFileDomain> boardSelectOneFile(HashMap<String, Object> map);
	public Integer boardCount();
}