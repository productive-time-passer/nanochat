package com.nanochat.taxengine.domain;

import java.util.Map;

public record TaxContext(
        TaxPayer taxpayer,
        TaxPeriod taxPeriod,
        Map<String, Person> persons,
        FactIndex factIndex
) {
    public TaxContext {
        persons = Map.copyOf(persons);
    }
}
