package org.kosa.nest.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
}
