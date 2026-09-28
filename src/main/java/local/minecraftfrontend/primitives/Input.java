package local.minecraftfrontend.primitives;

import local.minecraftfrontend.tokens.Tokens;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.core.Sizing;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;

public final class Input {
    private Input() {}
    public static TextBoxComponent create(Component label, String value, Consumer<String> onChange) {
        return create(label, value, 128, onChange);
    }
    public static TextBoxComponent create(Component label, String value, int maxLength, Consumer<String> onChange) {
        if (maxLength < 1) throw new IllegalArgumentException("maxLength must be positive");
        Objects.requireNonNull(onChange);
        var input = Components.textBox(Sizing.fill(100));
        input.setMessage(label); input.setHint(label); input.setMaxLength(maxLength);
        input.setTextColor(Tokens.COLORS.text());
        input.text(value);
        input.onChanged().subscribe(onChange::accept);
        return input;
    }
}
