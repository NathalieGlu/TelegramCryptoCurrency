package ru.nath.storage;

import ru.nath.cache.MemoCache;
import ru.nath.model.CoinData;

import java.util.Map;
import java.util.Set;

public class CoinStorage {

    MemoCache<String, CoinData> cache;

    public CoinStorage() {
        this.cache = new MemoCache<>();
    }

    public boolean contains(String key) {
        return cache.containsKey(key);
    }

    public String getName(String coinName) {
        return cache.get(coinName).getName();
    }

    public String getRank(String coinName) {
        return cache.get(coinName).getRank();
    }

    public String getPrice_usd(String coinName) {
        return cache.get(coinName).getPrice_usd();
    }

    public String getPercent_change_24h(String coinName) {
        return cache.get(coinName).getPercent_change_24h();
    }

    public String getPrice_rub(String coinName) {
        return cache.get(coinName).getPrice_rub();
    }

    public Set<String> getCoinsNames() {
        return cache.getAllKeys();
    }

    public void put(String key, CoinData value) {
        cache.put(key, value);
    }

    public Map<String, CoinData> sortMap() {
        return cache.sortMap();
    }
}
