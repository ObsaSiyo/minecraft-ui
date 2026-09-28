package local.minecraftfrontend.interfaces;

import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Sizing;
import java.util.Objects;
import local.minecraftfrontend.Ui;

/** Application interface shell around supplied pages. Caller owns page selection, history and close actions. */
public final class Workspace {
    private Workspace() {}

    /** Bounded shell: heading, navigation, supplied page(s) in a scrolling body, and footer. */
    public static FlowLayout create(Sizing width, Sizing height, net.minecraft.network.chat.Component title,
                                    Component navigation, Component content, Component footer) {
        Objects.requireNonNull(width); Objects.requireNonNull(height); Objects.requireNonNull(title);
        Objects.requireNonNull(navigation); Objects.requireNonNull(content); Objects.requireNonNull(footer);
        var shell = Ui.panel(width, height);
        var heading = Ui.text(title);
        heading.horizontalSizing(Sizing.fill(100));
        shell.child(heading);
        shell.child(navigation);
        shell.child(Ui.scroll(Sizing.fill(100), Sizing.expand(), content));
        shell.child(footer);
        return shell;
    }
}
