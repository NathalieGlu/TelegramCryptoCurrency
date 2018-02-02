package ru.nath.api;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import ru.nath.storage.CoinStorage;
import ru.nath.model.CoinData;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class CoinMarket {

    private static final String jsonUrl = "https://api.coinmarketcap.com/v1/ticker/?convert=RUB&limit=10000";

    private RestTemplate restTemplate;
    private CoinStorage storage;
    private static final Logger log = Logger.getLogger(CoinMarket.class.getName());


    public CoinMarket() {
        this.restTemplate = new RestTemplate();
        this.storage = new CoinStorage();

        getData();
    }

    @Scheduled(fixedDelay = 300 * 1000)
    @GetMapping
    private @ResponseBody
    void getData() {
        ResponseEntity<List<CoinData>> rateResponse =
                restTemplate.exchange(jsonUrl, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<CoinData>>() {
                        });

        List<CoinData> coinData = rateResponse.getBody();

        for (CoinData data : coinData) {
            storage.put(data.getSymbol().toLowerCase(), data);
        }
        log.info("Got data from url");
    }

    public boolean containsCoin(String coinName) {
        return storage.contains(coinName);
    }

    public String getName(String coinName) {
        return storage.getName(coinName);
    }

    public String getRank(String coinName) {
        return storage.getRank(coinName);
    }

    public String getPrice_usd(String coinName) {
        return storage.getPrice_usd(coinName);
    }

    public String getPercent_change_24h(String coinName) {
        return storage.getPercent_change_24h(coinName);
    }

    public String getPrice_rub(String coinName) {
        return storage.getPrice_rub(coinName);
    }

    public Map<String, CoinData> sortMap() {
        return storage.sortMap();
    }

    public String getCoinInfo(String coin) {
        return String.format("%s (%s): %s USD, %s RUB, percent change(24h): %s, rank: %s",
                getName(coin), coin, getPrice_usd(coin), getPrice_rub(coin),
                getPercent_change_24h(coin), getRank(coin));
    }
}
