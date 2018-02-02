package ru.nath.cache;

public interface Cache<symbol, coinObject> {
    void put(symbol key, coinObject value);

    coinObject get(symbol key);

    void remove(symbol key);

    void clear();

    boolean containsKey(symbol key);
}
