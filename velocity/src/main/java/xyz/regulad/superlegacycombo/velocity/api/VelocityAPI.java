package xyz.regulad.superlegacycombo.velocity.api;

import org.jetbrains.annotations.NotNull;
import xyz.regulad.superlegacycombo.common.api.CommonAPI;
import xyz.regulad.superlegacycombo.velocity.VelocityPlugin;

public class VelocityAPI extends CommonAPI {
    private final VelocityPlugin velocityPlugin;

    public VelocityAPI(final @NotNull VelocityPlugin velocityPlugin) {
        this.velocityPlugin = velocityPlugin;
        CommonAPI.setInstance(this);
    }
}
