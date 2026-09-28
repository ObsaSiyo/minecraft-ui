package local.minecraftfrontend;

import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import local.minecraftfrontend.tokens.Tokens;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApiBoundaryTest {
    private static final List<String> INTERNAL_PACKAGES = List.of(
        "local.minecraftfrontend.primitives.", "local.minecraftfrontend.components.",
        "local.minecraftfrontend.geometry.", "local.minecraftfrontend.tokens.",
        "local.minecraftfrontend.interfaces.", "local.minecraftfrontend.pages.",
        "local.minecraftfrontend.ContentScreen", "local.minecraftfrontend.patterns.");

    @Test void publicFacadeSignaturesDoNotExposeImplementationPackages() {
        for (var method : Ui.class.getDeclaredMethods()) {
            if (!Modifier.isPublic(method.getModifiers())) continue;
            String signature = method.toGenericString();
            for (String internal : INTERNAL_PACKAGES)
                assertFalse(signature.contains(internal), signature);
        }
    }

    @Test void demoUsesOnlyTheStableFrameworkEntryPoint() throws Exception {
        try (var sources = Files.walk(Path.of("src/demo/java"))) {
            for (Path source : sources.filter(path -> path.toString().endsWith(".java")).toList()) {
                String text = Files.readString(source);
                for (String internal : INTERNAL_PACKAGES)
                    assertFalse(text.contains(internal), source + " references " + internal);
            }
        }
    }

    @Test void publicColorRolesDelegateToMappedTokens() {
        assertEquals(Tokens.COLORS.surface(), Ui.color(Ui.ColorRole.SURFACE));
        assertEquals(Tokens.COLORS.surfaceRaised(), Ui.color(Ui.ColorRole.SURFACE_RAISED));
        assertEquals(Tokens.COLORS.text(), Ui.color(Ui.ColorRole.TEXT));
        assertEquals(Tokens.COLORS.textMuted(), Ui.color(Ui.ColorRole.TEXT_MUTED));
        assertEquals(Tokens.COLORS.border(), Ui.color(Ui.ColorRole.BORDER));
        assertEquals(Tokens.COLORS.focus(), Ui.color(Ui.ColorRole.FOCUS));
    }
}
