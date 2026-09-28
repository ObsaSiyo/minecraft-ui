package local.minecraftfrontend.components;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.HorizontalAlignment;
import io.wispforest.owo.ui.core.OwoUIAdapter;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.VerticalAlignment;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;
import local.minecraftfrontend.Ui;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;

/** Modal native screen: background controls are not in its input/focus tree. No confirmation policy. */
public final class Dialog extends BaseOwoScreen<FlowLayout> {
    private final Screen parent;
    private final Component opener;
    private final net.minecraft.network.chat.Component dismissLabel;
    private final Function<Runnable, Component> content;
    private ButtonComponent dismiss;
    private boolean initialFocus;

    /** Pass the actual opener explicitly; mouse dispatch may not have focused it before its callback. */
    public Dialog(Screen parent, Component opener, net.minecraft.network.chat.Component title,
                  net.minecraft.network.chat.Component dismissLabel, Function<Runnable, Component> content) {
        super(Objects.requireNonNull(title));
        this.parent = Objects.requireNonNull(parent);
        this.opener = Objects.requireNonNull(opener);
        this.dismissLabel = Objects.requireNonNull(dismissLabel);
        this.content = Objects.requireNonNull(content);
    }
    @Override protected OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }
    @Override protected void build(FlowLayout root) {
        root.horizontalAlignment(HorizontalAlignment.CENTER);
        root.verticalAlignment(VerticalAlignment.CENTER);
        root.surface(Ui.surface(Ui.color(Ui.ColorRole.SURFACE)));
        var panel = Ui.panel(Sizing.fill(90), Sizing.fill(90));
        var heading = Ui.text(title);
        heading.horizontalSizing(Sizing.fill(100));
        panel.child(heading);
        panel.child(Ui.scroll(Sizing.fill(100), Sizing.expand(),
            Objects.requireNonNull(content.apply(this::onClose), "Dialog content")));
        dismiss = Ui.button(dismissLabel, this::onClose);
        panel.child(Ui.element(dismiss));
        root.child(panel);
    }
    @Override protected void init() {
        super.init();
        if (!initialFocus && !invalid && uiAdapter != null && dismiss != null) {
            uiAdapter.rootComponent.focusHandler().focus(Ui.element(dismiss), Component.FocusSource.KEYBOARD_CYCLE);
            initialFocus = true;
        }
    }
    @Override public void onClose() {
        // A stale callback must not replace a newer screen or reopen the parent after disconnection.
        if (minecraft == null || minecraft.screen != this) return;
        minecraft.setScreen(parent);
        if (parent.getFocused() instanceof OwoUIAdapter<?> adapter) {
            var descendants = new ArrayList<Component>();
            adapter.rootComponent.collectDescendants(descendants);
            if (descendants.contains(opener) && opener.canFocus(Component.FocusSource.KEYBOARD_CYCLE))
                adapter.rootComponent.focusHandler().focus(opener, Component.FocusSource.KEYBOARD_CYCLE);
        } else if (opener instanceof GuiEventListener control && parent.children().contains(control)) {
            parent.setFocused(control);
        }
    }
    @Override public boolean isPauseScreen() { return false; }
}
