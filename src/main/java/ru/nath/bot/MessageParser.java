package ru.nath.bot;

import ru.nath.api.UsersDatabase;
import ru.nath.api.CoinMarket;
import ru.nath.model.CoinData;

import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

public class MessageParser {

    private final String startMessage = "/symbol - info about cryptocurrency\n" +
            "/add symbol amount - add said amount of coins to wallet\n" +
            "/wallet - calculate your finances\n" +
            "/stop - delete wallet\n";
    private final static int NUMBER_OF_LINES = 25;

    private static final Logger log = Logger.getLogger(MessageParser.class.getName());

    private CoinMarket coinMarket;
    private UsersDatabase userBase;

    MessageParser() {
        this.coinMarket = new CoinMarket();
        this.userBase = new UsersDatabase();
    }

    public String parseMessage(String message, String chat_id) {

        String answer = "";
        if (message.startsWith("/start")) {
            userBase.addUser(chat_id);
            answer = startMessage;
        } else if (message.startsWith("/stop")) {
            log.info("Parsing /stop message");
            answer = userBase.deleteUser(chat_id);
        } else if (message.startsWith("/coins")) {
            log.info("Parsing /coins message");
            answer = coinList();
        } else if (message.startsWith("/add")) {
            log.info("Parsing /add message");
            answer = parseAdd(message, chat_id);
        } else if (message.startsWith("/wallet")) {
            log.info("Parsing /wallet message");
            answer = userBase.calculateWallet(coinMarket, chat_id);
        } else if (coinMarket.containsCoin(message.substring(1))) {
            log.info("Parsing /symbol message");
            answer = coinMarket.getCoinInfo(message.substring(1));
        } else {
            answer = "Command not found!";
            log.info(answer);
        }
        return answer;
    }

    private String parseAdd(String message, String chat_id) {
        String[] str = message.split(" ");
        if (str.length != 3) {
            log.info("Wrong input");
            return "Wrong input";
        } else if (coinMarket.containsCoin(str[1])) {
            return userBase.addToSettings(chat_id, str[1], Double.parseDouble(str[2]));
        } else {
            log.info("Wrong symbol");
            return "No such symbol";
        }
    }

    private String coinList() {
        String answer = "";
        Map<String, CoinData> sortedCoins = coinMarket.sortMap();
        Iterator<Map.Entry<String, CoinData>> iter = sortedCoins.entrySet().iterator();

        for (int i = 0; i < NUMBER_OF_LINES; i++) {
            Map.Entry<String, CoinData> entry = iter.next();
            answer += String.format("%d. ", i + 1) + coinMarket.getName(entry.getKey()) + "\n";
        }
        return answer;
    }
}