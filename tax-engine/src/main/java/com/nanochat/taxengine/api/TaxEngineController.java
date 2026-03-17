package com.nanochat.taxengine.api;

import com.nanochat.taxengine.domain.FactIndex;
import com.nanochat.taxengine.domain.TaxContext;
import com.nanochat.taxengine.domain.TaxEngineResult;
import com.nanochat.taxengine.domain.TaxPayer;
import com.nanochat.taxengine.domain.TaxPeriod;
import com.nanochat.taxengine.engine.TaxEngine;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/tax")
public class TaxEngineController {

    private final TaxEngine taxEngine;

    public TaxEngineController(TaxEngine taxEngine) {
        this.taxEngine = taxEngine;
    }

    @PostMapping("/compute")
    public TaxEngineResult compute(@Valid @RequestBody ComputeTaxRequest request) {
        TaxContext context = new TaxContext(
                new TaxPayer(request.taxpayerId(), request.age(), request.residencyStatus(), request.regime()),
                new TaxPeriod(request.financialYear(), request.assessmentYear()),
                Map.of(),
                new FactIndex(request.facts() == null ? Map.of() : request.facts())
        );

        return taxEngine.compute(context);
    }
}
