package org.kosa.nest.common;

import java.io.File;

/**
 * 유저 컴퓨터에 다운로드 받을 디렉토리 위치와 폴더명 설정 및 <br>
 * 네트워크 연결에 사용되는 서버 ip와 port번호를 저장하는 클래스 <br>
 */
public interface NestConfig {
    String REPOPATH = System.getProperty("user.home") +  File.separator + "nest";
    String ip = "192.168.210.27";
    int port = 9876;
}