package org.yoosha.controllers;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.yoosha.notification.NotificationService;
import org.yoosha.entities.Session;
import org.yoosha.sesions.SessionsTypes;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkController implements NotificationService {
    public boolean isWork = false;
    public LocalTime workTime = LocalTime.of(0, 0, 0);
    public LocalTime restTime = LocalTime.of(0, 0, 0);

    public LocalDateTime workStartTime;
    public LocalDateTime restStartTime;

    private final int maxWorkTime;
    private final int maxRestTime;

    public Session currentSession = new Session(SessionsTypes.getRestSession());
    public ArrayList<Session> sessionsList = new ArrayList<Session>();

    @Autowired
    private TaskScheduler taskScheduler;

    private final AtomicInteger taskCounter = new AtomicInteger();
    private ScheduledFuture<?>[] scheduledTasks;

    public WorkController(int workTime, int restTime) {
        maxWorkTime = workTime;
        maxRestTime = restTime;
        taskScheduler = new ConcurrentTaskScheduler();

        restStartTime = LocalDateTime.now();
    }

    public void setWork() {
        stopAllSchedulers();
        if (isWork) {
            isWork = false;

            workTime = calculateDurationWorkTime();
            restStartTime = LocalDateTime.now();

            nextSession();

            startScheduleOnTime(maxRestTime * 60);
        } else {
            isWork = true;

            restTime = calculateDurationRestTime();
            workStartTime = LocalDateTime.now();

            nextSession();

            startScheduleOnTime(maxWorkTime * 60);
        }
    }

    public String workTimeAsString() {
        return workTime.getHour() + "h " + workTime.getMinute() + "m";
    }

    public String workTimeFromCalculateAsString() {
        LocalTime calculateWorkTime = calculateDurationWorkTime();
        return calculateWorkTime.getHour() + "h " + calculateWorkTime.getMinute() + "m";
    }

    public String restTimeFromCalculateAsString() {
        LocalTime calculateRestTime = calculateDurationRestTime();
        return calculateRestTime.getHour() + "h " + calculateRestTime.getMinute() + "m";
    }

    public LocalTime calculateDurationWorkTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(workStartTime, currentTime);
        return workTime.plus(duration);
    }

    public LocalTime calculateDurationRestTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(restStartTime, currentTime);
        return restTime.plus(duration);
    }

    private void nextSession() {
        currentSession.stop();
        sessionsList.add(currentSession);

        if (isWork) {
            currentSession = new Session(SessionsTypes.getWorkSession());
        } else {
            currentSession = new Session(SessionsTypes.getRestSession());
        }

        currentSession.start();
    }

    public String timeToEndSessionAsString() {
        return calculateTimeToEndSession() + "m";
    }

    public long calculateTimeToEndSession() {
        long sessionDurationSeconds = Duration.between(currentSession.getStartIn(), LocalDateTime.now()).getSeconds();
        long maxSessionSeconds = currentSession.getSessionType().isWork ? maxWorkTime * 60L : maxRestTime * 60L;
        long secondsToEnd = maxSessionSeconds - sessionDurationSeconds;
        return Math.max(0, secondsToEnd / 60);
    }

    @Override
    public void startScheduleOnTime(int plusSecond) {
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(() -> {
            notificationInAll("Время вышло!");
        }, triggerContext -> {
            return Date.from(Instant.now().plusSeconds(plusSecond)).toInstant();
        });
        int taskIndex = taskCounter.getAndIncrement();
        if (scheduledTasks == null) {
            scheduledTasks = new ScheduledFuture<?>[taskIndex + 1];
        } else if (taskIndex >= scheduledTasks.length) {
            ScheduledFuture<?>[] newTasks = new ScheduledFuture<?>[taskIndex + 1];
            System.arraycopy(scheduledTasks, 0, newTasks, 0, scheduledTasks.length);
            scheduledTasks = newTasks;
        }
        scheduledTasks[taskIndex] = scheduledTask;
    }

    @Override
    public void stopAllSchedulers() {
        if (scheduledTasks != null) {
            for (ScheduledFuture<?> scheduledTask : scheduledTasks) {
                if (scheduledTask != null) {
                    scheduledTask.cancel(true);
                }
            }
        }
    }

    @Override
    public void notificationInDesktop(String text) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Уведомление");
            alert.setHeaderText(null);
            alert.setContentText(text);
            alert.showAndWait();
        });
        stopAllSchedulers();
    }

    @Override
    public void notificationInTelegram(String text) {

    }

    @Override
    public void notificationInAll(String text) {
        try {
            notificationInDesktop(text);
        } catch (Exception ex) {System.out.println(ex.getMessage());}
        try {
            notificationInTelegram(text);
        } catch (Exception ex) {System.out.println(ex.getMessage());}
    }
}
