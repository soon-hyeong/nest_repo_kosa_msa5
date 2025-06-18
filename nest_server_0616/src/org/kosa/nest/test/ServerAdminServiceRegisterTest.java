package org.kosa.nest.test;

import java.util.Scanner;
import org.kosa.nest.service.ServerAdminService;

public class ServerAdminServiceRegisterTest {
	public static void main(String[] args) {
		ServerAdminService service = new ServerAdminService();
		Scanner scanner = new Scanner(System.in);
		boolean running = true;
		
		while (running) {
			System.out.println("1. 회원가입");
			System.out.println("2. 로그인");
			System.out.println("3. 내 정보");
			System.out.println("4. 내 정보 수정");
			System.out.println("5. 로그아웃");
			System.out.println("0. 종료");
			System.out.println("숫자를 입력하세요: ");

			try {
				int input = Integer.parseInt(scanner.nextLine().trim());

				switch (input) {
				case 1:
					service.register();
					break;
				case 2:
					if(service.login()) {
						System.out.println("로그인 성공");
					}else {
						System.out.println("로그인 실패");
					}
					break;
				case 3:
					var info = service.getMyInformation();
					if(info != null) {
						System.out.println(info);
					}
					break;
				case 4:
					var update = service.updateMyInformation();
					if(update != null)
						System.out.println("수정된 정보: " + update);
					break;
				case 5:
					if(service.logout())
						System.out.println("로그아웃 성공");
					break;
				case 0:
					running = false;
					System.out.println("테스트를 종료합니다.");
					break;
				default:
					System.out.println("잘못된 입력입니다.");
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("숫자를 입력하세요.");
			} catch (Exception e) {
				System.out.println("오류 발생: " + e.getMessage());
			}
		}
		scanner.close();
	}
}
