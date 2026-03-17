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
public class SalaryIncomeClassificationPlugin implements Plugin {
    @Override
    public String pluginId() {
        return "salary-income";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.INCOME;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of();
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return context.factIndex().amount("salaryIncome").compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state) {
        BigDecimal salary = context.factIndex().amount("salaryIncome");
        TaxImpact impact = new TaxImpact(TaxImpactType.ADDITION, primitive(), "salary", "gross", salary, salary, salary, Map.of());
        TaxComputation computation = new TaxComputation("cmp-salary", pluginId(), "SALARY_BASE", "Salary Income", primitive(),
                Map.of("salaryIncome", salary), List.of(impact), "Classifies salary into taxable income universe", Instant.EPOCH);
        return new PluginResult(List.of(computation));
    }
}
