package org.yoosha.sesions;

public class SessionType {
    public int id;
    public boolean isWork;

    public SessionType(int sessionId, boolean sessionIsWork) {
        id = sessionId;
        isWork = sessionIsWork;
    }

    @Override
    public String toString() {
        return "work: " + isWork;
    }
}
