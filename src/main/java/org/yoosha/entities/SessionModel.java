package org.yoosha.entities;

import org.yoosha.controllers.SqliteController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SessionModel {
    Connection connection = SqliteController.Connector();

    public boolean isConnected() {
        try {
            return !connection.isClosed();
        } catch (Exception ignored) { }
        return false;
    }

    public void createTable() {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String query = "CREATE TABLE IF NOT EXISTS sessions (id INTEGER PRIMARY KEY AUTOINCREMENT, startIn LONG, stopIn LONG, session_type STRING)";

        try {
            connection.prepareStatement(query).executeUpdate();
        } catch (Exception ex) {System.out.println(ex.getMessage());}
    }

    public void writeSession(Session session) {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String query = "INSERT INTO sessions (startIn, stopIn, session_type) VALUES (?, ?, ?)";

        if (session.getStartIn() == null) {
            return;
        }

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, session.getStartIn().toString());
            preparedStatement.setString(2, session.getStopIn().toString());
            preparedStatement.setString(3, session.getSessionType().toString());

            preparedStatement.executeUpdate();
        } catch (Exception ex) {System.out.println(ex.getMessage());}
    }

    public ArrayList<Session> getSessions() {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String query = "SELECT * FROM sessions";

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            final int columnCount = resultSet.getMetaData().getColumnCount();

            ArrayList<Session> sessions = new ArrayList<>();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    sessions.add((Session) resultSet.getObject(i));
                }
            }
            return sessions;
        } catch (Exception ignored) { }

        return null;
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (Exception ignored) { }
    }
}
