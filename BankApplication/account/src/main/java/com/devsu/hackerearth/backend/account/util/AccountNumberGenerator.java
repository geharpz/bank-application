package com.devsu.hackerearth.backend.account.util;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

@Component
public class AccountNumberGenerator {

    private static final String ENTITY_CODE = "3021";
    private static final String BRANCH_CODE = "0456";
    private static final int SEQUENCE_LENGTH = 8;

    private final AtomicLong sequence = new AtomicLong(1);

    public String generate() {
        long seq = sequence.getAndIncrement();
        String sequential = String.format("%0" + SEQUENCE_LENGTH + "d", seq);
        return ENTITY_CODE + BRANCH_CODE + sequential;
    }
}
