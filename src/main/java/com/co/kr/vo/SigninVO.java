package com.co.kr.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class SigninVO {
	private String id;	
	private String pw;
	private String level;
	private String ip;
	private String use;
}