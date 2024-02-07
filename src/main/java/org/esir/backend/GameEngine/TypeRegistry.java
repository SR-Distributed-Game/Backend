package org.esir.backend.GameEngine;

import java.util.HashMap;

public class TypeRegistry {
    private HashMap<String,Class<?>> instance = new HashMap<String,Class<?>>();
    public void registerType(String name, Class<?> type) {
        instance.put(name, type);
    }

    public Class<?> getType(String name) {
        return instance.get(name);
    }

    public boolean containsType(String name) {
        return instance.containsKey(name);
    }
}
