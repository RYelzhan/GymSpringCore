package com.epam.wca.common.gymcommon.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class AppConstants {
    public static final String DEFAULT_DATE_FORMAT = "dd.MM.yyyy HH:mm:ss z";
    public static final String TRANSACTION_ID_HEADER = "TransactionId";
    public static final String TRANSACTION_ID_PROPERTY = "TransactionId";
    public static final String USERNAME_HEADER = "UserId";
    public static final String TRAINING_ADD_QUEUE = "training-add-queue";
    public static final String TRAINING_DELETE_QUEUE = "training-delete-queue";
    public static final String TRAINEE_DELETE_QUEUE = "trainee-delete-queue";
    public static final String TRAINER_DELETE_QUEUE = "trainer-delete-queue";
    public static final String SPAN_ID_HEADER = "X_B3_SpanId";
    public static final String TRACE_ID_HEADER = "X_B3_TraceId";
}
