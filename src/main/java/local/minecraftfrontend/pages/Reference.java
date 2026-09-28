package local.minecraftfrontend.pages;

import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Sizing;
import java.util.Objects;
import local.minecraftfrontend.Ui;

/** Help/reference page. Supplied hierarchy, links and navigation remain caller-owned. */
public final class Reference {
    private Reference() {}

    /** A heading and breadcrumb area above independently scrollable body/related content. */
    public static FlowLayout create(Sizing width, Sizing height, net.minecraft.network.chat.Component title,
                                    Component breadcrumbs, Component body, Component related) {
        Objects.requireNonNull(width); Objects.requireNonNull(height); Objects.requireNonNull(title);
        Objects.requireNonNull(breadcrumbs); Objects.requireNonNull(body); Objects.requireNonNull(related);
        var layout = Ui.column(width, height);
        var heading = Ui.text(title);
        heading.horizontalSizing(Sizing.fill(100));
        layout.child(heading);
        layout.child(breadcrumbs);
        layout.child(Ui.scroll(Sizing.fill(100), Sizing.expand(75), body));
        layout.child(Ui.scroll(Sizing.fill(100), Sizing.expand(25), related));
        return layout;
    }
}
