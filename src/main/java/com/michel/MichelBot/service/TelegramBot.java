package com.michel.MichelBot.service;

import com.michel.MichelBot.config.BotConfig;
import com.michel.MichelBot.config.BotPhrases;
import com.michel.MichelBot.info.Projects;
import com.michel.MichelBot.logger.Logger;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
@PropertySource("application.properties")
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;
    private final BotPhrases phrases;
    private final Logger logger;

    public TelegramBot(BotConfig config){
        this.config = config;
        this.phrases = new BotPhrases();
        this.logger = Logger.getInstance();

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
            String userName = update.getMessage().getChat().getUserName();
            long chatId = update.getMessage().getChatId();
            handleMessage(chatId, messageText, userName);
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            handleCallback(chatId, callbackData);
        }

    }

    private void handleMessage(long chatId, String messageText, String userName){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date();
        logger.logInFile(dateFormat.format(date) + " " + chatId + " " + userName + ": " + messageText);
        switch (messageText){
            case "/start":
                sendMessage(chatId, phrases.greeting);
                break;
            case "/projects":
                for (Projects projects : Projects.values()) {
                    String text = "<b><i>" + projects.getTitle() + "</i></b>\n\n";
                    text += projects.getDescription() + "\n\n";
                    text += projects.getTechnologies();
                    sendDocument(chatId, text, projects.getFile());
                }
                break;
            case "/info":
                sendInfo(chatId);
                break;
            case "/about":
                sendAbout(chatId);
                break;
            default:
                sendMessage(chatId, phrases.unraritable);
        }
    }

    private void handleCallback(long chatId, String callbackData){
        switch (callbackData){

        }

    }

    //Send simple message
    private void sendMessage(long chatId, String text){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try{
            execute(message);
        }catch(TelegramApiException e){

        }
    }

    //Send message with reply keyboard
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

    //Send message with inline keyboard
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

    //Send message with url
    private void sendMessage(long chatId, String text, String url){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var button = new InlineKeyboardButton();
        button.setText("Перейти");
        button.setUrl(url);
        button.setCallbackData("BUTTON");

        rowInLine.add(button);
        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);
        message.setReplyMarkup(keyboardMarkup);

        try{
            execute(message);
        }catch(TelegramApiException e){
            System.out.println(e);
        }

    }

    //Send document
    private void sendDocument(long chatId, String text, File file){
        SendDocument message = new SendDocument();
        message.setChatId(String.valueOf(chatId));
        message.setCaption(text);
        message.setParseMode(ParseMode.HTML);
        message.setDocument(new InputFile(file));
        try{
            execute(message);
        }catch(TelegramApiException e){
            System.out.println(e);
        }
    }

    //Send about message
    private void sendAbout(long chatId){

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("MichelBot");
        button.setUrl(phrases.getCodeRef());
        button.setCallbackData("BUTTON");
        rowInLine.add(button);
        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);

        sendMessage(chatId, phrases.aboutBot, keyboardMarkup);

    }

    //Send info message
    private void sendInfo(long chatId){

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Telegram");
        button.setUrl(phrases.getTgRef());
        button.setCallbackData("BUTTON");
        rowInLine.add(button);
        rowsInLine.add(rowInLine);

        rowInLine = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText("VK");
        button.setUrl(phrases.getVkRef());
        button.setCallbackData("BUTTON");
        rowInLine.add(button);
        rowsInLine.add(rowInLine);

        rowInLine = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText("GitHub");
        button.setUrl(phrases.getGhRef());
        button.setCallbackData("BUTTON");
        rowInLine.add(button);
        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);

        sendMessage(chatId, phrases.aboutMihail, keyboardMarkup);

    }

}
