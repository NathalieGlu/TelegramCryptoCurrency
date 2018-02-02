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
    String price_usd;
    @JsonProperty("price_btc")
    String price_btc;
    @JsonProperty("24h_volume_usd")
    String day_volume_usd;
    @JsonProperty("market_cap_usd")
    String market_cap_usd;
    @JsonProperty("available_supply")
    String available_supply;
    @JsonProperty("total_supply")
    String total_supply;
    @JsonProperty("max_supply")
    String max_supply;
    @JsonProperty("percent_change_1h")
    String percent_change_1h;
    @JsonProperty("percent_change_24h")
    String percent_change_24h;
    @JsonProperty("percent_change_7d")
    String percent_change_7d;
    @JsonProperty("last_updated")
    String last_updated;
    @JsonProperty("price_rub")
    String price_rub;
    @JsonProperty("24h_volume_rub")
    String day_volume_rub;
    @JsonProperty("market_cap_rub")
    String market_cap_rub;

    public String getName() {
        return this.name;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public String getRank() {
        return this.rank;
    }

    public String getPrice_usd() {
        return this.price_usd;
    }

    public String getPercent_change_24h() {
        return this.percent_change_24h;
    }

    public String getPrice_rub() {
        return this.price_rub;
    }
}
