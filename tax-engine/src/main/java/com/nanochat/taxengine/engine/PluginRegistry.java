package com.nanochat.taxengine.engine;

import com.nanochat.taxengine.domain.TaxContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PluginRegistry {
    private final List<Plugin> plugins;

    public PluginRegistry(List<Plugin> plugins) {
        this.plugins = List.copyOf(plugins);
    }

    public List<Plugin> getPlugins(TaxContext context) {
        return plugins.stream().filter(plugin -> plugin.isApplicable(context)).toList();
    }
}
