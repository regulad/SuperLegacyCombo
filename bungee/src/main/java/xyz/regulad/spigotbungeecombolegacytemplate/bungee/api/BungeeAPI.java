package xyz.regulad.spigotbungeecombolegacytemplate.bungee.api;

import org.jetbrains.annotations.NotNull;
import xyz.regulad.spigotbungeecombolegacytemplate.bungee.BungeePlugin;
import xyz.regulad.spigotbungeecombolegacytemplate.common.api.CommonAPI;

public class BungeeAPI extends CommonAPI {
    private final BungeePlugin bungeePlugin;

    public BungeeAPI(final @NotNull BungeePlugin bungeePlugin) {
        this.bungeePlugin = bungeePlugin;
        CommonAPI.setInstance(this);
    }
}
