package local.minecraftfrontend.components;

import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.Sizing;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import local.minecraftfrontend.Ui;
import local.minecraftfrontend.primitives.Button;
import local.minecraftfrontend.tokens.Tokens;
import net.minecraft.network.chat.Component;

/** Supplied path snapshot. Earlier entries navigate; the final entry denotes the current location. */
public final class Breadcrumbs {
    private Breadcrumbs() {}
    public static ScrollContainer<FlowLayout> create(Sizing width, List<Ui.Choice> path, Consumer<String> onNavigate) {
        var items = Ui.choices(path);
        Objects.requireNonNull(onNavigate);
        var row = Ui.row(Sizing.content(), Sizing.content());
        for (int index = 0; index < items.size(); index++) {
            var item = items.get(index);
            boolean current = index == items.size() - 1;
            if (index > 0) row.child(Ui.muted(Component.literal("›")));
            var button = Button.rowLeft(item.label(), () -> {
                if (item.enabled() && !current) onNavigate.accept(item.id());
            });
            button.active(item.enabled() && !current);
            button.accessibleName(() -> item.label().copy().append(current ? ". Current location." : ""));
            Ui.element(button).horizontalSizing(Sizing.content());
            row.child(Ui.element(button));
        }
        return Containers.horizontalScroll(width, Sizing.fixed(Tokens.CONTROL_HEIGHT + Tokens.GAP), row);
    }
}
