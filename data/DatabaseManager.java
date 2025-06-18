package data;

import java.sql.*;

/**
 * Manages basic database operations using SQLite.
 * It stores player profiles, case progress, and suspect data.
 */
public class DatabaseManager {
    private Connection connection;

    public DatabaseManager(String dbURL) {
        try {
            connection = DriverManager.getConnection(dbURL);
            System.out.println("Connected to the database successfully.");
            initializeDatabase();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates tables if they do not exist.
     */
    private void initializeDatabase() {
        String createPlayerTable = "CREATE TABLE IF NOT EXISTS player (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "progress TEXT);";
        String createSuspectTable = "CREATE TABLE IF NOT EXISTS suspect (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "questioned BOOLEAN NOT NULL);";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createPlayerTable);
            stmt.execute(createSuspectTable);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a player profile.
     */
    public void insertPlayer(String name, String progress) {
        String sql = "INSERT INTO player (name, progress) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, progress);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a suspect’s questioned status.
     */
    public void updateSuspect(String name, boolean questioned) {
        String sql = "UPDATE suspect SET questioned = ? WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, questioned);
            pstmt.setString(2, name);
            int rows = pstmt.executeUpdate();
            if(rows == 0){
                // If not found, insert new record.
                insertSuspect(name, questioned);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a new suspect record.
     */
    public void insertSuspect(String name, boolean questioned) {
        String sql = "INSERT INTO suspect (name, questioned) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setBoolean(2, questioned);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if(connection != null) connection.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}