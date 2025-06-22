package com.github.fadingafterglow.repository;

import com.github.fadingafterglow.database.context.PersistenceContext;
import com.github.fadingafterglow.database.transaction.TransactionManager;

public abstract class BaseRepository {

    protected final TransactionManager transactionManager;

    public BaseRepository() {
        this.transactionManager = PersistenceContext.getInstance().getTransactionManager();
    }
}
