package xyz.regulad.superlegacycombo.common.api;

import lombok.Getter;
import lombok.Setter;

public abstract class CommonAPI {
    @Getter
    @Setter
    public static CommonAPI instance;
}
