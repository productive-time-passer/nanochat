package com.nanochat.taxengine.plugin.impl;

import com.nanochat.taxengine.domain.PluginResult;
import com.nanochat.taxengine.domain.PrimitiveType;
import com.nanochat.taxengine.domain.TaxComputation;
import com.nanochat.taxengine.domain.TaxComputationState;
import com.nanochat.taxengine.domain.TaxContext;
import com.nanochat.taxengine.domain.TaxImpact;
import com.nanochat.taxengine.domain.TaxImpactType;
import com.nanochat.taxengine.engine.Plugin;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class SlabTaxPlugin implements Plugin {
    @Override
    public String pluginId() {
        return "slab-tax";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.TAX_RATE_APPLICATION;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("salary-income", "allowance-exemption", "investment-deduction");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return true;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state) {
        BigDecimal gross = state.totalFor(PrimitiveType.INCOME);
        BigDecimal exemptions = state.totalFor(PrimitiveType.EXEMPTION);
        BigDecimal deductions = state.totalFor(PrimitiveType.DEDUCTION);
        BigDecimal taxable = gross.subtract(exemptions).subtract(deductions).max(BigDecimal.ZERO);

        BigDecimal tax = computeOldRegimeTax(taxable);

        TaxImpact impact = new TaxImpact(TaxImpactType.ADDITION, primitive(), "slabTax", "oldRegime", tax, tax, tax,
                Map.of("taxableIncome", taxable));

        TaxComputation computation = new TaxComputation("cmp-slab", pluginId(), "SLAB_2024", "Income Tax Slabs", primitive(),
                Map.of("taxableIncome", taxable), List.of(impact), "Applies slab tax for old regime", Instant.EPOCH);

        return new PluginResult(List.of(computation));
    }

    private BigDecimal computeOldRegimeTax(BigDecimal taxable) {
        BigDecimal remaining = taxable;
        BigDecimal tax = BigDecimal.ZERO;

        BigDecimal slab1 = new BigDecimal("250000");
        BigDecimal slab2 = new BigDecimal("250000");
        BigDecimal slab3 = new BigDecimal("500000");

        if (remaining.compareTo(slab1) <= 0) {
            return BigDecimal.ZERO;
        }
        remaining = remaining.subtract(slab1);

        BigDecimal second = remaining.min(slab2);
        tax = tax.add(second.multiply(new BigDecimal("0.05")));
        remaining = remaining.subtract(second);

        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal third = remaining.min(slab3);
            tax = tax.add(third.multiply(new BigDecimal("0.20")));
            remaining = remaining.subtract(third);
        }

        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            tax = tax.add(remaining.multiply(new BigDecimal("0.30")));
        }

        return tax;
    }
}
