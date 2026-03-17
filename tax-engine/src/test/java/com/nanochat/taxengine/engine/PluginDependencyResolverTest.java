package com.nanochat.taxengine.engine;

import com.nanochat.taxengine.domain.PluginResult;
import com.nanochat.taxengine.domain.PrimitiveType;
import com.nanochat.taxengine.domain.TaxComputationState;
import com.nanochat.taxengine.domain.TaxContext;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PluginDependencyResolverTest {

    private final PluginDependencyResolver resolver = new PluginDependencyResolver();

    @Test
    void shouldResolveDependenciesInOrder() {
        Plugin a = plugin("a", Set.of());
        Plugin b = plugin("b", Set.of("a"));
        Plugin c = plugin("c", Set.of("b"));

        ExecutionPlan plan = resolver.resolve(List.of(c, a, b));

        assertThat(plan.orderedPlugins()).extracting(Plugin::pluginId).containsExactly("a", "b", "c");
    }

    @Test
    void shouldThrowWhenCycleExists() {
        Plugin a = plugin("a", Set.of("b"));
        Plugin b = plugin("b", Set.of("a"));

        assertThatThrownBy(() -> resolver.resolve(List.of(a, b)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cycle");
    }

    private Plugin plugin(String id, Set<String> dependencies) {
        return new Plugin() {
            @Override
            public String pluginId() { return id; }
            @Override
            public PrimitiveType primitive() { return PrimitiveType.INCOME; }
            @Override
            public Set<String> dependencies() { return dependencies; }
            @Override
            public boolean isApplicable(TaxContext context) { return true; }
            @Override
            public PluginResult evaluate(TaxContext context, TaxComputationState state) { return PluginResult.empty(); }
        };
    }
}
