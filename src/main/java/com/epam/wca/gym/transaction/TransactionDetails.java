package com.epam.wca.gym.transaction;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Getter
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class TransactionDetails {
    private String id;

    public void setId(String id) {
        this.id = id;
    }
}
