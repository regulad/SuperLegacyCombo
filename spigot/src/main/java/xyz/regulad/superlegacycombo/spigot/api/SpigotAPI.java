package xyz.regulad.superlegacycombo.spigot.api;

import org.jetbrains.annotations.NotNull;
import xyz.regulad.superlegacycombo.common.api.CommonAPI;
import xyz.regulad.superlegacycombo.spigot.SpigotPlugin;

public class SpigotAPI extends CommonAPI {
    private final SpigotPlugin spigotPlugin;

    public SpigotAPI(final @NotNull SpigotPlugin spigotPlugin) {
        this.spigotPlugin = spigotPlugin;
        CommonAPI.setInstance(this);
    }
}
