package local.minecraftfrontend.primitives;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.TextureComponent;
import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.resources.ResourceLocation;

public final class Icon {
    private Icon() {}
    /** Whole texture, with explicit source dimensions rather than assuming source equals display. */
    public static TextureComponent create(ResourceLocation texture, int sourceWidth, int sourceHeight, int size) {
        if (sourceWidth < 1 || sourceHeight < 1 || size < 1) throw new IllegalArgumentException("Positive dimensions required");
        var icon = Components.texture(java.util.Objects.requireNonNull(texture), 0, 0,
            sourceWidth, sourceHeight, sourceWidth, sourceHeight);
        icon.sizing(Sizing.fixed(size), Sizing.fixed(size));
        return icon;
    }
}
