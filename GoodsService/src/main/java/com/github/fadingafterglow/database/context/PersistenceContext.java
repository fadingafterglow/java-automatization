package com.github.fadingafterglow.database.context;

import com.github.fadingafterglow.database.connection.DefaultConnectionFactory;
import com.github.fadingafterglow.database.connection.IConnectionFactory;
import com.github.fadingafterglow.database.transaction.DefaultTransactionManager;
import com.github.fadingafterglow.database.transaction.TransactionManager;
import lombok.Getter;

import java.util.Properties;

public class PersistenceContext {

    @Getter
    private static PersistenceContext instance;

    @Getter
    private final TransactionManager transactionManager;

    private PersistenceContext(IConnectionFactory connectionFactory) {
        this.transactionManager = new DefaultTransactionManager(connectionFactory);
    }

    public synchronized static void init(Properties properties) {
        if (instance != null) return;
        IConnectionFactory connectionFactory = new DefaultConnectionFactory(properties);
        instance = new PersistenceContext(connectionFactory);
    }
}
