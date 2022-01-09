package xyz.regulad.spigotbungeecombolegacytemplate.spigot.api;

import org.jetbrains.annotations.NotNull;
import xyz.regulad.spigotbungeecombolegacytemplate.common.api.CommonAPI;
import xyz.regulad.spigotbungeecombolegacytemplate.spigot.SpigotPlugin;

public class SpigotAPI extends CommonAPI {
    private final SpigotPlugin spigotPlugin;

    public SpigotAPI(final @NotNull SpigotPlugin spigotPlugin) {
        this.spigotPlugin = spigotPlugin;
        CommonAPI.setInstance(this);
    }
}
