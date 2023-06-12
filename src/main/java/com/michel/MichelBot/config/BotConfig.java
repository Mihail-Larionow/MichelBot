package com.michel.MichelBot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("string.properties")
@PropertySource("application.properties")
public class BotConfig {

    @Value("${bot.name}")
    String botName;

    @Value("${bot.token}")
    String token;

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
}
