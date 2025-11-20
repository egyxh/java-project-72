package hexlet.code.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);

    private static HikariDataSource dataSource;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            var config = new HikariConfig();

            String jdbcUrl = getJdbcUrl();
            config.setJdbcUrl(jdbcUrl);


            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(300000);
            config.setConnectionTimeout(30000);

            dataSource = new HikariDataSource(config);
            logger.info("Database connection pool initialized with URL: {}", jdbcUrl);
        }

        return dataSource;
    }

    private static String getJdbcUrl() {
        String jdbcDatabaseUrl = System.getenv("JDBC_DATABASE_URL");

        if (jdbcDatabaseUrl != null && !jdbcDatabaseUrl.isEmpty()) {
            logger.info("Using PostgreSQL database from JDBC_DATABASE_URL");
            if (jdbcDatabaseUrl.contains("postgresql")) {
                if (!jdbcDatabaseUrl.contains("?")) {
                    jdbcDatabaseUrl += "?";
                } else {
                    jdbcDatabaseUrl += "&";
                }
                jdbcDatabaseUrl += "ssl=true&sslmode=require";
            }
            return jdbcDatabaseUrl;
        } else {
            logger.info("Using H2 in-memory database for development");
            return "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;MODE=PostgreSQL";
        }
    }

    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Database connection pool closed");
        }
    }

    public static boolean testConnection() {
        try (Connection connection = getDataSource().getConnection()) {
            return connection.isValid(1000);
        } catch (SQLException e) {
            logger.error("Database connection test failed: {}", e.getMessage());
            return false;
        }
    }
}
