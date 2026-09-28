package local.minecraftfrontend.tokens;

/** Central token assembly: Brand → Alias → Map. No component tokens or theme engine. */
public final class Tokens {
    private Tokens() {}
    public static final Brand BRAND = Brand.CIEL;
    public static final Alias ALIAS = Alias.from(BRAND);
    public static final Map COLORS = Map.from(ALIAS);
    public static final int GAP = 6;
    public static final int PADDING = 10;
    public static final int CONTROL_HEIGHT = 20;
}
