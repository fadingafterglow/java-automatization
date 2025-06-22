package com.github.fadingafterglow.database.migration;

import com.github.fadingafterglow.database.context.PersistenceContext;
import com.github.fadingafterglow.exception.DataBaseException;
import com.github.fadingafterglow.database.transaction.TransactionDelegate;
import com.github.fadingafterglow.database.transaction.TransactionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DefaultMigrationRunner implements MigrationRunner {

    private static final String DEFAULT_MIGRATIONS_INDEX = "/migrations/index";
    private final String migrationsIndex;

    public DefaultMigrationRunner() {
        this(DEFAULT_MIGRATIONS_INDEX);
    }

    public DefaultMigrationRunner(String migrationsIndex) {
        this.migrationsIndex = migrationsIndex;
    }

    @Override
    public void runMigrations() {
        TransactionDelegate transactionDelegate = new TransactionDelegate();
        for (String migration: findMigrations()) {
            transactionDelegate.runInTransaction(() -> runMigration(migration));
        }
    }

    private void runMigration(String migration) {
        TransactionManager transactionManager = PersistenceContext.getInstance().getTransactionManager();
        try (Statement statement = transactionManager.currentTransaction().createStatement()) {
            statement.execute(migration);
        }
        catch (SQLException e) {
            throw new DataBaseException(e);
        }
    }

    private List<String> findMigrations() {
        try (InputStream index = getClass().getResourceAsStream(migrationsIndex)) {
            List<String> migrationFiles = readAllLines(index);
            List<String> result = new ArrayList<>(migrationFiles.size());
            for (String migrationFile : migrationFiles) {
                try (InputStream migration = getClass().getResourceAsStream(migrationFile)) {
                    if (migration == null) continue;
                    result.add(readAll(migration));
                }
            }
            return result;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readAll(InputStream inputStream) throws IOException {
        byte[] file = inputStream.readAllBytes();
        return new String(file, StandardCharsets.UTF_8);
    }

    private List<String> readAllLines(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream)).lines().toList();
    }
}
