package com.nanochat.taxengine.domain;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record TaxComputation(
        String computationId,
        String pluginId,
        String ruleId,
        String sectionReference,
        PrimitiveType primitiveType,
        Map<String, Object> inputs,
        List<TaxImpact> impacts,
        String explanation,
        Instant computedAt
) {
}
