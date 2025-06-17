package org.kosa.nest.test;

import org.kosa.nest.model.AdminDao;
import org.kosa.nest.model.AdminVO;

public class AdminDaoTest {
	public static void main(String[] args) {
		AdminDao dao = new AdminDao();
		AdminVO vo1 = new AdminVO(2,"nest1@naver.com","1234"); // 테스트용 객체
		try {
			int accountNo = dao.register(vo1); // register 메서드 호출과 결과
			System.out.println("등록 완료. 계정 번호 : " + accountNo);
		} catch(Exception e) {
			System.out.println("등록 실패 : " + e.getMessage());
			e.printStackTrace();
		}
	}
    
}