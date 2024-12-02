package com.epam.wca.common.gymcommon.logging;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TransactionContext {
    private static final ThreadLocal<String> transactionIds = new ThreadLocal<>();

    public void setTransactionId(String id) {
        transactionIds.set(id);
    }

    public String getTransactionId() {
        return transactionIds.get();
    }

    public void clear() {
        transactionIds.remove();
    }
}
