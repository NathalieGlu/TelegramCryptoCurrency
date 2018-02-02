package ru.nath.main;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.nath.bot.TelegramBot;

public class Main {

    public static void main(String[] args) {

        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new TelegramBot("ngCryptoCurrency_bot", "322898449:AAFATC0gubISixaN7hCxkH0WO4qRJybr0Vw"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}