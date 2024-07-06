package org.yoosha.core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.yoosha.config.Config;
import org.yoosha.entities.SessionModel;


@EnableScheduling
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("yoosha tips!");
        stage.setResizable(false);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 300);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // Смена кодировки print`a
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));

        // Загрузка конфига
        new Config().load();

        // Создание таблиц БД
        SessionModel sessionModel = new SessionModel();
        sessionModel.createTable();
        sessionModel.disconnect();

        // Запуск приложения
        launch();
    }
}