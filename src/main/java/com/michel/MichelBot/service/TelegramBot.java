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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("application.properties")
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;
    final String helpText = "Список доступных команд:\n"
        + "/tg - ссылка на аккаунт в telegram\n"
        + "/vk - ссылка на аккаунт в vkontakte\n"
        + "/github - ссылка на аккаунт на github\n";

    final String aboutText = "Данный бот - бот-визитка.";

    public TelegramBot(BotConfig config){
        this.config = config;
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/tg", "Get Telegram reference"));
        commands.add(new BotCommand("/vk", "Get VK reference"));
        commands.add(new BotCommand("/gh", "Get GitHub reference"));
        commands.add(new BotCommand("/about", "Information about this bot"));
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

        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText){
                case "/start":
                    greeting(chatId, update.getMessage().getChat().getFirstName());
                    sendMessage(chatId, helpText);
                    break;
                case "/tg":
                    sendTGReference(chatId);
                    break;
                case "/vk":
                    sendVKReference(chatId);
                    break;
                case "/gh":
                    sendGithubReference(chatId);
                    break;
                case "/info":
                    info(chatId);
                    break;
                case "/help":
                    sendMessage(chatId, helpText);
                    break;
                case "/about":
                    sendMessage(chatId, aboutText);
                    break;
                default:
                    sendMessage(chatId, "Прости, пока что я слишком глуп и не понимаю чего ты от меня хочешь...");
            }
        }

    }

    private void greeting(long chatId, String userName){
        String answer = "Привет! " + userName +".\n";
        answer += "Я бот-помощник Михаила Ларионова.\n";
        answer += "Чем я могу Вам помочь?";
        sendMessage(chatId, answer);
    }

    private void sendTGReference(long chatId){
        String answer = "Telegram: " + config.getTgRef() + ".\n";
        sendMessage(chatId, answer);
    }

    private void sendVKReference(long chatId){
        String answer = "VK: " + config.getVkRef() + ".\n";
        sendMessage(chatId, answer);
    }

    private void sendGithubReference(long chatId){
        String answer = "GitHub: " + config.getGhRef() + ".\n";
        sendMessage(chatId, answer);
    }

    private void info(long chatId){
        String answer = "Какую информацию вы хотите получить?\n";
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("TG");
        row.add("VK");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("GitHub");
        keyboardRows.add(row);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage(chatId, answer, replyKeyboardMarkup);
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
}
