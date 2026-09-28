package local.minecraftfrontend;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Pure navigation-input contracts; no native widgets or client lifecycle are instantiated. */
class NavigationTest {
    @Test void choiceRequiresStableIdentityAndLabel() {
        assertThrows(IllegalArgumentException.class, () -> new Ui.Choice(" ", Component.literal("Label"), true));
        assertThrows(NullPointerException.class, () -> new Ui.Choice("id", null, true));
    }
    @Test void duplicateIdsAreRejectedEvenWithDifferentLabels() {
        assertThrows(IllegalArgumentException.class, () -> Ui.choices(List.of(
            new Ui.Choice("same", Component.literal("First"), true),
            new Ui.Choice("same", Component.literal("Second"), false))));
    }
    @Test void navigationSnapshotIsImmutableAndKeepsDisabledEntries() {
        var source = new ArrayList<Ui.Choice>();
        source.add(new Ui.Choice("disabled", Component.literal("Disabled"), false));
        var snapshot = Ui.choices(source);
        source.clear();
        assertEquals(1, snapshot.size());
        assertFalse(snapshot.getFirst().enabled());
        assertThrows(UnsupportedOperationException.class, snapshot::clear);
        assertTrue(Ui.choices(List.of()).isEmpty());
    }
    @Test void labelsAreCopiedAtTheBoundary() {
        var label = Component.literal("Original");
        var choice = new Ui.Choice("id", label, true);
        label.append(" changed");
        assertEquals("Original", choice.label().getString());
        ((net.minecraft.network.chat.MutableComponent) choice.label()).append(" also changed");
        assertEquals("Original", choice.label().getString());
    }
}
