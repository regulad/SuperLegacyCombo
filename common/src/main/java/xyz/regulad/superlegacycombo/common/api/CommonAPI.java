package xyz.regulad.superlegacycombo.common.api;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

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
}
