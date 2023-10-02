package com.ua.foxminded.dmgolub.school.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ProdConnectionProvider implements ConnectionProvider {
    
    private Properties properties;
    
    public ProdConnectionProvider(String propertiesFileName) {
        if (propertiesFileName == null) {
            throw new IllegalArgumentException("Properties file name can not be null");
        }
        if (propertiesFileName.isEmpty()) {
            throw new IllegalArgumentException("Properties file name can not be an empty string");
        }
        this.properties = new Properties();
        InputStream stream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);
        if (stream != null) {
            try {
                properties.load(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public Connection get() {
        try {
            return DriverManager.getConnection(properties.getProperty("url"), properties);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }
}