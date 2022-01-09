package xyz.regulad.spigotbungeecombolegacytemplate.spigot;

import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.regulad.spigotbungeecombolegacytemplate.spigot.api.SpigotAPI;

public class SpigotPlugin extends JavaPlugin {
    @Getter
    private static @Nullable SpigotPlugin instance;
    @Getter
    private @Nullable Metrics metrics;
    @Getter
    private final @NotNull SpigotAPI spigotAPI = new SpigotAPI(this);

    @Override
    public void onEnable() {
        // Setup instance access
        instance = this;
        // Setup config
        this.saveDefaultConfig();
        // Setup bStats metrics
        this.metrics = new Metrics(this, 13871); // TODO: Replace this in your plugin!
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
