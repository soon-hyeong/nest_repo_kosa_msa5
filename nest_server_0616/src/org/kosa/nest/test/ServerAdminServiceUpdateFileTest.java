package org.kosa.nest.test;

import java.io.IOException;

import org.kosa.nest.service.ServerAdminService;

public class ServerAdminServiceUpdateFileTest {
	public static void main(String[] args) {
		
		// ServerAdminService 클래스를 생성합니다.
		ServerAdminService serverAdminService = new ServerAdminService();
		
		try {
			
			// uploadFile() 메서드를 실행합니다.
			serverAdminService.uploadFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
