package com.co.kr.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.co.kr.domain.BoardContentDomain;
import com.co.kr.domain.BoardFileDomain;
import com.co.kr.domain.BoardListDomain;

@Mapper
public interface UploadMapper {
	public List<BoardListDomain> boardList();
	
	public void contentUpload(BoardContentDomain boardContentDomain);
	public void fileUpload(BoardFileDomain boardFileDomain);
	
	public void bdContentUpdate(BoardContentDomain boardContentDomain);
	public void bdFileUpdate(BoardFileDomain boardFileDomain);
	
	public void bdContentRemove(HashMap<String, Object> map);
	public void bdFileRemove(BoardFileDomain boardFileDomain);
	
	public BoardListDomain boardSelectOne(HashMap<String, Object> map);
	public List<BoardFileDomain> boardSelectOneFile(HashMap<String, Object> map);

	//added
	public List<BoardListDomain> boardAllList(Map<String, Integer>map);
	public Integer boardCount();
}
