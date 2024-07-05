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

    @FXML
    void initialize() {
        try {
            workController = new WorkController(
                    Integer.parseInt(config.getValue("maxWorkTime").toString()),
                    Integer.parseInt(config.getValue("maxRestTime").toString())
            );
        } catch (Exception ex) {System.out.println(ex.getMessage());}

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
            if (workController.isWork) {
                mainStatistics.setText(workController.workTimeFromCalculateAsString());
            } else {
                try {
                    mainStatistics.setText(workController.restTimeFromCalculateAsString());
                    toNewSessionTime.setText(workController.timeToEndSessionAsString());
                } catch (NullPointerException ignored) { }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); // Бесконечное повторение

        timeline.play();

        work.setOnAction(actionEvent -> {
            workController.setWork();

            if (workController.isWork) {
                youWorkOrRest.setText("Вы сегодня работали:");
                work.setText("Перерыв");

                toNewSession.setText("До перерыва:");
            } else {
                youWorkOrRest.setText("Вы сегодня отдыхали:");
                work.setText("Работать");

                toNewSession.setText("До работы:");
            }

            toNewSessionTime.setText(workController.timeToEndSessionAsString());
        });
    }
}
