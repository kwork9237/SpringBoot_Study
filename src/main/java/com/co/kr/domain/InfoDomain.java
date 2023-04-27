package com.co.kr.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder(builderMethodName="builder")
public class InfoDomain {
	private Integer mbSeq;
	private String mbId;
	private String mbName;
	private String mbAddr;
	private String mbEmail;
	private String mbZipCode;
}
