package com.michel.MichelBot.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static Logger instance;
    private BufferedWriter bwriter;
    private FileWriter fwriter;
    private File file;
    private String fileName = "./logs.log";

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                    System.out.println("First instance of Logger created.");
                }
            }
        }
        return instance;
    }

    public void log(Object obj){
        System.out.println(obj);
    }

    public void logInFile(Object log) {
        bwriter = null;
        fwriter = null;
        boolean existingFile = true;

        synchronized (Logger.class) {
            try {
                file = new File(fileName);
                if (!file.exists()) {
                    file.createNewFile();
                    existingFile = false;
                }

                fwriter = new FileWriter(file.getAbsoluteFile(), true);
                bwriter = new BufferedWriter(fwriter);
                if (existingFile) {
                    bwriter.newLine();
                }
                bwriter.write(log.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bwriter != null)
                        bwriter.close();
                    if (fwriter != null)
                        fwriter.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
