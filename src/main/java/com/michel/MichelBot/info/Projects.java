package com.michel.MichelBot.info;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public enum Projects {

    DICTOPHONE(
            "Dictophone",
            "Приложение для создания аудиозаметок.",
            "Язык: Kotlin"
    ),

    FRIENDSMAP(
            "Friends Map",
            "Приложение, позволяющее узнать местоположение друга VK, использующего это приложение.",
            "Язык: Java"
    ),

    RUBICKSCUBE(
            "Rubick's Cube",
            "Игра-ностальгия. Любимая игрушка детства.",
            "Язык: C#"
    ),

    WEATHERIT(
            "WeatherIt",
            "Самое простое погодное приложение.",
            "Язык: Kotlin"
    );

    private final String title;
    private final String description;
    private final String technologies;

     Projects(String title, String description, String technologies){
         this.title = title;
         this.description = description;
         this.technologies = technologies;
         System.out.println(title);
    }

    public String getTitle(){
         return title;
    }

    public String getDescription(){
         return description;
    }

    public String getTechnologies() {
        return technologies;
    }

    public File getFile(){
        return new File("./src/main/resources/files/" + "short_logo" + ".png");
    }

}
