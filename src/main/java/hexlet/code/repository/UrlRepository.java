package hexlet.code.repository;

import hexlet.code.model.Url;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {

    public UrlRepository(DataSource dataSource) {
        super(dataSource);
    }

    public void save(Url url) throws SQLException {
        createTableIfNotExists();

        var sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, url.getName());
            preparedStatement.setTimestamp(2, Timestamp.from(url.getCreatedAt()));

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            }
        }
    }

    public List<Url> findAll() throws SQLException {
        createTableIfNotExists();

        var sql = "SELECT * FROM urls ORDER BY created_at DESC";
        var result = new ArrayList<Url>();

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement();
             var resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                var url = new Url();
                url.setId(resultSet.getLong("id"));
                url.setName(resultSet.getString("name"));
                url.setCreatedAt(resultSet.getTimestamp("created_at").toInstant());
                result.add(url);
            }
        }

        return result;
    }

    public Optional<Url> findById(Long id) throws SQLException {
        createTableIfNotExists();

        var sql = "SELECT * FROM urls WHERE id = ?";

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var url = new Url();
                    url.setId(resultSet.getLong("id"));
                    url.setName(resultSet.getString("name"));
                    url.setCreatedAt(resultSet.getTimestamp("created_at").toInstant());
                    return Optional.of(url);
                }
            }
        }

        return Optional.empty();
    }

    public Optional<Url> findByName(String name) throws SQLException {
        createTableIfNotExists();

        var sql = "SELECT * FROM urls WHERE name = ?";

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var url = new Url();
                    url.setId(resultSet.getLong("id"));
                    url.setName(resultSet.getString("name"));
                    url.setCreatedAt(resultSet.getTimestamp("created_at").toInstant());
                    return Optional.of(url);
                }
            }
        }

        return Optional.empty();
    }
}
