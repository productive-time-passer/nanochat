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
public class InvestmentDeductionPlugin implements Plugin {
    private static final BigDecimal CAP_80C = new BigDecimal("150000");

    @Override
    public String pluginId() {
        return "investment-deduction";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.DEDUCTION;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("salary-income");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return context.factIndex().amount("section80cInvestment").compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state) {
        BigDecimal invested = context.factIndex().amount("section80cInvestment");
        BigDecimal allowed = invested.min(CAP_80C);

        TaxImpact impact = new TaxImpact(TaxImpactType.REDUCTION, primitive(), "80C", "investment", invested, invested, allowed,
                Map.of("cap", CAP_80C));

        TaxComputation computation = new TaxComputation("cmp-80c", pluginId(), "80C", "Section 80C", primitive(),
                Map.of("invested", invested), List.of(impact), "Applies 80C cap", Instant.EPOCH);

        return new PluginResult(List.of(computation));
    }
}
