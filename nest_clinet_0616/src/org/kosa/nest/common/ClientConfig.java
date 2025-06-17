package org.kosa.nest.common;

import java.io.File;

public interface ClientConfig {
	String REPOPATH = System.getProperty("user.home") +  File.separator + "nest";
//	String REPOPATH = "C:\\Project"+"\\nesttest"; // 테스트용 경로. 현재 학원 컴퓨터는 
}
