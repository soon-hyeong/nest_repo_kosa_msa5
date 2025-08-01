package org.kosa.nest.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

public class DatabasePoolManager {
    
    private static DatabasePoolManager instance;
    
    private DataSource dataSource;
    
    private DatabasePoolManager() {
        BasicDataSource dbcp = new BasicDataSource();
        dbcp.setDriverClassName(DbConfig.DRIVER);
        dbcp.setUrl(DbConfig.URL);
        dbcp.setUsername(DbConfig.USER);
        dbcp.setPassword(DbConfig.PASS);
        this.dataSource = dbcp;
    }
    
    public static DatabasePoolManager getInstance() {
        if(instance == null)
            instance = new DatabasePoolManager();
        return instance;
    }
    
    public DataSource getDataSource() {
        return dataSource;
    }
    
    public static void closeAll(PreparedStatement pstmt, Connection con) throws SQLException {
        if (pstmt != null)
            pstmt.close();
        if (con != null)
            con.close();
    }
    public static void closeAll(ResultSet rs, PreparedStatement pstmt, Connection con) throws SQLException {
        if (rs != null)
            rs.close();
        closeAll(pstmt, con);
    }
    
}
