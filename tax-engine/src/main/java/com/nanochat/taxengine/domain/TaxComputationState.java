package com.nanochat.taxengine.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaxComputationState {

    private final List<TaxComputation> computations = new ArrayList<>();
    private final Map<PrimitiveType, List<TaxImpact>> impactsByPrimitive = new EnumMap<>(PrimitiveType.class);
    private final Map<String, BigDecimal> incomeTotals = new HashMap<>();
    private final Map<String, BigDecimal> deductionTotals = new HashMap<>();
    private final Map<String, BigDecimal> taxTotals = new HashMap<>();

    public TaxComputationState() {
        for (PrimitiveType value : PrimitiveType.values()) {
            impactsByPrimitive.put(value, new ArrayList<>());
        }
    }

    public void addComputations(List<TaxComputation> newComputations) {
        for (TaxComputation computation : newComputations) {
            computations.add(computation);
            for (TaxImpact impact : computation.impacts()) {
                impactsByPrimitive.get(impact.primitiveType()).add(impact);
                accumulate(impact);
            }
        }
    }

    private void accumulate(TaxImpact impact) {
        if (impact.primitiveType() == PrimitiveType.INCOME) {
            incomeTotals.merge(impact.category(), impact.allowedAmount(), BigDecimal::add);
        }
        if (impact.primitiveType() == PrimitiveType.DEDUCTION || impact.primitiveType() == PrimitiveType.EXEMPTION || impact.primitiveType() == PrimitiveType.ADJUSTMENT) {
            deductionTotals.merge(impact.category(), impact.allowedAmount(), BigDecimal::add);
        }
        if (impact.primitiveType() == PrimitiveType.TAX_RATE_APPLICATION || impact.primitiveType() == PrimitiveType.TAX_CREDIT) {
            taxTotals.merge(impact.category(), impact.allowedAmount(), BigDecimal::add);
        }
    }

    public List<TaxComputation> computations() {
        return List.copyOf(computations);
    }

    public List<TaxImpact> impacts(PrimitiveType primitiveType) {
        return List.copyOf(impactsByPrimitive.get(primitiveType));
    }

    public BigDecimal totalFor(PrimitiveType primitiveType) {
        return impacts(primitiveType).stream()
                .map(TaxImpact::allowedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
