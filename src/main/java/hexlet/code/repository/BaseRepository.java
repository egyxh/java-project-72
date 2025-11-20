package hexlet.code.repository;

import javax.sql.DataSource;
import java.sql.SQLException;

public abstract class BaseRepository {
    protected DataSource dataSource;

    public BaseRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected void createTableIfNotExists() throws SQLException {
        var sql = """
            CREATE TABLE IF NOT EXISTS urls (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL UNIQUE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
}
