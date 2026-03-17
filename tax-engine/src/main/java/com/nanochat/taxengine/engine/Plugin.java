package com.nanochat.taxengine.engine;

import com.nanochat.taxengine.domain.PluginResult;
import com.nanochat.taxengine.domain.PrimitiveType;
import com.nanochat.taxengine.domain.TaxComputationState;
import com.nanochat.taxengine.domain.TaxContext;

import java.util.Set;

public interface Plugin {
    String pluginId();

    PrimitiveType primitive();

    Set<String> dependencies();

    boolean isApplicable(TaxContext context);

    PluginResult evaluate(TaxContext context, TaxComputationState state);
}
