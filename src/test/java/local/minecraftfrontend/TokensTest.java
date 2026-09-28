package local.minecraftfrontend;

import local.minecraftfrontend.tokens.Tokens;
import local.minecraftfrontend.tokens.Alias;
import local.minecraftfrontend.tokens.Map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TokensTest {
    @Test void aliasesPreserveTheExistingPalette() {
        var brand = Tokens.BRAND;
        var alias = Alias.from(brand);
        assertEquals(brand.ink(), alias.surface());
        assertEquals(brand.slate(), alias.raised());
        assertEquals(brand.white(), alias.foreground());
        assertEquals(brand.silver(), alias.muted());
        assertEquals(brand.accent(), alias.emphasis());
    }

    @Test void mappedRolesComeOnlyFromAliases() {
        var alias = new Alias(1, 2, 3, 4, 5);
        assertEquals(new Map(1, 2, 3, 4, 4, 5), Map.from(alias));
    }

    @Test void defaultMappedColorsAreOpaque() {
        var colors = Tokens.COLORS;
        for (int color : new int[] {colors.surface(), colors.surfaceRaised(), colors.text(),
                colors.textMuted(), colors.border(), colors.focus()})
            assertEquals(255, color >>> 24);
    }
}
