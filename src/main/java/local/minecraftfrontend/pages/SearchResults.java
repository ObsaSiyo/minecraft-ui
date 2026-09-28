package local.minecraftfrontend.pages;

import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Sizing;
import java.util.Objects;
import local.minecraftfrontend.Ui;
import local.minecraftfrontend.patterns.ListDetail;

/** Search-results page built from the list/detail pattern. Search and selection remain caller-owned. */
public final class SearchResults {
    private SearchResults() {}

    /** Caller retains the search control and content handles for updates and event callbacks. */
    public static FlowLayout create(Sizing width, Sizing height, TextBoxComponent search,
                                    Component summary, Component results, Component details) {
        Objects.requireNonNull(width); Objects.requireNonNull(height); Objects.requireNonNull(search);
        Objects.requireNonNull(summary); Objects.requireNonNull(results); Objects.requireNonNull(details);
        var layout = Ui.column(width, height);
        layout.child(Ui.element(search));
        layout.child(summary);
        layout.child(ListDetail.create(Sizing.fill(100), Sizing.expand(), results, details));
        return layout;
    }
}
