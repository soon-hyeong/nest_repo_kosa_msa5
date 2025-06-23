package org.kosa.nest.common;

import java.io.File;

/**
 * 네트워크 연결에 사용되는 port번호를 저장하는 인터페이스입니다.
 */
public interface NestConfig {
	
	int port = 9876;
    String REPOPATH = System.getProperty("user.home") +  File.separator + "nestServer";

}
