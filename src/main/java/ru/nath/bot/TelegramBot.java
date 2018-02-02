package ru.nath.bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.logging.Logger;

public class TelegramBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;

    private static final Logger log = Logger.getLogger(TelegramBot.class.getName());

    MessageParser messageParser;

    public TelegramBot(String botUsername, String botToken) {
        this.messageParser = new MessageParser();
        this.botUsername = botUsername;
        this.botToken = botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            log.info(String.format("Message: %s, User: %d", message_text, chat_id));
            String answer = messageParser.parseMessage(message_text, String.valueOf(chat_id));

            SendMessage message = new SendMessage()
                    .setChatId(chat_id)
                    .setText(answer);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
