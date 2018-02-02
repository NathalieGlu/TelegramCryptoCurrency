package ru.nath.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MemoCache<symbol extends Serializable, coinObject extends Serializable> implements Cache<symbol, coinObject> {
    private Map<symbol, coinObject> objectMap;
    private final int SIZE = 10000;

    public MemoCache() {
        objectMap = new HashMap<symbol, coinObject>(SIZE);
    }

    @Override
    public void put(symbol key, coinObject value) {
        objectMap.put(key, value);
    }

    @Override
    public coinObject get(symbol key) {
        return objectMap.get(key);
    }

    @Override
    public void remove(symbol key) {
        objectMap.remove(key);
    }

    @Override
    public void clear() {
        objectMap.clear();
    }

    @Override
    public boolean containsKey(symbol key) {
        return objectMap.containsKey(key);
    }

    public Set<symbol> getAllKeys() {
        return objectMap.keySet();
    }

    public Map<symbol, coinObject> sortMap() {
        return new TreeMap<symbol, coinObject>(objectMap);
    }
}
