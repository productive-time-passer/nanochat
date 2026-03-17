package com.nanochat.taxengine.engine;

import com.nanochat.taxengine.domain.TaxComputationState;
import com.nanochat.taxengine.domain.TaxContext;
import com.nanochat.taxengine.domain.TaxEngineResult;
import org.springframework.stereotype.Service;

@Service
public class TaxEngine {

    private final PluginRegistry pluginRegistry;
    private final PluginDependencyResolver dependencyResolver;
    private final PluginExecutor executor;
    private final ResultAssembler assembler;

    public TaxEngine(PluginRegistry pluginRegistry, PluginDependencyResolver dependencyResolver, PluginExecutor executor, ResultAssembler assembler) {
        this.pluginRegistry = pluginRegistry;
        this.dependencyResolver = dependencyResolver;
        this.executor = executor;
        this.assembler = assembler;
    }

    public TaxEngineResult compute(TaxContext context) {
        var plugins = pluginRegistry.getPlugins(context);
        var plan = dependencyResolver.resolve(plugins);
        var state = new TaxComputationState();
        executor.execute(plan, context, state);
        return assembler.build(state);
    }
}
