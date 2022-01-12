package xyz.regulad.superlegacycombo.common.api;

import lombok.Getter;
import lombok.Setter;
import org.bstats.MetricsBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.regulad.superlegacycombo.common.db.MySQL;

import java.util.logging.Logger;

/**
 * @param <P> The type of the player on the platform.
 */
public interface CommonAPI<P> {
    static CommonAPI<?> getInstance() {
        return InstanceHolder.getInstance();
    }

    static void setInstance(final @Nullable CommonAPI<?> instance) {
        InstanceHolder.setInstance(instance);
    }

    class InstanceHolder {
        @Getter
        @Setter
        private static @Nullable CommonAPI<?> instance;
    }

    @NotNull Logger getLogger();

    @Nullable MySQL<P> getMySQL();
}
