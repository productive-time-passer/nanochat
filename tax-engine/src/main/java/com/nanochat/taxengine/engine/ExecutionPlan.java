package com.nanochat.taxengine.engine;

import java.util.List;

public record ExecutionPlan(List<Plugin> orderedPlugins) {
}
