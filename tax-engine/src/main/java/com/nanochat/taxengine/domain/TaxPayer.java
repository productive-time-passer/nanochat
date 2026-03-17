package com.nanochat.taxengine.domain;

public record TaxPayer(
        String taxpayerId,
        int age,
        String residencyStatus,
        String regime
) {
}
