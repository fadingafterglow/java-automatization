package com.github.fadingafterglow.database.connection;

import com.github.fadingafterglow.exception.DataBaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DefaultConnectionFactory implements IConnectionFactory {

    private final String url;
    private final String username;
    private final String password;

    public DefaultConnectionFactory(Properties properties) {
        this.url = properties.getProperty("jdbc.url");
        this.username = properties.getProperty("jdbc.username");
        this.password = properties.getProperty("jdbc.password");
    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        }
        catch (SQLException e) {
            throw new DataBaseException(e);
        }
    }
}
