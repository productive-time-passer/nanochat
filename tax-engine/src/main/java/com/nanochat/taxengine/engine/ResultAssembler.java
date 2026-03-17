package com.nanochat.taxengine.engine;

import com.nanochat.taxengine.domain.PrimitiveType;
import com.nanochat.taxengine.domain.TaxComputationState;
import com.nanochat.taxengine.domain.TaxEngineResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ResultAssembler {

    public TaxEngineResult build(TaxComputationState state) {
        BigDecimal grossIncome = state.totalFor(PrimitiveType.INCOME);
        BigDecimal exemptions = state.totalFor(PrimitiveType.EXEMPTION);
        BigDecimal deductions = state.totalFor(PrimitiveType.DEDUCTION);
        BigDecimal adjustments = state.totalFor(PrimitiveType.ADJUSTMENT);
        BigDecimal taxableIncome = grossIncome.subtract(exemptions).subtract(deductions).subtract(adjustments).max(BigDecimal.ZERO);
        BigDecimal taxBeforeCredits = state.totalFor(PrimitiveType.TAX_RATE_APPLICATION);
        BigDecimal taxCredits = state.totalFor(PrimitiveType.TAX_CREDIT);
        BigDecimal finalTaxPayable = taxBeforeCredits.subtract(taxCredits).max(BigDecimal.ZERO);

        return new TaxEngineResult(
                grossIncome,
                exemptions,
                deductions,
                taxableIncome,
                taxBeforeCredits,
                taxCredits,
                finalTaxPayable,
                state.impacts(PrimitiveType.INCOME),
                state.impacts(PrimitiveType.DEDUCTION),
                state.impacts(PrimitiveType.EXEMPTION),
                state.impacts(PrimitiveType.ADJUSTMENT),
                state.impacts(PrimitiveType.TAX_RATE_APPLICATION),
                state.impacts(PrimitiveType.TAX_CREDIT),
                state.impacts(PrimitiveType.COMPLIANCE_OBLIGATION),
                state.computations()
        );
    }
}
