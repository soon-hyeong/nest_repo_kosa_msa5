package org.kosa.nest.common;

import java.io.File;

/**
 * 유저 컴퓨터에 다운로드 받을 디렉토리 위치와 폴더명 설정하는 클래스 <br>
 */
public interface ClientConfig {
	String REPOPATH = System.getProperty("user.home") +  File.separator + "nest";
}
