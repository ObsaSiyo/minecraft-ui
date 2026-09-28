package local.minecraftfrontend.patterns;

import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Sizing;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import local.minecraftfrontend.Ui;
import local.minecraftfrontend.tokens.Tokens;

/** Contextual actions with visible explanations. No authorization, pending state or action policy. */
public final class ActionPanel extends FlowLayout {
    private record Row(Ui.Action action, FlowLayout layout, ButtonComponent button, LabelComponent reason) {}
    private final List<Row> rows = new ArrayList<>();

    private ActionPanel(Sizing width, List<Ui.Action> actions) {
        super(width, Sizing.content(), Algorithm.VERTICAL);
        gap(Tokens.GAP);
        for (var action : actions) {
            var row = Ui.column(Sizing.fill(100), Sizing.content());
            var button = Ui.button(action.label(), action::request,
                () -> action.label().copy().append(". ").append(action.explanation().get()));
            var reason = Ui.muted(net.minecraft.network.chat.Component.empty());
            reason.horizontalSizing(Sizing.fill(100));
            row.child(Ui.element(button));
            rows.add(new Row(action, row, button, reason));
            child(row);
        }
        synchronize();
    }
    public static FlowLayout create(Sizing width, List<Ui.Action> actions) {
        return new ActionPanel(Objects.requireNonNull(width), List.copyOf(actions));
    }
    @Override protected void parentUpdate(float delta, int mouseX, int mouseY) {
        synchronize();
    }
    private void synchronize() {
        for (var row : rows) {
            row.button().active(row.action().enabled().getAsBoolean());
            var text = Objects.requireNonNull(row.action().explanation().get(), "Action explanation");
            if (!row.reason().text().equals(text)) row.reason().text(text.copy());
            boolean attached = row.layout().children().contains(row.reason());
            if (!text.getString().isBlank() && !attached) row.layout().child(row.reason());
            else if (text.getString().isBlank() && attached) row.layout().removeChild(row.reason());
        }
    }
}
