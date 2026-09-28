package local.minecraftfrontend.primitives;

import io.wispforest.owo.ui.core.Component;
import java.util.List;

/** Attached tooltips use owo's overlay pass, outside component clipping. */
public final class Tooltip {
    private Tooltip() {}
    public static <C extends Component> C attach(C component, List<net.minecraft.network.chat.Component> lines) {
        component.tooltip((java.util.Collection<net.minecraft.network.chat.Component>) List.copyOf(lines));
        return component;
    }
    public static <C extends Component> C attach(C component, net.minecraft.network.chat.Component text) {
        return attach(component, List.of(text));
    }
}
