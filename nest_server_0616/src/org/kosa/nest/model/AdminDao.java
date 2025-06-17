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
		String sql = "INSERT INTO admin (id,email, password) VALUES(?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);			
			pstmt.setInt(1, admin.getId());
			pstmt.setString(2, admin.getEmail());
			pstmt.setString(3, admin.getPassword());

			return pstmt.executeUpdate();
		} finally {
			DatabaseUtil.closeAll(pstmt, conn);
		}
	}

	// 2. getAdminInfo : ID로 관리자 정보 조회
	public AdminVO getAdminInfo(int id) throws SQLException {
		String sql = "SELECT id, email, password FROM admin WHERE id = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return new AdminVO(rs.getInt("id"), rs.getString("email"), rs.getString("password"));
			}
		} finally {
			DatabaseUtil.closeAll(rs, pstmt, conn);
		}
		return null; // 해당 ID의 관리자가 없는 경우
	}

	// 3. updateAdminInfo : 관리자 정보 수정
	public int updateAdminInfo(AdminVO admin) throws SQLException {
		String sql = "UPDATE admin SET email = ?, password = ? WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, admin.getEmail());
			pstmt.setString(2, admin.getPassword());
			pstmt.setInt(3, admin.getId());
			return pstmt.executeUpdate();
		}
	}
}
