package org.kosa.nest.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.kosa.nest.common.DatabaseUtil;

public class AdminDao {
	
	@SuppressWarnings("unused")
	private static AdminDao instance;
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	private AdminDao() {
		
	}
	
	public static AdminDao getInstance() {
		return instance = new AdminDao();
	}
	/**
	 * 1. register 회원가입
	 * SQL에 쓸 때 email과 password를 입력하여 회원가입을 진행
	 * 
	 * @param admin
	 * @return
	 * @throws SQLException
	 */
	public int register(AdminVO admin) throws SQLException {
		conn = DatabaseUtil.getConnection();
		String sql = "INSERT INTO admin (email, password) VALUES(?,?)"; // Email,Password 순으로 입력
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, admin.getEmail());
			pstmt.setString(2, admin.getPassword());

			return pstmt.executeUpdate();
		} finally {
			DatabaseUtil.closeAll(pstmt, conn);
		}
	}

	
	/**
	 * 2. getAdminInfo
	 * email로 AdminVO에 들어있는 관리자의 정보를 추출하도록 진행
	 * 
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	public AdminVO getAdminInfo(String email) throws SQLException {
		conn = DatabaseUtil.getConnection();
		String sql = "SELECT id, email, password FROM admin WHERE email = ?"; // email를 통해 관리자 정보를 조회
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return new AdminVO(rs.getInt("id"), rs.getString("email"), rs.getString("password"));
			}else {
				throw new SQLException("email doesn't exist");
			}
		} finally {
			DatabaseUtil.closeAll(rs, pstmt, conn);
		}

	}

	
	/**
	 * 3. updateAdminInfo
	 * AdminVO에 있는 관리자 정보를 불러와 email과 password를 수정하도록 진행
	 * 
	 * @param admin
	 * @throws SQLException
	 */
	public void updateAdminInfo(AdminVO admin) throws SQLException {
		conn = DatabaseUtil.getConnection();
		String sql = "UPDATE admin SET email = ?, password = ? WHERE id = ?"; // Id를 찾아 email과 password를 수정하도록 구현
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, admin.getEmail());
	        pstmt.setString(2, admin.getPassword());
	        pstmt.setInt(3, admin.getId());

	        pstmt.executeUpdate();
	    } finally {
	        DatabaseUtil.closeAll(null, pstmt, conn);
	    }
	}
}
