package com.github.fadingafterglow.database.connection;

import java.sql.Connection;

public interface IConnectionFactory {

    Connection getConnection();
}
