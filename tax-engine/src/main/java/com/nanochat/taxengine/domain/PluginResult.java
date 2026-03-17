package com.nanochat.taxengine.domain;

import java.util.List;

public record PluginResult(List<TaxComputation> computations) {

    public static PluginResult empty() {
        return new PluginResult(List.of());
    }
}
