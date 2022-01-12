package xyz.regulad.superlegacycombo.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.bstats.velocity.Metrics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.regulad.superlegacycombo.common.api.CommonAPI;
import xyz.regulad.superlegacycombo.common.db.MySQL;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Logger;

// fixme
@Plugin(
        id = "superlegacycombo",
        name = "SuperLegacyCombo",
        version = "1.0.0-SNAPSHOT",
        description = "A template for legacy (Minecraft?=1.8.8) Minecraft plugins that use both Spigot, BungeeCord, as well as a common module.",
        authors = {"regulad"}
)
public class VelocityPlugin implements CommonAPI<Player> {
    @Getter
    private static @Nullable VelocityPlugin instance;

    @Inject
    private @NotNull ProxyServer proxy;
    @Inject
    @Getter
    private @NotNull Logger logger;
    @Getter
    private @Nullable MySQL<Player> mySQL;

    @Inject
    private @NotNull Metrics.Factory metricsFactory;
    @Getter
    private @Nullable Metrics metrics;

    @Inject
    @DataDirectory
    private @NotNull Path pluginFolder;
    @Getter
    private @Nullable File pluginFolderFile;
    @Getter
    private @Nullable File configFile;
    @Getter
    private @Nullable YAMLConfigurationLoader configurationLoader;
    private @Nullable ConfigurationNode config;

    public VelocityPlugin() {
        instance = this;
    }

    @Subscribe
    public void initializeMetrics(final @NotNull ProxyInitializeEvent proxyInitializeEvent) {
        this.metrics = this.metricsFactory.make(this, 13899); // TODO: Replace this in your plugin!
    }

    @Subscribe
    public void setInstance(final @NotNull ProxyInitializeEvent proxyInitializeEvent) {
        CommonAPI.setInstance(this);
    }

    /**
     * Sets up file instances so other methods can work.
     */
    public void setupFiles() {
        this.pluginFolderFile = this.pluginFolder.toFile();
        this.configFile = new File(this.pluginFolderFile, "config.yml");
    }

    /**
     * Saves the default configuration stored in the plugin to the disk.
     */
    public void saveDefaultConfig() {
        if (this.pluginFolderFile == null || this.configFile == null) this.setupFiles();
        try {
            if (!this.pluginFolderFile.exists()) {
                this.pluginFolderFile.mkdir();
            }

            if (!this.configFile.exists()) {
                try (final @Nullable InputStream configStream = VelocityPlugin.class.getClassLoader().getResourceAsStream("config.yml")) {
                    if (configStream != null) {
                        try (final @NotNull FileOutputStream outputStream = new FileOutputStream(this.configFile)) {
                            configStream.transferTo(outputStream);
                        }
                    }
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void setupConfigLoader() {
        if (this.configFile == null) this.setupFiles();
        this.configurationLoader = YAMLConfigurationLoader.builder()
                .setFile(this.configFile)
                .build();
    }

    /**
     * Reloads the plugin's parent {@link ConfigurationNode}.
     */
    public void reloadConfig() throws IOException {
        if (this.configurationLoader == null) this.setupConfigLoader();
        if (!Objects.requireNonNull(this.configFile).exists()) this.saveDefaultConfig();
        this.config = this.configurationLoader.load();
    }

    /**
     * Provides the plugin's parent {@link ConfigurationNode}, reloading it if it has not yet been loaded.
     *
     * @return The loaded {@link ConfigurationNode}, which may be {@code null} if an exception occurs.
     */
    public @Nullable ConfigurationNode getConfig() {
        if (this.config == null) {
            try {
                this.reloadConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.config;
    }

    @Subscribe
    public void loadConfig(final @NotNull ProxyInitializeEvent proxyInitializeEvent) {
        try {
            reloadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Config version: " + this.config.getNode("version").getInt());
    }

    @Subscribe
    public void loadMySQL(final @NotNull ProxyInitializeEvent proxyInitializeEvent) {
        final @NotNull ConfigurationNode databaseNode = Objects.requireNonNull(this.getConfig()).getNode("db");
        if (databaseNode.getNode("enabled").getBoolean(false)) {
            this.mySQL = new MySQL<>(
                    Objects.requireNonNull(databaseNode.getNode("host").getString()),
                    databaseNode.getNode("port").getInt(3306),
                    Objects.requireNonNull(databaseNode.getNode("database").getString()),
                    Objects.requireNonNull(databaseNode.getNode("username").getString()),
                    Objects.requireNonNull(databaseNode.getNode("password").getString()),
                    this
            );
        }
        if (this.mySQL != null) {
            this.mySQL.setupTable();
            this.mySQL.connect();
        }
    }

    @Subscribe
    public void discardMySQL(final @NotNull ProxyShutdownEvent proxyShutdownEvent) {
        if (this.mySQL != null) {
            this.mySQL.close();
        }
        this.mySQL = null;
    }
}
