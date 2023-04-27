package com.co.kr.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder(builderMethodName="builder")
public class ProfileDomain {
	private Integer mbSeq;
	
	private String fileName;
	private String filePath;
}
