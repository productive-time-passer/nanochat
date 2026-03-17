package com.nanochat.taxengine.engine;

import com.nanochat.taxengine.domain.TaxComputationState;
import com.nanochat.taxengine.domain.TaxContext;
import org.springframework.stereotype.Component;

@Component
public class PluginExecutor {
    public void execute(ExecutionPlan plan, TaxContext context, TaxComputationState state) {
        for (Plugin plugin : plan.orderedPlugins()) {
            state.addComputations(plugin.evaluate(context, state).computations());
        }
    }
}
