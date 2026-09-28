package local.minecraftfrontend.primitives;

import local.minecraftfrontend.tokens.Tokens;

import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.ScrollContainer;

/** Replaces the standalone row-offset widget with owo's bounded pixel-scroll viewport. */
public final class Scrollbar {
    private Scrollbar() {}
    public static <C extends Component> ScrollContainer<C> create(Sizing width, Sizing height, C content) {
        return Containers.verticalScroll(width, height, content)
            .scrollbar(ScrollContainer.Scrollbar.flat(Color.ofArgb(Tokens.COLORS.textMuted())))
            .scrollbarThiccness(3);
    }
}
