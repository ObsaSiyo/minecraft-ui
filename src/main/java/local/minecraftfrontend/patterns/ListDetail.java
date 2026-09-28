package local.minecraftfrontend.patterns;

import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Sizing;
import java.util.Objects;
import local.minecraftfrontend.Ui;

/** Vertical list/detail pattern. Selection and data remain outside the layout. */
public final class ListDetail {
    private ListDetail() {}
    public static FlowLayout create(Sizing width, Sizing height, Component list, Component detail) {
        Objects.requireNonNull(width); Objects.requireNonNull(height);
        Objects.requireNonNull(list); Objects.requireNonNull(detail);
        var layout = Ui.column(width, height);
        layout.child(Ui.scroll(Sizing.fill(100), Sizing.expand(60), list));
        layout.child(Ui.scroll(Sizing.fill(100), Sizing.expand(40), detail));
        return layout;
    }
}
