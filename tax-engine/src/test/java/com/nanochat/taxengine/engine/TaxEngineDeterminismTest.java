package com.nanochat.taxengine.engine;

import com.nanochat.taxengine.domain.FactIndex;
import com.nanochat.taxengine.domain.TaxContext;
import com.nanochat.taxengine.domain.TaxEngineResult;
import com.nanochat.taxengine.domain.TaxPayer;
import com.nanochat.taxengine.domain.TaxPeriod;
import com.nanochat.taxengine.plugin.impl.AllowanceExemptionPlugin;
import com.nanochat.taxengine.plugin.impl.InvestmentDeductionPlugin;
import com.nanochat.taxengine.plugin.impl.ItrFilingCompliancePlugin;
import com.nanochat.taxengine.plugin.impl.SalaryIncomeClassificationPlugin;
import com.nanochat.taxengine.plugin.impl.SlabTaxPlugin;
import com.nanochat.taxengine.plugin.impl.TdsCreditPlugin;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TaxEngineDeterminismTest {

    @Test
    void shouldProduceDeterministicOutputForSameContext() {
        TaxEngine engine = new TaxEngine(
                new PluginRegistry(List.of(
                        new SalaryIncomeClassificationPlugin(),
                        new AllowanceExemptionPlugin(),
                        new InvestmentDeductionPlugin(),
                        new SlabTaxPlugin(),
                        new TdsCreditPlugin(),
                        new ItrFilingCompliancePlugin()
                )),
                new PluginDependencyResolver(),
                new PluginExecutor(),
                new ResultAssembler()
        );

        TaxContext context = new TaxContext(
                new TaxPayer("PAN123", 35, "RESIDENT", "OLD"),
                new TaxPeriod("2024-25", "2025-26"),
                Map.of(),
                new FactIndex(Map.of(
                        "salaryIncome", new BigDecimal("1200000"),
                        "hraReceived", new BigDecimal("240000"),
                        "rentPaid", new BigDecimal("300000"),
                        "section80cInvestment", new BigDecimal("180000"),
                        "tds", new BigDecimal("90000")
                ))
        );

        TaxEngineResult first = engine.compute(context);
        TaxEngineResult second = engine.compute(context);

        assertThat(first).isEqualTo(second);
        assertThat(first.finalTaxPayable()).isEqualByComparingTo("85000.00");
    }
}
