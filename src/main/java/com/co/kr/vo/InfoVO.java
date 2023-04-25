package com.co.kr.vo;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Getter;

@ToString
@Getter
@AllArgsConstructor
public class InfoVO {
	private String id;
	
	private String name;
	private String addr;
	private String zipcode;
}
