package org.kosa.nest.common;

public interface DbConfig {
	// 인터페이스이므로 public static final로 자동 인식된다
	String DRIVER = "com.mysql.cj.jdbc.Driver"; //패키지명.클래스명
	String URL = "jdbc:mysql://localhost:3306/kosa?serverTimezone=UTC"; 
	String USER = "joyofbeing"; // 계정명 
	String PASS = "1234"; // 비밀번호
}
