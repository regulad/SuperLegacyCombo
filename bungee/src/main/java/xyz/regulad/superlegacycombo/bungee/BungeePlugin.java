package xyz.regulad.superlegacycombo.bungee;

import lombok.Getter;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.bstats.bungeecord.Metrics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.regulad.superlegacycombo.common.api.CommonAPI;
import xyz.regulad.superlegacycombo.common.db.MySQL;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class BungeePlugin extends Plugin implements CommonAPI<ProxiedPlayer> {
    @Getter
    private static @Nullable BungeePlugin instance;
    @Getter
    private @Nullable MySQL<ProxiedPlayer> mySQL;

    @Getter
    private @Nullable Metrics metrics;
    private @Nullable Configuration config;

    @Getter
    private @Nullable BungeeAudiences bungeeAudiences;

    @Override
    public void onEnable() {
        // Setup instance access
        instance = this;
        CommonAPI.setInstance(this);
        // Setup config
        if (this.getConfig() == null) this.saveDefaultConfig();
        if (this.getConfig() == null) this.getLogger().warning("Couldn't load configuration!");
        // Setup adventure
        this.bungeeAudiences = BungeeAudiences.create(this);
        // Setup bStats metrics
        this.metrics = new Metrics(this, 13901); // TODO: Replace this in your plugin!
        // Setup MySQL
        if (this.getConfig().getBoolean("db.enabled", false)) {
            this.mySQL = new MySQL<>(
                    this.getConfig().getString("db.host"),
                    this.getConfig().getInt("db.port"),
                    this.getConfig().getString("db.database"),
                    this.getConfig().getString("db.username"),
                    this.getConfig().getString("db.password"),
                    this
            );
        }
        if (this.mySQL != null) {
            this.mySQL.setupTable();
            this.mySQL.connect();
        }
    }

    @Override
    public void onDisable() {
        // Discard instance access
        instance = null;
        CommonAPI.setInstance(null);
        // Discard instance access
        this.config = null;
        // Discard adventure
        if (this.bungeeAudiences != null) {
            this.bungeeAudiences.close();
        }
        // Discard bStats metrics
        this.metrics = null;
        // Discard MySQL
        if (this.mySQL != null) {
            this.mySQL.close();
        }
        this.mySQL = null;
    }

    /**
     * Saves the default configuration stored in the plugin to the disk.
     */
    public void saveDefaultConfig() {
        if (!this.getDataFolder().exists()) this.getDataFolder().mkdir();

        final @NotNull File file = new File(this.getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (final @NotNull InputStream in = this.getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Reloads the plugin's {@link Configuration}.
     */
    public void reloadConfig() {
        try {
            final @NotNull ConfigurationProvider configurationProvider = ConfigurationProvider.getProvider(YamlConfiguration.class);
            // Technically Nullable, but the server would have already crashed if that was the case.

            final @NotNull Configuration defaultConfiguration;
            try (final @NotNull InputStream defaultConfig = this.getResourceAsStream("config.yml")) {
                defaultConfiguration = configurationProvider.load(defaultConfig); // Will this break? It is barely documented.
            }

            this.config = configurationProvider.load(new File(this.getDataFolder(), "config.yml"), defaultConfiguration);
        } catch (IOException exception) {
            this.config = null;
        }
    }

    /**
     * Provides the plugin's {@link Configuration}, reloading it if it has not yet been loaded.
     *
     * @return The loaded {@link Configuration}, which may be {@code null} if an exception occurs.
     */
    public @Nullable Configuration getConfig() {
        if (this.config == null) this.reloadConfig();
        return this.config;
    }
}
