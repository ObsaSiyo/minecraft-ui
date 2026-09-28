package local.minecraftfrontend.components;

import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.Sizing;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import local.minecraftfrontend.Ui;
import local.minecraftfrontend.primitives.Button;
import local.minecraftfrontend.primitives.Tooltip;
import local.minecraftfrontend.tokens.Tokens;
import net.minecraft.client.gui.GuiGraphics;

/** Controlled section navigation. Selection is read, never changed by the component. */
public final class Tabs {
    private Tabs() {}
    public static ScrollContainer<FlowLayout> create(Sizing width, List<Ui.Choice> choices,
                                                     Supplier<String> selected, Consumer<String> onSelect) {
        var items = Ui.choices(choices);
        Objects.requireNonNull(selected); Objects.requireNonNull(onSelect);
        var row = Ui.row(Sizing.content(), Sizing.content());
        for (var item : items) {
            var button = new Button(item.label(), () -> {
                if (item.enabled() && !Objects.equals(selected.get(), item.id())) onSelect.accept(item.id());
            }, Button.Variant.ROW) {
                @Override public void renderWidget(GuiGraphics graphics, int mx, int my, float delta) {
                    super.renderWidget(graphics, mx, my, delta);
                    if (Objects.equals(selected.get(), item.id()))
                        graphics.fill(getX(), getY() + getHeight() - 1, getX() + getWidth(), getY() + getHeight(), Tokens.COLORS.focus());
                }
            };
            button.active(item.enabled());
            button.accessibleName(() -> item.label().copy().append(
                Objects.equals(selected.get(), item.id()) ? ". Selected." : ""));
            Ui.element(button).horizontalSizing(Sizing.content());
            Tooltip.attach(Ui.element(button), item.label());
            row.child(Ui.element(button));
        }
        return Containers.horizontalScroll(width, Sizing.fixed(Tokens.CONTROL_HEIGHT + Tokens.GAP), row);
    }
}
