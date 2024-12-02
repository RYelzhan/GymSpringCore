package com.epam.wca.gym.communication.feign.interceptor;

import com.epam.wca.common.gymcommon.exception.InternalErrorException;
import com.epam.wca.common.gymcommon.logging.TransactionContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import static com.epam.wca.common.gymcommon.util.AppConstants.TRANSACTION_ID_HEADER;

public class TransactionIdInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String transactionId = TransactionContext.getTransactionId();

        if (transactionId == null) {
            throw new InternalErrorException("Feign Client call without transaction id.");
        }

        requestTemplate.header(TRANSACTION_ID_HEADER, transactionId);
    }
}
