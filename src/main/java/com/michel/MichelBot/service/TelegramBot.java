package com.michel.MichelBot.service;

import com.michel.MichelBot.config.BotConfig;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
    final String goButtonText = "Перейти";

    final String aboutText = "Я - бот-визитка.\n Разработан на Java с использованием фреймворка Spring. Мой исходный код на GitHub";

    public TelegramBot(BotConfig config){
        this.config = config;
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/about", "Information about this bot"));
        commands.add(new BotCommand("/info", "Information about author"));
        commands.add(new BotCommand("/tg", "Telegram reference"));
        commands.add(new BotCommand("/vk", "VK reference"));
        commands.add(new BotCommand("/gh", "GitHub reference"));
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
                sendMessage(chatId, greetingText);
                break;
            case "/tg":
                sendMessageWithUrl(chatId, "Telegram аккаунт Михаила", "TG_BUTTON", config.getTgRef());
                break;
            case "/vk":
                sendMessageWithUrl(chatId, "VK аккаунт Михаила", "VK_BUTTON", config.getVkRef());
                break;
            case "/gh":
                sendMessageWithUrl(chatId, "GitHub профиль Михаила", "GH_BUTTON", config.getGhRef());
                break;
            case "/info":
                info(chatId);
                break;
            case "/about":
                about(chatId);
                break;
            default:
                sendMessage(chatId, "Прости, пока что я слишком глуп и не понимаю чего ты от меня хочешь...");
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

        sendMessage(chatId, aboutText, keyboardMarkup);
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

        sendMessage(chatId, infoText, keyboardMarkup);
    }


    private void sendMessage(long chatId, String text){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try{
            execute(message);
        }catch(TelegramApiException e){

        }
    }

    private void sendMessage(long chatId, String text, ReplyKeyboardMarkup keyboardMarkup){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setReplyMarkup(keyboardMarkup);

        try{
            execute(message);
        }catch(TelegramApiException e){

        }
    }

    private void sendMessage(long chatId, String text, InlineKeyboardMarkup keyboardMarkup){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setReplyMarkup(keyboardMarkup);

        try{
            execute(message);
        }catch(TelegramApiException e){

        }

    }

    private void sendMessageWithUrl(long chatId, String text, String callbackName, String url){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var button = new InlineKeyboardButton();
        button.setText("Перейти");
        button.setCallbackData(callbackName);
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

}
