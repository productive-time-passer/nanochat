package com.nanochat.taxengine.domain;

import java.math.BigDecimal;
import java.util.Map;

public record TaxImpact(
        TaxImpactType taxImpactType,
        PrimitiveType primitiveType,
        String category,
        String subCategory,
        BigDecimal amount,
        BigDecimal eligibleAmount,
        BigDecimal allowedAmount,
        Map<String, Object> attributes
) {
}
