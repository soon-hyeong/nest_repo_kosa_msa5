package org.kosa.nest.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.kosa.nest.common.DatabaseUtil;

public class FileDao {
	
    public List<FileVO> getAllFileInfoList() throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<FileVO> list = new ArrayList<>();

        try {
            con = DatabaseUtil.getConnection();
            String sql = "SELECT title, tag, created_at FROM file";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String tag = rs.getString("tag");
                java.sql.Date createDate = rs.getDate("created_at");

                list.add(new FileVO(title, tag, createDate));
            }
        } finally {
            DatabaseUtil.closeAll(rs, pstmt, con);
        }

        return list;
    }
    
    /**
     * 데이터베이스에 새로운 파일 정보를 생성합니다(Create). <br>
     * FileVO 객체를 인자로 받아 파일 정보를 전달받고, insert문을 실행하여 데이터베이스에 새로운 파일 정보를 생성합니다. <br>
     * @param fileVO
     * @throws SQLException
     */
    public void createFileInfo(FileVO fileVO) throws SQLException {
    	Connection con = null;
    	PreparedStatement pstmt = null;
    	
    	try {
    		con = DatabaseUtil.getConnection();
    		String sql = "INSERT INTO file (title, tag, description, admin_id, created_at, file_location) "
    				+ " VALUES(?, ?, ?, ?, ?, ?)";
    		pstmt = con.prepareStatement(sql);
    		pstmt.setString(1, fileVO.getSubject());
    		pstmt.setString(2, fileVO.getTag());
    		pstmt.setString(3, fileVO.getDescription());
    		pstmt.setInt(4, fileVO.getAdminId());
    		pstmt.setTimestamp(5, Timestamp.valueOf(fileVO.getCreatedAt()));
    		pstmt.setString(6, fileVO.getFileLocation()); 
    		pstmt.executeUpdate();
    		
    	} finally {
    		DatabaseUtil.closeAll(pstmt, con);
    	}
    }
    
    /**
     * 데이터베이스에 저장되어 있는 파일 정보를 업데이트 합니다(Update). <br>
     * 바꿀 파일의 이름과 FileVO 객체를 인자로 받아 파일 이름, 태그, 설명, admin_id, file_location을 변경합니다. <br>
     * Update문을 실행합니다. <br>
     * @param currentTitle
     * @param fileVO
     * @throws SQLException
     */
    public void updateFileInfo(String currentTitle, FileVO fileVO) throws SQLException {
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
    		pstmt.setString(6, currentTitle);
    		pstmt.executeUpdate();
    	} finally {
    		DatabaseUtil.closeAll(pstmt, con);
    	}
    }
    
    /**
     * 데이터베이스에 저장되어 있는 파일 정보를 삭제합니다(Delete). <br>
     * 인자로 삭제하고자 하는 파일 정보의 제목을 전달받아 DELETE 문을 실행합니다.
     * @param title
     * @throws SQLException
     */
    public void deleteFileInfo(String title) throws SQLException {
    	Connection con = null;
    	PreparedStatement pstmt = null;
    	
    	try {
    		con = DatabaseUtil.getConnection();
    		String sql = "DELETE FROM file WHERE title = ?";
    		pstmt = con.prepareStatement(sql);
    		pstmt.setString(1, title);
    		pstmt.executeUpdate();
    	} finally{
    		DatabaseUtil.closeAll(pstmt, con);
    	}
    }

    /**
     * tag를 통해 사용자가 원하는 파일의 모든 정보를 가져옴<br>
     * @param keyword
     * @return
     * @throws SQLException
     */
    public ArrayList<FileVO> getFileInfoList(String keyword) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<FileVO> list = new ArrayList<>();

        try {
            con = DatabaseUtil.getConnection();
            String sql = "SELECT title " +
                         "FROM file WHERE title LIKE ? OR tag LIKE ? OR created_at LIKE ?";
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            pstmt.setString(3, keyword + "%");

            rs = pstmt.executeQuery();

            while (rs.next()) {
                String subject = rs.getString("title");
                list.add(new FileVO(subject));
            }
        } finally {
            DatabaseUtil.closeAll(rs, pstmt, con);
        }

        return list;
    }

    public ArrayList<FileVO> getFileInfo(String keyword) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<FileVO> list = new ArrayList<>();

        try {
            con = DatabaseUtil.getConnection();
            String sql = "SELECT id, uploaded_time, title, tag, description, admin_id, created_at, file_location " +
                         "FROM file WHERE title = ?";
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, keyword);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int fileId = rs.getInt("id");
                LocalDateTime uploadAt = rs.getTimestamp("uploaded_time").toLocalDateTime();
                String subject = rs.getString("title");
                String tag = rs.getString("tag");
                String description = rs.getString("description");
                int adminId = rs.getInt("admin_id");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                String filelocation = rs.getString("file_location");

                list.add(new FileVO(fileId, filelocation, createdAt, uploadAt, adminId, subject, tag, description));
            }
        } finally {
            DatabaseUtil.closeAll(rs, pstmt, con);
        }

        return list;
    }
    
}
