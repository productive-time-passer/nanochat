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
public class AllowanceExemptionPlugin implements Plugin {
    @Override
    public String pluginId() {
        return "allowance-exemption";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.EXEMPTION;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("salary-income");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return context.factIndex().amount("hraReceived").compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state) {
        BigDecimal hraReceived = context.factIndex().amount("hraReceived");
        BigDecimal rentPaid = context.factIndex().amount("rentPaid");
        BigDecimal salary = context.factIndex().amount("salaryIncome");
        BigDecimal allowed = hraReceived.min(rentPaid.subtract(salary.multiply(new BigDecimal("0.10"))).max(BigDecimal.ZERO));

        TaxImpact impact = new TaxImpact(TaxImpactType.REDUCTION, primitive(), "hra", "allowance", hraReceived, hraReceived, allowed,
                Map.of("rentPaid", rentPaid));

        TaxComputation computation = new TaxComputation("cmp-hra", pluginId(), "HRA_EXEMPTION", "Section 10(13A)", primitive(),
                Map.of("hraReceived", hraReceived, "rentPaid", rentPaid), List.of(impact), "Computes HRA exemption", Instant.EPOCH);

        return new PluginResult(List.of(computation));
    }
}
