package local.minecraftfrontend.interfaces;

import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Sizing;
import local.minecraftfrontend.pages.SearchResults;

/** Search/discovery family entry point; its current composition is a SearchResults page. */
public final class SearchDiscovery {
    private SearchDiscovery() {}
    public static FlowLayout create(Sizing width, Sizing height, TextBoxComponent search,
                                    Component summary, Component results, Component details) {
        return SearchResults.create(width, height, search, summary, results, details);
    }
}
