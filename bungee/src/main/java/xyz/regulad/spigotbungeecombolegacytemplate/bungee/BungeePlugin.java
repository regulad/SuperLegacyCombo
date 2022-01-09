package xyz.regulad.spigotbungeecombolegacytemplate.bungee;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.bstats.bungeecord.Metrics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.regulad.spigotbungeecombolegacytemplate.bungee.api.BungeeAPI;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class BungeePlugin extends Plugin {
    @Getter
    private static @Nullable BungeePlugin instance;
    @Getter
    private @Nullable Metrics metrics;
    private @Nullable Configuration configuration;
    @Getter
    private final @NotNull BungeeAPI bungeeAPI = new BungeeAPI(this);

    @Override
    public void onEnable() {
        // Setup instance access
        instance = this;
        // Setup config
        if (this.getConfig() == null) this.saveDefaultConfig();
        if (this.getConfig() == null) this.getLogger().warning("Couldn't load configuration!");
        // Setup bStats metrics
        this.metrics = new Metrics(this, 13872); // TODO: Replace this in your plugin!
    }

    @Override
    public void onDisable() {
        this.metrics = null;
        this.configuration = null;
    }

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

            this.configuration = configurationProvider.load(new File(this.getDataFolder(), "config.yml"), defaultConfiguration);
        } catch (IOException exception) {
            this.configuration = null;
        }
    }

    /**
     * Provides the plugin's {@link Configuration}, reloading it if it has not yet been loaded.
     *
     * @return The loaded {@link Configuration}, which may be null if an exception occurs.
     */
    public @Nullable Configuration getConfig() {
        if (this.configuration == null) this.reloadConfig();
        return this.configuration;
    }
}
