package com.co.kr.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName="builder")
public class InfoDomain {
	private Integer mbSeq;
	private String mbId;
	
	//userinfo
	private String myName;
	private String myAddr;
	private String myZipCode;
}
