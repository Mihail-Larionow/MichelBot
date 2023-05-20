package com.michel.MichelBot.service;

import com.michel.MichelBot.config.BotConfig;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@PropertySource("application.properties")
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    public TelegramBot(BotConfig config){
        this.config = config;
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
                    
                    break;
                case "/tg":
                    sendTGReference(chatId);
                    break;
                case "/vk":
                    sendVKReference(chatId);
                    break;
                case "/github":
                    sendGithubReference(chatId);
                    break;
                case "/help":
                    help(chatId);
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
        String answer = "GitHub: " + config.getGithubRef() + ".\n";
        sendMessage(chatId, answer);
    }

    private void help(long chatId){
        String answer = "Список доступных команд:\n";
        answer += "/tg - ссылка на аккаунт в telegram\n";
        answer += "/vk - ссылка на аккаунт в vkontakte\n";
        answer += "/github - ссылка на аккаунт на github\n";
        sendMessage(chatId, answer);
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
}
