package org.yoosha.entities;

import org.yoosha.sesions.SessionType;

import java.time.LocalDateTime;

public class Session {

    private int id;

    private LocalDateTime startIn;

    private LocalDateTime stopIn;

    private SessionType sessionType;

    public Session(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public Session(int id, LocalDateTime startIn, LocalDateTime stopIn, SessionType sessionType) {
        this.id = id;
        this.startIn = startIn;
        this.stopIn = stopIn;
        this.sessionType = sessionType;
    }

    public void start() {
        startIn = LocalDateTime.now();
    }

    public void stop() {
        stopIn = LocalDateTime.now();

        SessionModel sessionModel = new SessionModel();

        if (sessionModel.isConnected()) {
            sessionModel.writeSession(this);
        }
    }

    public void setId(int id) {this.id = id;}

    public void setStartIn(LocalDateTime startIn) {this.startIn = startIn;}

    public void setStopIn(LocalDateTime stopIn) {this.stopIn = stopIn;}

    public void setSessionType(SessionType sessionType) {this.sessionType = sessionType;}

    public int getId() {return id;}

    public LocalDateTime getStartIn() {return startIn;}

    public LocalDateTime getStopIn() {return stopIn;}

    public SessionType getSessionType() {return sessionType;}
}
