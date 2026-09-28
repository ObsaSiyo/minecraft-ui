package local.minecraftfrontend.pages;

import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Sizing;
import java.util.Objects;
import local.minecraftfrontend.Ui;

/** Bounded-task form page. Caller owns validation, pending state, submission and persistence. */
public final class Form {
    private Form() {}

    /** Supplied controls keep caller labels, enabled state and callbacks; no implicit submit. */
    public static FlowLayout create(Sizing width, Sizing height, Component fields, Component feedback,
                                    ButtonComponent submit, ButtonComponent cancel) {
        Objects.requireNonNull(width); Objects.requireNonNull(height); Objects.requireNonNull(fields);
        Objects.requireNonNull(feedback); Objects.requireNonNull(submit); Objects.requireNonNull(cancel);
        var layout = Ui.column(width, height);
        layout.child(Ui.scroll(Sizing.fill(100), Sizing.expand(), fields));
        layout.child(feedback);
        // Vertical actions remain usable without a separate compact-layout state machine.
        layout.child(Ui.element(submit));
        layout.child(Ui.element(cancel));
        return layout;
    }
}
