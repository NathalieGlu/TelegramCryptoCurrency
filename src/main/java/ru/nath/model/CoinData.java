package ru.nath.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinData implements Serializable {
    @JsonProperty("id")
    String id;
    @JsonProperty("name")
    String name;
    @JsonProperty("symbol")
    String symbol;
    @JsonProperty("rank")
    String rank;
    @JsonProperty("price_usd")
    String priceUsd;
    @JsonProperty("price_btc")
    String priceBtc;
    @JsonProperty("24h_volume_usd")
    String dayVolumeUsd;
    @JsonProperty("market_cap_usd")
    String marketCapUsd;
    @JsonProperty("available_supply")
    String availableSupply;
    @JsonProperty("total_supply")
    String totalSupply;
    @JsonProperty("max_supply")
    String maxSupply;
    @JsonProperty("percent_change_1h")
    String percentChange1H;
    @JsonProperty("percent_change_24h")
    String percentChange24H;
    @JsonProperty("percent_change_7d")
    String percentChange7D;
    @JsonProperty("last_updated")
    String lastUpdated;
    @JsonProperty("price_rub")
    String priceRub;
    @JsonProperty("24h_volume_rub")
    String dayVolumeRub;
    @JsonProperty("market_cap_rub")
    String marketCapRub;

    public String getName() {
        return this.name;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public String getRank() {
        return this.rank;
    }

    public String getPriceUsd() {
        return this.priceUsd;
    }

    public String getPercentChange24H() {
        return this.percentChange24H;
    }

    public String getPriceRub() {
        return this.priceRub;
    }
}
