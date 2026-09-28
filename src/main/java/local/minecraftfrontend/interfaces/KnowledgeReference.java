package local.minecraftfrontend.interfaces;

import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Sizing;
import local.minecraftfrontend.pages.Reference;

/** Knowledge/reference family entry point built from a reusable Reference page. */
public final class KnowledgeReference {
    private KnowledgeReference() {}
    public static FlowLayout create(Sizing width, Sizing height, net.minecraft.network.chat.Component title,
                                    Component breadcrumbs, Component body, Component related) {
        return Reference.create(width, height, title, breadcrumbs, body, related);
    }
}
