package com.co.kr.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName="builder")
public class InfoDomain {
	private String mbId;
	
	//profile image
	private String fileName;
	private String filePath;
	
	//userinfo
	private String myName;
	private String myAddr;
	private String myZipCode;
}
