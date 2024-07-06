package org.yoosha.core;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.yoosha.config.Config;
import org.yoosha.controllers.WorkController;
import org.yoosha.localization.Localization;
import org.yoosha.localization.UIText;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text mainStatistics;

    @FXML
    private Button settings;

    @FXML
    private Button statistics;

    @FXML
    private Button work;

    @FXML
    private Text toNewSession;

    @FXML
    private Text toNewSessionTime;

    @FXML
    private Text youWorkOrRest;

    private final Config config = new Config();
    private WorkController workController;
    private final Timeline timeline = new Timeline();

    private UIText uiText;

    @FXML
    void initialize() {
        // Инициализация локализации
        Localization localization = initializeLocalization();
        uiText = new UIText(localization);

        // Настройка текста элементов интерфейса
        setUIText();

        // Инициализация WorkController
        workController = initializeWorkController();

        // Настройка таймера
        setupTimer();

        // Обработчик события нажатия на кнопку "Работа/Отдых"
        work.setOnAction(actionEvent -> handleWorkButtonAction());
    }

    private Localization initializeLocalization() {
        if (config.getValue("language") != null) {
            return new Localization((String) config.getValue("language"));
        } else {
            return new Localization();
        }
    }

    private void setUIText() {
        youWorkOrRest.setText(uiText.getText("youWork"));
        work.setText(uiText.getText("rest"));
        toNewSession.setText(uiText.getText("toRest"));
        settings.setText(uiText.getText("settings"));
        statistics.setText(uiText.getText("statistics"));
    }

    private WorkController initializeWorkController() {
        try {
            return new WorkController(
                    Integer.parseInt(config.getValue("maxWorkTime").toString()),
                    Integer.parseInt(config.getValue("maxRestTime").toString())
            );
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null; // Возвращаем null, если возникла ошибка
        }
    }

    private void setupTimer() {
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(200d), event -> {
            updateTimer();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); // Бесконечное повторение
        timeline.play();
    }

    private void updateTimer() {
        if (workController.isWork) {
            mainStatistics.setText(workController.workTimeFromCalculateAsString());
        } else {
            try {
                mainStatistics.setText(workController.restTimeFromCalculateAsString());
            } catch (NullPointerException ignored) { }
        }

        try {
            toNewSessionTime.setText(workController.timeToEndSessionAsString());
        } catch (NullPointerException ignored) { }
    }

    private void handleWorkButtonAction() {
        workController.setWork();

        if (workController.isWork) {
            youWorkOrRest.setText(uiText.getText("youWork"));
            work.setText(uiText.getText("rest"));
            toNewSession.setText(uiText.getText("toRest"));
        } else {
            youWorkOrRest.setText(uiText.getText("youRest"));
            work.setText(uiText.getText("work"));
            toNewSession.setText(uiText.getText("toWork"));
        }

        settings.setText(uiText.getText("settings"));
        statistics.setText(uiText.getText("statistics"));
    }
}