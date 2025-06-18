package org.kosa.nest.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.kosa.nest.common.DatabaseUtil;

public class FileDao {
	
    public List<FileVO> getFileinfoList() throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<FileVO> list = new ArrayList<>();

        try {
            con = DatabaseUtil.getConnection();
            String sql = "SELECT title, tag, file_create_time FROM file";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String tag = rs.getString("tag");
                java.sql.Date createDate = rs.getDate("file_create_time");

                list.add(new FileVO(title, tag, createDate));
            }
        } finally {
            DatabaseUtil.closeAll(rs, pstmt, con);
        }

        return list;
    }
    
    public void createFileInfo(FileVO fileVO) throws SQLException {
    	Connection con = null;
    	PreparedStatement pstmt = null;
    	
    	try {
    		con = DatabaseUtil.getConnection();
    		String sql = "INSERT INTO file (uploaded_time, title, tag, description, admin_id, created_at, file_location) "
    				+ " VALUES(?, ?, ?, ?, ?, ?, ?)";
    		pstmt = con.prepareStatement(sql);
    		pstmt.setTimestamp(1, Timestamp.valueOf(fileVO.getUploadAt()));
    		pstmt.setString(2, fileVO.getSubject());
    		pstmt.setString(3, fileVO.getTag());
    		pstmt.setString(4, fileVO.getDescription());
    		pstmt.setInt(5, fileVO.getAdminId());
    		pstmt.setTimestamp(6, Timestamp.valueOf(fileVO.getCreatedAt()));
    		pstmt.setString(7, fileVO.getSubject());    		
    		
    	} finally {
    		DatabaseUtil.closeAll(pstmt, con);
    	}
    }
    
    public void updateFileInfo(FileVO fileVO) throws SQLException {
    	Connection con = null;
    	PreparedStatement pstmt = null;
    	
    	try {
    		con = DatabaseUtil.getConnection();
    		// 파일 정보 업데이트시 제목, 이름, 태그, 설명, 관리자아이디, 파일위치만 변경 가능
    		String sql = "UPDATE file SET title = ?, tag = ?, description = ?, admin_id = ?, file_location = ? WHERE title = ?";
    		pstmt = con.prepareStatement(sql);
    		pstmt.setString(1, fileVO.getSubject());
    		pstmt.setString(2, fileVO.getTag());
    		pstmt.setString(3, fileVO.getDescription());
    		pstmt.setInt(4, fileVO.getAdminId());
    		pstmt.setString(5, fileVO.getFileLocation());
    		pstmt.setString(6, fileVO.getSubject());
    		pstmt.executeUpdate();
    	} finally {
    		DatabaseUtil.closeAll(pstmt, con);
    	}
    }
    
    public void deleteFileInfo(FileVO fileVO) throws SQLException {
    	Connection con = null;
    	PreparedStatement pstmt = null;
    	
    	try {
    		con = DatabaseUtil.getConnection();
    		String sql = "DELETE FROM file WHERE subject = ?";
    		pstmt = con.prepareStatement(sql);
    		pstmt.setString(1, fileVO.getSubject());
    		pstmt.executeQuery();
    	} finally{
    		DatabaseUtil.closeAll(pstmt, con);
    	}
    }
}
