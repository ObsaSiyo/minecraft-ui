package local.minecraftfrontend;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.OwoUIAdapter;
import java.util.Objects;
import java.util.function.Function;

/** Internal Minecraft/owo lifecycle adapter, not a screen-type template. Access through Ui.screen. */
final class ContentScreen extends BaseOwoScreen<FlowLayout> {
    private final Function<Runnable, Component> content;

    /** Factory runs during initial screen build, not on construction or every resize. */
    ContentScreen(net.minecraft.network.chat.Component title, Function<Runnable, Component> content) {
        super(Objects.requireNonNull(title));
        this.content = Objects.requireNonNull(content);
    }
    @Override protected OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }
    @Override protected void build(FlowLayout root) {
        root.child(Objects.requireNonNull(content.apply(this::onClose), "Screen content"));
    }
    @Override public boolean isPauseScreen() { return false; }
}
