package database;

import configs.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseClient {

    private DatabaseClient() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    Config.getDbUrl(),
                    Config.getDbUsername(),
                    Config.getDbPassword()
            );
        } catch (SQLException exception) {
            throw new RuntimeException("Cannot connect to database", exception);
        }
    }
}