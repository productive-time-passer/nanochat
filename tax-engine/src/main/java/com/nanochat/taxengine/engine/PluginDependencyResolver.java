package com.nanochat.taxengine.engine;

import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class PluginDependencyResolver {

    public ExecutionPlan resolve(List<Plugin> plugins) {
        Map<String, Plugin> byId = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();
        Map<String, Set<String>> dependents = new HashMap<>();
        for (Plugin plugin : plugins) {
            byId.put(plugin.pluginId(), plugin);
            indegree.put(plugin.pluginId(), 0);
            dependents.put(plugin.pluginId(), new HashSet<>());
        }

        for (Plugin plugin : plugins) {
            for (String dependency : plugin.dependencies()) {
                if (byId.containsKey(dependency)) {
                    indegree.compute(plugin.pluginId(), (k, v) -> v + 1);
                    dependents.get(dependency).add(plugin.pluginId());
                }
            }
        }

        ArrayDeque<String> queue = new ArrayDeque<>(indegree.entrySet().stream()
                .filter(e -> e.getValue() == 0)
                .map(Map.Entry::getKey)
                .sorted()
                .toList());

        List<Plugin> ordered = new ArrayList<>();
        while (!queue.isEmpty()) {
            String current = queue.removeFirst();
            ordered.add(byId.get(current));
            for (String dependent : dependents.get(current).stream().sorted().toList()) {
                int newValue = indegree.compute(dependent, (k, v) -> v - 1);
                if (newValue == 0) {
                    queue.addLast(dependent);
                }
            }
        }

        if (ordered.size() != plugins.size()) {
            throw new IllegalStateException("Cycle detected in plugin dependency graph");
        }

        return new ExecutionPlan(ordered);
    }
}
