package org.kosa.nest.test;

import java.util.Scanner;

import org.kosa.nest.model.AdminDao;
import org.kosa.nest.model.AdminVO;

public class AdminDaoTest {
	public static void main(String[] args) {
		AdminDao dao = new AdminDao();
		Scanner sc = new Scanner(System.in); // 테스트용 객체
		try {
			// 1. register 테스트
			System.out.print("등록할 관리자 이메일 입력: ");
            String email = sc.nextLine();
            System.out.print("등록할 관리자 비밀번호 입력: ");
            String password = sc.nextLine();
            
            AdminVO vo = new AdminVO(0,email,password);
			int accountNo = dao.register(vo); // register 메서드 호출과 결과
			System.out.println("등록 완료. 계정 번호 : " + accountNo);
			
			// 2. getAdminInfo 테스트
			System.out.print("조회할 관리자 ID를 입력하세요: ");
			int searchId = Integer.parseInt(sc.nextLine());
			AdminVO retrievedAdmin = dao.getAdminInfo(searchId); 
			if (retrievedAdmin != null) {
			    System.out.println("조회된 관리자 정보: " + retrievedAdmin.getId() + ", " + retrievedAdmin.getEmail() + ", " + retrievedAdmin.getPassword());
			} else {
			    System.out.println("해당 ID의 관리자가 존재하지 않습니다.");
			}

            // 3. updateAdminInfo 테스트 (비밀번호 변경 등)
            System.out.print("수정할 이메일 입력: ");
            String newEmail = sc.nextLine();
            System.out.print("수정할 비밀번호 입력: ");
            String newPassword = sc.nextLine();

            retrievedAdmin.setEmail(newEmail);
            retrievedAdmin.setPassword(newPassword);
            dao.updateAdminInfo(retrievedAdmin);
            System.out.println("관리자 정보 수정 완료.");

            // 4. 수정된 결과 다시 조회
            AdminVO updatedAdmin = dao.getAdminInfo(searchId); // 사용자가 입력한 ID 기준으로 조회
            if (updatedAdmin != null) {
                System.out.println("수정된 관리자 정보: " + updatedAdmin.getId() + ", " + updatedAdmin.getEmail() + ", " + updatedAdmin.getPassword());
            } else {
                System.out.println("수정된 정보를 찾을 수 없습니다.");
            }
		} catch(Exception e) {
			System.out.println("등록 실패 : " + e.getMessage());
			e.printStackTrace();
		}finally {
			sc.close();
		}
	}
}