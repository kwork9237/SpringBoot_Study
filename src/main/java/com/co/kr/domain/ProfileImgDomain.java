package com.co.kr.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName="builder")
public class ProfileImgDomain {
	private String mbId;
	
	//profile image
	private String fileName;
	private String filePath;
}
