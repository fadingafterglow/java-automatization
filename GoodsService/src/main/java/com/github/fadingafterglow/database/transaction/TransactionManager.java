package com.github.fadingafterglow.database.transaction;

public interface TransactionManager {

    void beginTransaction(boolean readOnly, TransactionIsolation isolation);

    boolean isTransactionActive();

    Transaction currentTransaction();

    void commitCurrent();

    void rollbackCurrent();
}
