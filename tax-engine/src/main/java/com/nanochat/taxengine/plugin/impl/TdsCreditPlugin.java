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
public class TdsCreditPlugin implements Plugin {
    @Override
    public String pluginId() {
        return "tds-credit";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.TAX_CREDIT;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("slab-tax");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return context.factIndex().amount("tds").compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state) {
        BigDecimal tds = context.factIndex().amount("tds");

        TaxImpact impact = new TaxImpact(TaxImpactType.REDUCTION, primitive(), "tds", "form16", tds, tds, tds, Map.of());
        TaxComputation computation = new TaxComputation("cmp-tds", pluginId(), "TDS", "Tax Deducted at Source", primitive(),
                Map.of("tds", tds), List.of(impact), "Applies TDS credit", Instant.EPOCH);

        return new PluginResult(List.of(computation));
    }
}
