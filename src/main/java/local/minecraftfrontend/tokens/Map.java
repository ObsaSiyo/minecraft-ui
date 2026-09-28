package local.minecraftfrontend.tokens;

/** Reusable UI mappings; never component-specific tokens. */
public record Map(int surface, int surfaceRaised, int text, int textMuted, int border, int focus) {
    public static Map from(Alias alias) {
        return new Map(alias.surface(), alias.raised(), alias.foreground(), alias.muted(), alias.muted(), alias.emphasis());
    }
}
