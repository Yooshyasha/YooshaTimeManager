package org.yoosha.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class SqliteController {
    public static Connection Connector() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public boolean isConnected() {
        try {
            if (!Objects.requireNonNull(Connector()).isClosed()) {
                return true;
            }
        } catch (SQLException e) {

        }
        return false;
    }

}
