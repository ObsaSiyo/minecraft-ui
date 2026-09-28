package local.minecraftfrontend.tokens;

/** Existing Ciel palette; raw colors have one owner. */
public record Brand(int ink, int slate, int white, int silver, int accent) {
    public static final Brand CIEL = new Brand(0xFF11141C, 0xFF232936, 0xFFF2F4F8, 0xFFADB5C5, 0xFFFF6778);
}
