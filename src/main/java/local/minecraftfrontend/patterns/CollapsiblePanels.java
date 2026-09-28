package local.minecraftfrontend.patterns;

import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.ParentComponent;
import io.wispforest.owo.ui.core.Sizing;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import local.minecraftfrontend.Ui;
import local.minecraftfrontend.tokens.Tokens;

/** Controlled disclosure. A click requests a state change; only the supplied state reveals content. */
public final class CollapsiblePanels extends FlowLayout {
    private final net.minecraft.network.chat.Component title;
    private final Component content;
    private final BooleanSupplier expanded;
    private final ButtonComponent header;
    private boolean shown;

    private CollapsiblePanels(Sizing width, net.minecraft.network.chat.Component title, Component content,
                              BooleanSupplier expanded, Consumer<Boolean> onExpandedChange) {
        super(width, Sizing.content(), Algorithm.VERTICAL);
        this.title = title.copy(); this.content = content; this.expanded = expanded;
        gap(Tokens.GAP);
        header = Ui.button(title, () -> onExpandedChange.accept(!expanded.getAsBoolean()),
            () -> title.copy().append(expanded.getAsBoolean() ? ". Expanded." : ". Collapsed."));
        child(Ui.element(header));
        updateHeader(false);
        synchronize();
    }
    public static FlowLayout create(Sizing width, net.minecraft.network.chat.Component title, Component content,
                                    BooleanSupplier expanded, Consumer<Boolean> onExpandedChange) {
        Objects.requireNonNull(width); Objects.requireNonNull(title); Objects.requireNonNull(content);
        Objects.requireNonNull(expanded); Objects.requireNonNull(onExpandedChange);
        return new CollapsiblePanels(width, title, content, expanded, onExpandedChange);
    }
    @Override protected void parentUpdate(float delta, int mouseX, int mouseY) {
        synchronize();
    }
    private void synchronize() {
        boolean next = expanded.getAsBoolean();
        if (next == shown) return;
        if (next) child(content);
        else {
            // Hidden descendants must not keep keyboard focus after an external collapse.
            var focus = focusHandler();
            if (focus != null && focus.focused() != null) {
                var descendants = new ArrayList<Component>();
                descendants.add(content);
                if (content instanceof ParentComponent parent) parent.collectDescendants(descendants);
                if (descendants.contains(focus.focused()))
                    focus.focus(Ui.element(header), Component.FocusSource.KEYBOARD_CYCLE);
            }
            removeChild(content);
        }
        shown = next;
        updateHeader(next);
    }
    private void updateHeader(boolean open) {
        header.setMessage(net.minecraft.network.chat.Component.literal(open ? "▾ " : "▸ ").append(title));
    }
}
