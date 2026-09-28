package local.minecraftfrontend.tokens;

/** Semantic color roles derived from the brand palette. */
public record Alias(int surface, int raised, int foreground, int muted, int emphasis) {
    public static Alias from(Brand brand) {
        return new Alias(brand.ink(), brand.slate(), brand.white(), brand.silver(), brand.accent());
    }
}
