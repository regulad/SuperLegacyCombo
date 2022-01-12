package xyz.regulad.superlegacycombo.common.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.regulad.superlegacycombo.common.api.CommonAPI;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 * @param <P> The type of the player on the platform.
 */
@Data
public class MySQL<P> {
    private final @NotNull String host;
    private final int port;
    private final @NotNull String database;
    private final @NotNull String password;
    private final @NotNull String username;
    private final @NotNull CommonAPI<P> api;

    private final HikariConfig hikariConfig = new HikariConfig();
    @Setter(value = AccessLevel.NONE) // Simply don't need it.
    private @Nullable HikariDataSource hikariDataSource = null;

    /**
     * This must be called in order to use this database.
     */
    public synchronized void connect() {
        this.hikariConfig.setPoolName("Core MySQL");
        this.hikariConfig.setJdbcUrl(String.format("jdbc:mysql://%s%s/%s", this.host, port != 3306 ? ":" + port : "", this.database));
        api.getLogger().info("Connecting to " + this.hikariConfig.getJdbcUrl());
        // Maybe don't hardcode these?
        this.hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        this.hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        this.hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        this.hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
        this.hikariConfig.addDataSourceProperty("encoding", "UTF-8");
        this.hikariConfig.addDataSourceProperty("useUnicode", "true");
        this.hikariConfig.addDataSourceProperty("useSSL", "false");
        this.hikariConfig.setUsername(username);
        this.hikariConfig.setPassword(password);
        this.hikariDataSource = new HikariDataSource(this.hikariConfig);
    }

    public void setupTable() {
        try (final @NotNull Connection connection = Objects.requireNonNull(this.hikariDataSource).getConnection()) {
            Statement statement = connection.createStatement();
            Objects.requireNonNull(statement).executeUpdate("CREATE TABLE IF NOT EXISTS %s (UUID CHAR(36) UNIQUE, PRIMARY KEY (UUID));");
            // fixme
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wraps {@link HikariDataSource#getConnection()}
     */
    public @NotNull Connection getConnection() throws SQLException {
        return Objects.requireNonNull(this.hikariDataSource).getConnection();
    }

    public synchronized void close() {
        if (this.hikariDataSource != null) {
            this.hikariDataSource.close();
        }
    }

    public boolean running() {
        return this.hikariDataSource != null && this.hikariDataSource.isRunning();
    }
}
