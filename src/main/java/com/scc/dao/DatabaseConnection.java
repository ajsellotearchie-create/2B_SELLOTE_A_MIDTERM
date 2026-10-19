package com.scc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:scenario.db";

    // Explicitly load the SQLite driver when the class is loaded
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found! Make sure you added the dependency to your project.");
            e.printStackTrace();
        }
    }

    private DatabaseConnection() {
        // Private constructor to prevent instantiation
    }

    /** Opens a connection with foreign keys enforced. */
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        } catch (SQLException e) {
            conn.close(); // Clean up connection if PRAGMA fails
            throw e;
        }
        return conn;
    }

    /** Creates the allocation table if it does not exist yet. */
    public static void initialize() {
        String sql = """
                CREATE TABLE IF NOT EXISTS allocation (
                    id           INTEGER PRIMARY KEY AUTOINCREMENT,
                    student_name TEXT    NOT NULL,
                    building     TEXT    NOT NULL,
                    room_no      TEXT    NOT NULL,
                    monthly_rent REAL    NOT NULL
                )
                """;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Database initialized successfully at: scenario.db");
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }
}