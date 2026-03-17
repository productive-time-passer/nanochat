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
public class ItrFilingCompliancePlugin implements Plugin {
    @Override
    public String pluginId() {
        return "itr-compliance";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.COMPLIANCE_OBLIGATION;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("slab-tax");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return true;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state) {
        BigDecimal grossIncome = state.totalFor(PrimitiveType.INCOME);
        BigDecimal threshold = new BigDecimal("300000");
        BigDecimal obligation = grossIncome.compareTo(threshold) > 0 ? BigDecimal.ONE : BigDecimal.ZERO;

        TaxImpact impact = new TaxImpact(TaxImpactType.OBLIGATION, primitive(), "ITR_FILING", "itr-1-or-2", obligation, obligation, obligation,
                Map.of("threshold", threshold));

        TaxComputation computation = new TaxComputation("cmp-itr", pluginId(), "ITR_FILE", "ITR filing obligation", primitive(),
                Map.of("grossIncome", grossIncome), List.of(impact), "Flags filing obligation", Instant.EPOCH);

        return new PluginResult(List.of(computation));
    }
}
