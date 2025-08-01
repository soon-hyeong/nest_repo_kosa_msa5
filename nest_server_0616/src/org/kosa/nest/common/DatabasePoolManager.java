package org.kosa.nest.common;

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
}
