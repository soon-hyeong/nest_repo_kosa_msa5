package org.kosa.nest.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.kosa.nest.common.DatabaseUtil;

public class AdminDao {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	// 1. register : 회원 가입
	public int register(AdminVO admin) throws SQLException {
		conn = DatabaseUtil.getConnection();
		String sql = "INSERT INTO admin (email, password) VALUES(?,?)"; // Id,Email,Password 순으로 입력
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, admin.getEmail());
			pstmt.setString(2, admin.getPassword());

			return pstmt.executeUpdate();
		} finally {
			DatabaseUtil.closeAll(pstmt, conn);
		}
	}

	// 2. getAdminInfo : ID로 관리자 정보 조회
	public AdminVO getAdminInfo(String email) throws SQLException {
		conn = DatabaseUtil.getConnection();
		String sql = "SELECT id, email, password FROM admin WHERE email = ?"; // Id를 통해 관리자 정보를 조회
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

	// 3. updateAdminInfo : 관리자 정보 수정
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
