package com.michel.MichelBot.info;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public enum Projects {

    DICTOPHONE(
            "Dictophone",
            "Приложение для создания аудиозаметок."
    ),

    FRIENDSMAP(
            "Friends Map",
            "Приложение, позволяющее узнать местоположение друга VK, использующего это приложение."
    ),

    RUBICKSCUBE(
            "Rubick's Cube",
            "Игра-ностальгия. Любимая игрушка детства."
    ),

    WEATHERIT(
            "WeatherIt",
            "Самое простое погодное приложение."
    );

    private final String title;
    private final String description;

     Projects(String title, String description){
         this.title = title;
         this.description = description;
         System.out.println(title);
    }

    public String getTitle(){
         return title;
    }

    public String getDescription(){
         return description;
    }

    public File getFile(){
        return new File("");
    }

}
