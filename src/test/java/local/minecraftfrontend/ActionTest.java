package local.minecraftfrontend;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.network.chat.Component;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Pure activation contracts; not native rendering/focus tests. */
class ActionTest {
    @Test void disabledActionDoesNotInvokeCallback() {
        var calls = new AtomicInteger();
        var action = new Ui.Action(Component.literal("Apply"), () -> false,
            () -> Component.literal("Unavailable"), calls::incrementAndGet);
        action.request();
        assertEquals(0, calls.get());
    }
    @Test void activationRechecksCurrentAvailability() {
        var enabled = new AtomicBoolean(true);
        var calls = new AtomicInteger();
        var action = new Ui.Action(Component.literal("Apply"), enabled::get,
            Component::empty, calls::incrementAndGet);
        enabled.set(false);
        action.request();
        assertEquals(0, calls.get());
        enabled.set(true);
        action.request();
        assertEquals(1, calls.get());
    }
    @Test void callbackFailureIsNotRetriedOrSwallowed() {
        var calls = new AtomicInteger();
        var action = new Ui.Action(Component.literal("Apply"), () -> true, Component::empty, () -> {
            calls.incrementAndGet();
            throw new IllegalStateException("Fixture failure");
        });
        assertThrows(IllegalStateException.class, action::request);
        assertEquals(1, calls.get());
    }
    @Test void labelsAreDefensiveAndCallbacksRequired() {
        var label = Component.literal("Original");
        var action = new Ui.Action(label, () -> true, Component::empty, () -> {});
        label.append(" changed");
        ((net.minecraft.network.chat.MutableComponent) action.label()).append(" changed again");
        assertEquals("Original", action.label().getString());
        assertThrows(NullPointerException.class,
            () -> new Ui.Action(Component.empty(), () -> true, Component::empty, null));
    }
}
