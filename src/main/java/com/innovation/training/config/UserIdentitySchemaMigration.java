package com.innovation.training.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class UserIdentitySchemaMigration implements ApplicationRunner {

    private static final String TABLE_NAME = "user";
    private static final String OLD_USERNAME_INDEX = "uk_user_username";
    private static final String USERNAME_TEACHER_TYPE_INDEX = "uk_user_username_teacher_type";

    private final DataSource dataSource;

    public UserIdentitySchemaMigration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            String schema = currentSchema(connection);
            if (schema == null || !tableExists(connection, schema)) {
                return;
            }

            try (Statement statement = connection.createStatement()) {
                if (indexExists(connection, schema, OLD_USERNAME_INDEX)) {
                    statement.execute("ALTER TABLE `user` DROP INDEX `uk_user_username`");
                }
                if (!indexExists(connection, schema, USERNAME_TEACHER_TYPE_INDEX)) {
                    statement.execute("ALTER TABLE `user` ADD UNIQUE KEY `uk_user_username_teacher_type` (`username`, `teacher_type`)");
                }
            }
        }
    }

    private String currentSchema(Connection connection) throws SQLException {
        String catalog = connection.getCatalog();
        if (catalog != null && !catalog.isBlank()) {
            return catalog;
        }
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT DATABASE()")) {
            return resultSet.next() ? resultSet.getString(1) : null;
        }
    }

    private boolean tableExists(Connection connection, String schema) throws SQLException {
        String sql = """
                SELECT COUNT(*)
                FROM information_schema.tables
                WHERE table_schema = ? AND table_name = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            statement.setString(2, TABLE_NAME);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getLong(1) > 0;
            }
        }
    }

    private boolean indexExists(Connection connection, String schema, String indexName) throws SQLException {
        String sql = """
                SELECT COUNT(*)
                FROM information_schema.statistics
                WHERE table_schema = ? AND table_name = ? AND index_name = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            statement.setString(2, TABLE_NAME);
            statement.setString(3, indexName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getLong(1) > 0;
            }
        }
    }
}
