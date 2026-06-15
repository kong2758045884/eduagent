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
public class ResourceMediaSchemaMigration implements ApplicationRunner {

    private static final String TABLE_NAME = "teaching_resource";

    private final DataSource dataSource;

    public ResourceMediaSchemaMigration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            String schema = currentSchema(connection);
            if (schema == null || !tableExists(connection, schema)) {
                return;
            }

            addColumnIfMissing(connection, schema, "cover_url",
                    "ALTER TABLE `teaching_resource` ADD COLUMN `cover_url` VARCHAR(512) DEFAULT NULL COMMENT 'cover url' AFTER `school`");
            addColumnIfMissing(connection, schema, "media_url",
                    "ALTER TABLE `teaching_resource` ADD COLUMN `media_url` VARCHAR(512) DEFAULT NULL COMMENT 'media url' AFTER `cover_url`");
            addColumnIfMissing(connection, schema, "duration",
                    "ALTER TABLE `teaching_resource` ADD COLUMN `duration` VARCHAR(32) DEFAULT NULL COMMENT 'media duration' AFTER `media_url`");
            addColumnIfMissing(connection, schema, "uploader",
                    "ALTER TABLE `teaching_resource` ADD COLUMN `uploader` VARCHAR(128) DEFAULT NULL COMMENT 'uploader display name' AFTER `duration`");
            addColumnIfMissing(connection, schema, "tags",
                    "ALTER TABLE `teaching_resource` ADD COLUMN `tags` VARCHAR(512) DEFAULT NULL COMMENT 'resource tags' AFTER `uploader`");
            addColumnIfMissing(connection, schema, "view_count",
                    "ALTER TABLE `teaching_resource` ADD COLUMN `view_count` INT NOT NULL DEFAULT 0 COMMENT 'view count' AFTER `likes`");
            addColumnIfMissing(connection, schema, "favorite_count",
                    "ALTER TABLE `teaching_resource` ADD COLUMN `favorite_count` INT NOT NULL DEFAULT 0 COMMENT 'favorite count' AFTER `view_count`");
        }
    }

    private void addColumnIfMissing(Connection connection, String schema, String columnName, String ddl) throws SQLException {
        if (columnExists(connection, schema, columnName)) {
            return;
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute(ddl);
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

    private boolean columnExists(Connection connection, String schema, String columnName) throws SQLException {
        String sql = """
                SELECT COUNT(*)
                FROM information_schema.columns
                WHERE table_schema = ? AND table_name = ? AND column_name = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            statement.setString(2, TABLE_NAME);
            statement.setString(3, columnName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getLong(1) > 0;
            }
        }
    }
}
