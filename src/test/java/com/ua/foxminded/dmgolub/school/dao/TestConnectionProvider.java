package com.ua.foxminded.dmgolub.school.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TestConnectionProvider implements ConnectionProvider {

    private static final String URL = "jdbc:postgresql://localhost/school_test";
    
    @Override
    public Connection get() {
        Properties properties = new Properties();
        properties.setProperty("user", "dmgolub");
        properties.setProperty("password", "foxminded");
        try {
            return DriverManager.getConnection(URL, properties);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}