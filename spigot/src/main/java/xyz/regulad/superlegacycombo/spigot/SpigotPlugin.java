package xyz.regulad.superlegacycombo.spigot;

import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import xyz.regulad.superlegacycombo.common.api.CommonAPI;

public class SpigotPlugin extends JavaPlugin implements CommonAPI<Player> {
    @Getter
    private static @Nullable SpigotPlugin instance;
    @Getter
    private @Nullable Metrics metrics;

    @Getter
    private @Nullable BukkitAudiences bukkitAudiences;

    @Override
    public void onEnable() {
        // Setup instance access
        instance = this;
        CommonAPI.setInstance(this);
        // Setup config
        this.saveDefaultConfig();
        // Setup adventure
        this.bukkitAudiences = BukkitAudiences.create(this);
        // Setup bStats metrics
        this.metrics = new Metrics(this, 13900); // TODO: Replace this in your plugin!
    }

    @Override
    public void onDisable() {
        // Discard instance access
        instance = null;
        CommonAPI.setInstance(null);
        // Discard adventure
        if (this.bukkitAudiences != null) {
            this.bukkitAudiences.close();
            this.bukkitAudiences = null;
        }
        // Discard bStats metrics
        this.metrics = null;
    }
}
