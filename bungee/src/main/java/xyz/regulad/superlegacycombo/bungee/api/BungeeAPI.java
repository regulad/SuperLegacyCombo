package xyz.regulad.superlegacycombo.bungee.api;

import org.jetbrains.annotations.NotNull;
import xyz.regulad.superlegacycombo.bungee.BungeePlugin;
import xyz.regulad.superlegacycombo.common.api.CommonAPI;

public class BungeeAPI extends CommonAPI {
    private final BungeePlugin bungeePlugin;

    public BungeeAPI(final @NotNull BungeePlugin bungeePlugin) {
        this.bungeePlugin = bungeePlugin;
        CommonAPI.setInstance(this);
    }
}
