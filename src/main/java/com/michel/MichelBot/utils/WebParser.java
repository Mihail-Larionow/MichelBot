package com.michel.MichelBot.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class WebParser {

    public String gitParse(String link){
        String text = "";

        try {
            Document document = Jsoup.connect(link).get();
            text = document.text();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return text;
    }

}
