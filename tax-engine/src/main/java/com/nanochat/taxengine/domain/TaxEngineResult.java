package com.nanochat.taxengine.domain;

import java.math.BigDecimal;
import java.util.List;

public record TaxEngineResult(
        BigDecimal grossIncome,
        BigDecimal exemptions,
        BigDecimal deductions,
        BigDecimal taxableIncome,
        BigDecimal taxBeforeCredits,
        BigDecimal taxCredits,
        BigDecimal finalTaxPayable,
        List<TaxImpact> incomeBreakdown,
        List<TaxImpact> deductionBreakdown,
        List<TaxImpact> exemptionBreakdown,
        List<TaxImpact> adjustmentBreakdown,
        List<TaxImpact> taxComponents,
        List<TaxImpact> taxCreditsBreakdown,
        List<TaxImpact> complianceObligations,
        List<TaxComputation> computationTrace
) {
}
