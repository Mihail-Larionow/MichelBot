package com.michel.MichelBot.service;

import com.michel.MichelBot.config.BotConfig;
import com.michel.MichelBot.config.BotPhrases;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
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
    final BotPhrases phrases;

    public TelegramBot(BotConfig config){
        this.config = config;
        this.phrases = new BotPhrases();
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
                sendSimpleMessage(chatId, phrases.greeting);
                break;
            case "/projects":
                System.out.println("projects");
                sendMessageWithUrl(chatId, phrases.aboutDictophone, phrases.getDictophone());
                sendMessageWithUrl(chatId, phrases.aboutFriendsMap, phrases.getFriendsMap());
                sendMessageWithUrl(chatId, phrases.aboutRubiksCube, phrases.getRubiksCube());
                sendMessageWithUrl(chatId, phrases.aboutWeatherIt, phrases.getWeatherIt());
                break;
            case "/info":
                info(chatId);
                break;
            case "/about":
                about(chatId);
                break;
            default:
                sendSimpleMessage(chatId, phrases.unraritable);
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
        button.setUrl(phrases.getCodeRef());
        rowInLine.add(button);
        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);

        sendSimpleMessage(chatId, phrases.aboutBot, keyboardMarkup);
    }

    private void info(long chatId){
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Telegram");
        button.setUrl(phrases.getTgRef());
        rowInLine.add(button);
        rowsInLine.add(rowInLine);

        rowInLine = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText("VK");
        button.setUrl(phrases.getVkRef());
        rowInLine.add(button);
        rowsInLine.add(rowInLine);

        rowInLine = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText("GitHub");
        button.setUrl(phrases.getGhRef());
        rowInLine.add(button);
        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);

        sendSimpleMessage(chatId, phrases.aboutMihail, keyboardMarkup);
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
        message.setDocument(new InputFile(file));
    }
    

}
