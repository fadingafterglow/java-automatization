package com.github.fadingafterglow;

import com.github.fadingafterglow.database.context.PersistenceContext;
import com.github.fadingafterglow.database.migration.DefaultMigrationRunner;
import com.github.fadingafterglow.database.transaction.TransactionDelegate;
import com.github.fadingafterglow.repository.GoodsRepository;
import org.junit.jupiter.api.AfterEach;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Properties;

public abstract class BaseTest {

    @SuppressWarnings("resource")
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:17.2")
            .withUsername("tests")
            .withPassword("tests")
            .withDatabaseName("tests");;

    static {
        POSTGRESQL_CONTAINER.start();

        Properties properties = new Properties();
        properties.setProperty("jdbc.url", POSTGRESQL_CONTAINER.getJdbcUrl());
        properties.setProperty("jdbc.username", POSTGRESQL_CONTAINER.getUsername());
        properties.setProperty("jdbc.password", POSTGRESQL_CONTAINER.getPassword());
        PersistenceContext.init(properties);

        new DefaultMigrationRunner().runMigrations();
    }

    protected final TransactionDelegate transactionDelegate;
    protected final GoodsRepository goodsRepository;

    protected BaseTest() {
        this.transactionDelegate = new TransactionDelegate();
        this.goodsRepository = new GoodsRepository();
    }

    @AfterEach
    void cleanUp() {
        transactionDelegate.runInTransaction(goodsRepository::deleteAll);
    }
}
