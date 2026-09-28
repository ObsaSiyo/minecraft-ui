package local.minecraftfrontend.primitives;

import io.wispforest.owo.ui.base.BaseComponent;
import io.wispforest.owo.ui.core.OwoUIDrawContext;
import io.wispforest.owo.ui.core.Sizing;

/** Internal adapter: display-only drawing participates in owo layout and clipping. */
final class Paint extends BaseComponent {
    @FunctionalInterface interface Draw { void render(OwoUIDrawContext graphics, int x, int y, int width, int height); }
    private final Draw draw;
    Paint(Sizing width, Sizing height, Draw draw) {
        this.draw = java.util.Objects.requireNonNull(draw);
        sizing(width, height);
    }
    @Override public void draw(OwoUIDrawContext context, int mouseX, int mouseY, float partialTicks, float delta) {
        if (width > 0 && height > 0) draw.render(context, x, y, width, height);
    }
}
