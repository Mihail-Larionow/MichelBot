package com.michel.MichelBot.config;

import com.michel.MichelBot.utils.WebParser;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@Data
@PropertySource("string.properties")
public class BotPhrases {

    WebParser parser = new WebParser();

    @Value("${message.tg}")
    String tgRef;

    @Value("${message.vk}")
    String vkRef;

    @Value("${message.github}")
    String ghRef;

    @Value("${message.code}")
    String codeRef;

    @Value("${message.dictophone}")
    String dictophone;

    @Value("${message.friendsMap}")
    String friendsMap;

    @Value("${message.rubiksCube}")
    String rubiksCube;

    @Value("${message.weatherIt}")
    String weatherIt;

    public final String greeting = """
            Привет! Я бот-помощник Михаила!
            Чем я могу Вам помочь?
            """;

    public final String aboutMihail = """
            Привет! Меня зовут Ларионов Михаил!
            """;


    public final String aboutBot = """ 
            Я - бот-визитка.
            Разработан на Java с использованием фреймворка Spring. Мой исходный код на GitHub
            """;

    public final String languageRequest = """ 
            На каком языке Вам комфортнее всего будет общаться?
            """;

    public final String aboutDictophone = parser.gitParse("https://github.com/Mihail-Larionow/Dictophone");

    public final String aboutFriendsMap = parser.gitParse("https://github.com/Mihail-Larionow/Dictophone");
    
    public final String aboutRubiksCube = parser.gitParse("https://github.com/Mihail-Larionow/Dictophone");
    
    public final String aboutWeatherIt = parser.gitParse("https://github.com/Mihail-Larionow/Dictophone");

    public final String unraritable = "Прости, пока что я слишком глуп и не понимаю чего ты от меня хочешь...";

}
