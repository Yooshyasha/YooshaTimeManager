package org.yoosha.notification;

import org.springframework.scheduling.TaskScheduler;

public interface NotificationService {
    TaskScheduler taskScheduler = null;

    void startScheduleOnTime(int plusSecond);

    void stopAllSchedulers();

    void notificationInDesktop(String text);

    void notificationInTelegram(String text);

    void notificationInAll(String text);
}
