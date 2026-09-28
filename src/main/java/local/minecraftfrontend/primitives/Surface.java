package local.minecraftfrontend.primitives;

import local.minecraftfrontend.tokens.Tokens;

/** Background functions attach to any owo parent component. */
public final class Surface {
    private Surface() {}
    public static io.wispforest.owo.ui.core.Surface flat(int mappedColor) {
        return io.wispforest.owo.ui.core.Surface.flat(mappedColor);
    }
    public static io.wispforest.owo.ui.core.Surface panel() {
        return flat(Tokens.COLORS.surface()).and(io.wispforest.owo.ui.core.Surface.outline(Tokens.COLORS.border()));
    }
}
