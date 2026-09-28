package local.minecraftfrontend;

import local.minecraftfrontend.tokens.Tokens;
import local.minecraftfrontend.primitives.*;
import local.minecraftfrontend.components.IconButton;
import local.minecraftfrontend.components.Slot;
import local.minecraftfrontend.components.Tabs;
import local.minecraftfrontend.components.Breadcrumbs;
import local.minecraftfrontend.components.Dialog;
import local.minecraftfrontend.components.ListRow;
import local.minecraftfrontend.interfaces.Workspace;
import local.minecraftfrontend.interfaces.SearchDiscovery;
import local.minecraftfrontend.interfaces.KnowledgeReference;
import local.minecraftfrontend.interfaces.TransactionalForm;
import local.minecraftfrontend.patterns.ListDetail;
import local.minecraftfrontend.patterns.TitledSections;
import local.minecraftfrontend.patterns.CollapsiblePanels;
import local.minecraftfrontend.patterns.ActionPanel;
import local.minecraftfrontend.pages.SearchResults;
import local.minecraftfrontend.pages.Reference;
import local.minecraftfrontend.pages.Form;

import io.wispforest.owo.ui.base.BaseComponent;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.component.TextureComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.container.GridLayout;
import io.wispforest.owo.ui.container.StackLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Sizing;
import java.util.Objects;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.ResourceLocation;

/** Stable consumer import. Public signatures expose only Ui, Java, Minecraft and owo types. */
public final class Ui {
    private Ui() {}
    public enum Glyph { BOOK, PRESETS, PERSON, TREE, GEAR, SLIME }
    public enum SlotState { NORMAL, SELECTED, DISABLED }
    public enum ColorRole { SURFACE, SURFACE_RAISED, TEXT, TEXT_MUTED, BORDER, FOCUS }

    /** Immutable navigation identity/availability snapshot; selection stays with the caller. */
    public record Choice(String id, net.minecraft.network.chat.Component label, boolean enabled) {
        public Choice {
            if (Objects.requireNonNull(id).isBlank()) throw new IllegalArgumentException("Choice ID must not be blank");
            label = Objects.requireNonNull(label).copy();
        }
        @Override public net.minecraft.network.chat.Component label() { return label.copy(); }
    }
    /** Validate before creating native widgets. Duplicate IDs make controlled selection ambiguous. */
    public static List<Choice> choices(List<Choice> source) {
        var result = List.copyOf(source);
        var ids = new java.util.HashSet<String>();
        for (var choice : result)
            if (!ids.add(choice.id())) throw new IllegalArgumentException("Duplicate choice ID: " + choice.id());
        return result;
    }

    /** Presentation callbacks only; callers own availability and explanations. */
    public record Action(net.minecraft.network.chat.Component label, BooleanSupplier enabled,
                         Supplier<net.minecraft.network.chat.Component> explanation, Runnable onPress) {
        public Action {
            label = Objects.requireNonNull(label).copy();
            Objects.requireNonNull(enabled); Objects.requireNonNull(explanation); Objects.requireNonNull(onPress);
        }
        @Override public net.minecraft.network.chat.Component label() { return label.copy(); }
        /** Recheck at activation time, not only when the control was last rendered. */
        public void request() { if (enabled.getAsBoolean()) onPress.run(); }
    }

    public static int color(ColorRole role) {
        return switch (Objects.requireNonNull(role)) {
            case SURFACE -> Tokens.COLORS.surface();
            case SURFACE_RAISED -> Tokens.COLORS.surfaceRaised();
            case TEXT -> Tokens.COLORS.text();
            case TEXT_MUTED -> Tokens.COLORS.textMuted();
            case BORDER -> Tokens.COLORS.border();
            case FOCUS -> Tokens.COLORS.focus();
        };
    }
    public static FlowLayout row(Sizing width, Sizing height) { return Row.create(width, height); }
    public static FlowLayout column(Sizing width, Sizing height) { return Column.create(width, height); }
    public static GridLayout grid(Sizing width, Sizing height, int rows, int columns) {
        return Grid.create(width, height, rows, columns);
    }
    public static StackLayout stack(Sizing width, Sizing height) { return Stack.create(width, height); }
    public static FlowLayout panel(Sizing width, Sizing height) {
        var panel = column(width, height);
        panel.padding(io.wispforest.owo.ui.core.Insets.of(Tokens.PADDING));
        panel.surface(Surface.panel());
        return panel;
    }
    public static LabelComponent text(net.minecraft.network.chat.Component value) { return Label.create(value); }
    public static LabelComponent muted(net.minecraft.network.chat.Component value) { return Label.muted(value); }
    public static ButtonComponent button(net.minecraft.network.chat.Component label, Runnable onPress) {
        return Button.create(label, onPress);
    }
    public static ButtonComponent button(net.minecraft.network.chat.Component label, Runnable onPress,
                                         Supplier<net.minecraft.network.chat.Component> accessibleName) {
        return Button.create(label, onPress).accessibleName(accessibleName);
    }
    public static ButtonComponent rowButton(net.minecraft.network.chat.Component label, Runnable onPress) {
        return Button.row(label, onPress);
    }
    public static ButtonComponent rowButtonLeft(net.minecraft.network.chat.Component label, Runnable onPress) {
        return Button.rowLeft(label, onPress);
    }
    public static ButtonComponent menuItem(net.minecraft.network.chat.Component label, Runnable onPress) {
        return Button.menuItem(label, onPress);
    }
    public static ButtonComponent iconButton(net.minecraft.network.chat.Component name, Glyph glyph,
                                             boolean dropdown, Runnable onPress) {
        var internal = switch (Objects.requireNonNull(glyph)) {
            case BOOK -> IconButton.Glyph.BOOK;
            case PRESETS -> IconButton.Glyph.PRESETS;
            case PERSON -> IconButton.Glyph.PERSON;
            case TREE -> IconButton.Glyph.TREE;
            case GEAR -> IconButton.Glyph.GEAR;
            case SLIME -> IconButton.Glyph.SLIME;
        };
        return new IconButton(name, internal, dropdown, onPress);
    }
    public static ButtonComponent checkbox(Supplier<net.minecraft.network.chat.Component> label,
                                           BooleanSupplier checked, Runnable onPress, boolean labeled) {
        return new Checkbox(label, checked, onPress, labeled);
    }
    public static TextBoxComponent input(net.minecraft.network.chat.Component label, String value,
                                         int maxLength, Consumer<String> onChange) {
        return Input.create(label, value, maxLength, onChange);
    }
    public static <C extends Component> ScrollContainer<C> scroll(Sizing width, Sizing height, C content) {
        return Scrollbar.create(width, height, content);
    }
    public static TextureComponent icon(ResourceLocation texture, int textureWidth, int textureHeight, int size) {
        return Icon.create(texture, textureWidth, textureHeight, size);
    }
    public static BaseComponent texture(ResourceLocation sprite, Sizing width, Sizing height) {
        return Texture.create(sprite, width, height);
    }
    public static BaseComponent texture(ResourceLocation sprite, Sizing width, Sizing height, IntSupplier tint) {
        return Texture.create(sprite, width, height, tint);
    }
    public static BaseComponent progress(Sizing width, DoubleSupplier fraction) {
        return Progress.create(width, fraction);
    }
    public static BaseComponent orb(int radius, DoubleSupplier fraction, int mappedFill) {
        return Orb.create(radius, fraction, mappedFill);
    }
    public static BaseComponent mask(Sizing width, Sizing height, DoubleSupplier fraction, int mappedColor) {
        return Mask.create(width, height, fraction, mappedColor);
    }
    public static StackLayout slot(Sizing width, Sizing height, Supplier<SlotState> state) {
        Objects.requireNonNull(state);
        return Slot.create(width, height, () -> switch (Objects.requireNonNull(state.get())) {
            case NORMAL -> Slot.State.NORMAL;
            case SELECTED -> Slot.State.SELECTED;
            case DISABLED -> Slot.State.DISABLED;
        });
    }
    public static FlowLayout clip(Sizing width, Sizing height, Component content) {
        return Clip.create(width, height, content);
    }
    public static io.wispforest.owo.ui.core.Surface surface(int mappedColor) { return Surface.flat(mappedColor); }
    public static io.wispforest.owo.ui.core.Surface panelSurface() { return Surface.panel(); }
    public static <C extends Component> C tooltip(C component, net.minecraft.network.chat.Component text) {
        return Tooltip.attach(component, text);
    }
    public static <C extends Component> C tooltip(C component, List<net.minecraft.network.chat.Component> lines) {
        return Tooltip.attach(component, lines);
    }
    public static AbstractWidget resizeHandle(net.minecraft.network.chat.Component name, BiConsumer<Double, Double> onResize) {
        return new ResizeHandle(name, onResize);
    }
    public static ScrollContainer<FlowLayout> tabs(Sizing width, List<Choice> choices,
                                                   Supplier<String> selected, Consumer<String> onSelect) {
        return Tabs.create(width, choices, selected, onSelect);
    }
    public static ScrollContainer<FlowLayout> breadcrumbs(Sizing width, List<Choice> path, Consumer<String> onNavigate) {
        return Breadcrumbs.create(width, path, onNavigate);
    }
    public static ButtonComponent listRow(net.minecraft.network.chat.Component label,
                                           net.minecraft.network.chat.Component supporting, ResourceLocation icon,
                                           BooleanSupplier selected, BooleanSupplier enabled, Runnable onPress) {
        return new ListRow(label, supporting, icon, selected, enabled, onPress);
    }
    public static net.minecraft.client.gui.screens.Screen dialog(net.minecraft.client.gui.screens.Screen parent,
                                                                  Component opener, net.minecraft.network.chat.Component title,
                                                                  net.minecraft.network.chat.Component dismissLabel,
                                                                  Function<Runnable, Component> content) {
        return new Dialog(parent, opener, title, dismissLabel, content);
    }
    /** Bounded compositions: use fixed/fill/expand height, not content-derived height. */
    public static FlowLayout workspace(Sizing width, Sizing height, net.minecraft.network.chat.Component title,
                                        Component navigation, Component content, Component footer) {
        return Workspace.create(width, height, title, navigation, content, footer);
    }
    public static FlowLayout searchDiscovery(Sizing width, Sizing height, TextBoxComponent search,
                                              Component summary, Component results, Component details) {
        return SearchDiscovery.create(width, height, search, summary, results, details);
    }
    public static FlowLayout knowledgeReference(Sizing width, Sizing height, net.minecraft.network.chat.Component title,
                                                 Component breadcrumbs, Component body, Component related) {
        return KnowledgeReference.create(width, height, title, breadcrumbs, body, related);
    }
    public static FlowLayout transactionalForm(Sizing width, Sizing height, Component fields, Component feedback,
                                                ButtonComponent submit, ButtonComponent cancel) {
        return TransactionalForm.create(width, height, fields, feedback, submit, cancel);
    }
    /** Reusable pages can be composed into any interface family, independently of the family entry points. */
    public static FlowLayout searchResultsPage(Sizing width, Sizing height, TextBoxComponent search,
                                               Component summary, Component results, Component details) {
        return SearchResults.create(width, height, search, summary, results, details);
    }
    public static FlowLayout referencePage(Sizing width, Sizing height, net.minecraft.network.chat.Component title,
                                           Component breadcrumbs, Component body, Component related) {
        return Reference.create(width, height, title, breadcrumbs, body, related);
    }
    public static FlowLayout formPage(Sizing width, Sizing height, Component fields, Component feedback,
                                      ButtonComponent submit, ButtonComponent cancel) {
        return Form.create(width, height, fields, feedback, submit, cancel);
    }
    public static FlowLayout titledSection(Sizing width, net.minecraft.network.chat.Component title,
                                           net.minecraft.network.chat.Component description, Component content) {
        return TitledSections.create(width, title, description, content);
    }
    public static FlowLayout collapsiblePanel(Sizing width, net.minecraft.network.chat.Component title, Component content,
                                              BooleanSupplier expanded, Consumer<Boolean> onExpandedChange) {
        return CollapsiblePanels.create(width, title, content, expanded, onExpandedChange);
    }
    public static FlowLayout actionPanel(Sizing width, List<Action> actions) {
        return ActionPanel.create(width, actions);
    }
    public static FlowLayout listDetail(Sizing width, Sizing height, Component list, Component detail) {
        return ListDetail.create(width, height, list, detail);
    }
    public static net.minecraft.client.gui.screens.Screen screen(net.minecraft.network.chat.Component title,
                                                                 Function<Runnable, Component> content) {
        return new ContentScreen(title, content);
    }
    /** owo injects Component into AbstractWidget at runtime; no duplicate wrapper/focus tree. */
    public static Component element(AbstractWidget widget) { return (Component) Objects.requireNonNull(widget); }
}
