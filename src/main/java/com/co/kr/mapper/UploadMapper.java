package com.co.kr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.co.kr.domain.BoardListDomain;

@Mapper
public interface UploadMapper {
	public List<BoardListDomain> boardList();
}
