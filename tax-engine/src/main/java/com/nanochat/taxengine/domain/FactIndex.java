package com.nanochat.taxengine.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public final class FactIndex {

    private final Map<String, BigDecimal> values;

    public FactIndex(Map<String, BigDecimal> values) {
        this.values = Map.copyOf(values);
    }

    public static FactIndex empty() {
        return new FactIndex(new HashMap<>());
    }

    public BigDecimal amount(String key) {
        return values.getOrDefault(key, BigDecimal.ZERO);
    }

    public Map<String, BigDecimal> all() {
        return values;
    }
}
