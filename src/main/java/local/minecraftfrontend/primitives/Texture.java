package local.minecraftfrontend.primitives;

import io.wispforest.owo.ui.base.BaseComponent;
import io.wispforest.owo.ui.core.Sizing;
import java.util.function.IntSupplier;
import net.minecraft.resources.ResourceLocation;

/** GUI-atlas sprites retain Minecraft stretch/tile/nine-slice metadata. */
public final class Texture {
    private Texture() {}
    public static BaseComponent create(ResourceLocation sprite, Sizing width, Sizing height) {
        return create(sprite, width, height, () -> 0xFFFFFFFF);
    }
    public static BaseComponent create(ResourceLocation sprite, Sizing width, Sizing height, IntSupplier tint) {
        java.util.Objects.requireNonNull(tint);
        return new Paint(width, height, (graphics, x, y, w, h) -> {
            if (sprite == null) return;
            int color = tint.getAsInt();
            graphics.setColor((color >> 16 & 255) / 255F, (color >> 8 & 255) / 255F,
                (color & 255) / 255F, (color >>> 24) / 255F);
            try { graphics.blitSprite(sprite, x, y, w, h); }
            finally { graphics.setColor(1, 1, 1, 1); }
        });
    }
}
