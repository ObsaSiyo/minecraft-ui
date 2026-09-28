package local.minecraftfrontend.patterns;

import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Sizing;
import java.util.Objects;
import local.minecraftfrontend.Ui;
import net.minecraft.ChatFormatting;

/** Groups content under a heading and optional description; no navigation or data policy. */
public final class TitledSections {
    private TitledSections() {}
    public static FlowLayout create(Sizing width, net.minecraft.network.chat.Component title,
                                    net.minecraft.network.chat.Component description, Component content) {
        Objects.requireNonNull(width); Objects.requireNonNull(title);
        Objects.requireNonNull(description); Objects.requireNonNull(content);
        var section = Ui.column(width, Sizing.content());
        var heading = Ui.text(title.copy().withStyle(ChatFormatting.BOLD));
        heading.horizontalSizing(Sizing.fill(100));
        section.child(heading);
        if (!description.getString().isBlank()) {
            var summary = Ui.muted(description);
            summary.horizontalSizing(Sizing.fill(100));
            section.child(summary);
        }
        section.child(content);
        return section;
    }
}
