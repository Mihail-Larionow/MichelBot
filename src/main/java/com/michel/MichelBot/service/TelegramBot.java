package com.michel.MichelBot.service;

import com.michel.MichelBot.config.BotConfig;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Component
@PropertySource("application.properties")
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    final String greetingText = """
            Привет! Я бот-помощник Михаила!
            Чем я могу Вам помочь?
            """;

    final String infoText = """
            Привет! Меня зовут Ларионов Михаил!
            """;

    final String aboutText = "Я - бот-визитка.\n Разработан на Java с использованием фреймворка Spring. Мой исходный код на GitHub";

    final String aboutDictophone = "Dictophone - приложение для сохранения аудио заметок.\n";
    final String aboutFriendsMap = "Friends Map - приложение, которое позволяет увидеть где находятся твои друзья VK.\n";
    final String aboutRubikCube = "Rubik's Cube - приложение, позволяющее понастольгировать, играя в кубик рубика.\n";
    final String aboutWeatherIt = "WeatherIt - самое простое погодное приложение.\n";

    public TelegramBot(BotConfig config){
        this.config = config;
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/about", "Information about this bot"));
        commands.add(new BotCommand("/info", "Information about author"));
        commands.add(new BotCommand("/projects", "Other projects"));
        try{
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        }catch(TelegramApiException e){

        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            handleMessage(chatId, messageText);
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            handleCallback(chatId, callbackData);
        }

    }

    private void handleMessage(long chatId, String messageText){
        switch (messageText){
            case "/start":
                sendSimpleMessage(chatId, greetingText);
                break;
            case "/projects":
                sendMessageWithUrl(chatId, aboutDictophone, config.getDictophone());
                sendMessageWithUrl(chatId, aboutFriendsMap, config.getFriendsMap());
                sendMessageWithUrl(chatId, aboutRubikCube, config.getRubiksCube());
                sendMessageWithUrl(chatId, aboutWeatherIt, config.getWeatherIt());
                break;
            case "/info":
                info(chatId);
                break;
            case "/about":
                about(chatId);
                break;
            default:
                sendSimpleMessage(chatId, "Прости, пока что я слишком глуп и не понимаю чего ты от меня хочешь...");
        }
    }

    private void handleCallback(long chatId, String callbackData){
        switch (callbackData){

        }

    }

    private void about(long chatId){
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("MichelBot");
        button.setUrl(config.getCodeRef());
        rowInLine.add(button);
        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);

        sendSimpleMessage(chatId, aboutText, keyboardMarkup);
    }

    private void info(long chatId){
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Telegram");
        button.setUrl(config.getTgRef());
        rowInLine.add(button);
        rowsInLine.add(rowInLine);

        rowInLine = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText("VK");
        button.setUrl(config.getVkRef());
        rowInLine.add(button);
        rowsInLine.add(rowInLine);

        rowInLine = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText("GitHub");
        button.setUrl(config.getGhRef());
        rowInLine.add(button);
        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);

        sendSimpleMessage(chatId, infoText, keyboardMarkup);
    }


    private void sendSimpleMessage(long chatId, String text){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try{
            execute(message);
        }catch(TelegramApiException e){

        }
    }

    private void sendSimpleMessage(long chatId, String text, ReplyKeyboardMarkup keyboardMarkup){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setReplyMarkup(keyboardMarkup);

        try{
            execute(message);
        }catch(TelegramApiException e){

        }
    }

    private void sendSimpleMessage(long chatId, String text, InlineKeyboardMarkup keyboardMarkup){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setReplyMarkup(keyboardMarkup);

        try{
            execute(message);
        }catch(TelegramApiException e){

        }

    }

    private void sendMessageWithUrl(long chatId, String text, String url){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var button = new InlineKeyboardButton();
        button.setText("Перейти");
        button.setUrl(url);

        rowInLine.add(button);
        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);
        message.setReplyMarkup(keyboardMarkup);

        try{
            execute(message);
        }catch(TelegramApiException e){

        }
    }

    private void sendFile(long chatId, String text, File file){
        SendDocument message = new SendDocument();
        message.setChatId(String.valueOf(chatId));
        message.setCaption(text);

    }

}
