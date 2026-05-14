package main.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class DatabaseInitializer {

    private static final Logger logger = AppLogger.getLogger(DatabaseInitializer.class);

    private DatabaseInitializer() {
    }

    public static void initializeDatabase() {
        logger.info("Starting database initialization...");
        try (Connection connection = DatabaseConnection.getConnection()) {

            String sql = new BufferedReader(
                    new InputStreamReader(
                            Objects.requireNonNull(DatabaseInitializer.class.getClassLoader()
                                    .getResourceAsStream("schema.sql"))
                    )
            ).lines().collect(Collectors.joining("\n"));

            String[] statements = sql.split(";");

            // 1) Execută DDL (CREATE / DROP / ALTER) întotdeauna
            for (String statementSql : statements) {
                String trimmed = statementSql.trim();
                if (trimmed.isBlank()) continue;
                String up = trimmed.toUpperCase();
                if (up.startsWith("CREATE") || up.startsWith("DROP") || up.startsWith("ALTER")) {
                    try (Statement statement = connection.createStatement()) {
                        statement.execute(trimmed);
                    }
                }
            }

            boolean needSeed = false;
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM studiouri")) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    needSeed = (count == 0);
                } else {
                    needSeed = true;
                }
            } catch (SQLException ex) {
                needSeed = true;
            }
            if (needSeed) {
                for (String statementSql : statements) {
                    String trimmed = statementSql.trim();
                    if (trimmed.isBlank()) continue;
                    String up = trimmed.toUpperCase();
                    if (up.startsWith("INSERT") || up.startsWith("REPLACE")) {
                        try (Statement statement = connection.createStatement()) {
                            statement.execute(trimmed);
                        }
                    }
                }
                logger.info("Seed data inserted.");
            } else {
                logger.info("Seed data already present; skipping INSERT statements.");
            }

            logger.info("Database initialized successfully. Tabelele au fost create.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Database initialization failed", e);
        }
    }
}