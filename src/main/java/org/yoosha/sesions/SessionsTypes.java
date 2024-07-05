package org.yoosha.sesions;

public class SessionsTypes {
    public static SessionType getWorkSession() {return new SessionType(0, true);}

    public static SessionType getRestSession() {return new SessionType(1, false);}
}
