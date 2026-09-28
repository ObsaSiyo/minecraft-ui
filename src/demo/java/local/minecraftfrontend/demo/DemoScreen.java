package local.minecraftfrontend.demo;

import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.HorizontalAlignment;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.VerticalAlignment;
import java.util.List;
import java.util.Locale;
import local.minecraftfrontend.Ui;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/** Fixture-only application logic, deliberately excluded from the library JAR. */
public final class DemoScreen {
    private record Entry(String name, String description) {}
    private static final List<Entry> ENTRIES = List.of(
        new Entry("Water", "Fixture entry. No learned skills, costs or gameplay requests."),
        new Entry("Flame", "Fixture entry. Applications supply text and callbacks."),
        new Entry("Wind", "Fixture entry. Selection is owned by this demo, not the UI library."),
        new Entry("Earth", "Fixture entry. Resize the window to check layout and retained input."),
        new Entry("Light", "Fixture entry. Try Tab, Shift+Tab, Enter, scrolling and Escape."),
        new Entry("Shadow", "Fixture entry. This is not a live Tensura integration.")
    );

    private boolean checked, actionsEnabled;
    private boolean expanded = true;
    private String selectedEntry = "", section = "library";

    private DemoScreen() {}

    public static net.minecraft.client.gui.screens.Screen create() {
        var fixture = new DemoScreen();
        return Ui.screen(Component.literal("Frontend fixture"), fixture::build);
    }

    private FlowLayout build(Runnable close) {
        var root = Ui.column(Sizing.fill(100), Sizing.fill(100));
        root.horizontalAlignment(HorizontalAlignment.CENTER);
        root.verticalAlignment(VerticalAlignment.CENTER);
        var panel = Ui.column(Sizing.fill(100), Sizing.content());
        panel.child(Ui.icon(ResourceLocation.withDefaultNamespace("textures/item/book.png"), 16, 16, 16));
        panel.child(Ui.text(Component.literal("Library · fixture data")));
        var rows = Ui.column(Sizing.fill(100), Sizing.content());
        var detail = Ui.text(Component.literal("Select an entry. No gameplay actions are connected."));
        detail.horizontalSizing(Sizing.fill(100));
        var count = Ui.muted(Component.empty());
        java.util.function.Consumer<String> filter = query -> {
            rows.clearChildren();
            String needle = query.toLowerCase(Locale.ROOT);
            int matches = 0;
            for (var entry : ENTRIES) {
                if (!entry.name().toLowerCase(Locale.ROOT).contains(needle)) continue;
                matches++;
                rows.child(Ui.element(Ui.listRow(Component.literal(entry.name()),
                    Component.literal(entry.name().equals("Shadow") ? "Disabled fixture" : "Select to inspect"),
                    ResourceLocation.withDefaultNamespace("textures/item/book.png"),
                    () -> selectedEntry.equals(entry.name()), () -> !entry.name().equals("Shadow"), () -> {
                        selectedEntry = entry.name();
                        detail.text(Component.literal(entry.name() + ": " + entry.description()));
                    })));
            }
            if (matches == 0) rows.child(Ui.muted(Component.literal("No matching entries")));
            count.text(Component.literal(matches + " entries"));
        };
        var search = Ui.input(Component.literal("Search entries"), "", 128, filter);
        panel.child(Ui.searchDiscovery(Sizing.fill(100), Sizing.fixed(180), search, count, rows, detail));
        var referenceBody = Ui.text(Component.literal("Reference fixture. Descriptions and related links are supplied by the consuming mod."));
        referenceBody.horizontalSizing(Sizing.fill(100));
        panel.child(Ui.knowledgeReference(Sizing.fill(100), Sizing.fixed(130), Component.literal("Knowledge / Reference"),
            Ui.breadcrumbs(Sizing.fill(100), List.of(
                new Ui.Choice("library", Component.literal("Library"), true),
                new Ui.Choice("reference", Component.literal("Reference"), true)),
                id -> referenceBody.text(Component.literal("Navigation requested: " + id))), referenceBody,
            Ui.element(Ui.rowButtonLeft(Component.literal("Inspect selected entry"), () -> referenceBody.text(detail.text())))));
        panel.child(form());
        panel.child(patterns());
        filter.accept("");
        var gallery = primitives();
        var body = Ui.column(Sizing.fill(100), Sizing.content());
        body.child(panel);
        var tabs = Ui.tabs(Sizing.fill(100), List.of(
            new Ui.Choice("library", Component.literal("Library"), true),
            new Ui.Choice("primitives", Component.literal("Primitives"), true),
            new Ui.Choice("unavailable", Component.literal("Unavailable"), false)), () -> section, next -> {
                section = next;
                body.clearChildren();
                body.child(next.equals("library") ? panel : gallery);
            });
        root.child(Ui.workspace(Sizing.fill(90), Sizing.fill(90), Component.literal("Workspace fixture"),
            tabs, body,
            Ui.element(Ui.button(Component.literal("Close"), close))));
        return root;
    }

    /** Gallery exercises construction/composition; callbacks are fixture-owned. */
    private FlowLayout primitives() {
        var gallery = Ui.column(Sizing.fill(100), Sizing.content());
        gallery.child(Ui.text(Component.literal("Primitive gallery")));
        gallery.child(Ui.element(Ui.checkbox(() -> Component.literal("Fixture selection"),
            () -> checked, () -> checked = !checked, true)));
        var controls = Ui.row(Sizing.fill(100), Sizing.content());
        controls.child(Ui.element(Ui.iconButton(Component.literal("Toggle fixture selection"),
            Ui.Glyph.GEAR, true, () -> checked = !checked)));
        controls.child(Ui.orb(10, () -> checked ? 1 : 0.4, Ui.color(Ui.ColorRole.FOCUS)));
        var slot = Ui.slot(Sizing.fixed(24), Sizing.fixed(24),
            () -> checked ? Ui.SlotState.SELECTED : Ui.SlotState.NORMAL);
        slot.child(Ui.icon(ResourceLocation.withDefaultNamespace("textures/item/book.png"), 16, 16, 16));
        slot.child(Ui.mask(Sizing.fill(100), Sizing.fill(100), () -> checked ? 0 : 0.5, Ui.color(Ui.ColorRole.SURFACE)));
        controls.child(slot);
        gallery.child(controls);
        gallery.child(Ui.progress(Sizing.fill(100), () -> checked ? 1 : 0.4));
        gallery.child(Ui.texture(ResourceLocation.withDefaultNamespace("hud/hotbar"), Sizing.fixed(182), Sizing.fixed(22)));
        var grid = Ui.grid(Sizing.fill(100), Sizing.content(), 1, 2);
        grid.child(Ui.element(Ui.rowButtonLeft(Component.literal("Row"), () -> checked = !checked)), 0, 0);
        var menu = Ui.menuItem(Component.literal("Menu item"), () -> checked = !checked);
        Ui.tooltip(Ui.element(menu), Component.literal("Fixture callback only"));
        grid.child(Ui.element(menu), 0, 1);
        gallery.child(grid);
        gallery.child(Ui.clip(Sizing.fill(100), Sizing.fixed(12),
            Ui.text(Component.literal("Clipped viewport — no scrolling here."))));
        var request = Ui.muted(Component.literal("Resize handle emits deltas; no settings are saved."));
        request.horizontalSizing(Sizing.fill(100));
        gallery.child(request);
        gallery.child(Ui.element(Ui.resizeHandle(Component.literal("Fixture resize request"),
            (dx, dy) -> request.text(Component.literal("Requested delta: " + dx + ", " + dy)))));
        return gallery;
    }

    private FlowLayout patterns() {
        var content = Ui.column(Sizing.fill(100), Sizing.content());
        var result = Ui.muted(Component.literal("No action requested."));
        result.horizontalSizing(Sizing.fill(100));
        content.child(Ui.element(Ui.checkbox(() -> Component.literal("Enable fixture action"),
            () -> actionsEnabled, () -> actionsEnabled = !actionsEnabled, true)));
        var actions = Ui.actionPanel(Sizing.fill(100), List.of(
            new Ui.Action(Component.literal("Run local fixture"), () -> actionsEnabled,
                () -> Component.literal(actionsEnabled ? "Ready. No server or file writes." : "Enable the checkbox first."),
                () -> result.text(Component.literal("Local fixture action requested."))),
            new Ui.Action(Component.literal("Collapse from inside"), () -> true,
                () -> Component.literal("Keyboard focus should return to the disclosure header."),
                () -> expanded = false)));
        content.child(Ui.collapsiblePanel(Sizing.fill(100), Component.literal("Contextual actions"),
            actions, () -> expanded, next -> expanded = next));
        content.child(result);
        return Ui.titledSection(Sizing.fill(100), Component.literal("Pattern fixtures"),
            Component.literal("Expansion, availability and actions are controlled by this demo."), content);
    }

    private FlowLayout form() {
        var fields = Ui.column(Sizing.fill(100), Sizing.content());
        fields.child(Ui.text(Component.literal("Transactional / Form — fixture only")));
        var feedback = Ui.muted(Component.literal("Enter a name. Nothing will be saved."));
        feedback.horizontalSizing(Sizing.fill(100));
        var name = Ui.input(Component.literal("Fixture name"), "", 32, ignored -> {});
        var submit = Ui.button(Component.literal("Submit fixture"),
            () -> feedback.text(Component.literal("Fixture submitted: " + name.getValue())));
        submit.active(false);
        name.onChanged().subscribe(value -> submit.active(!value.isBlank()));
        fields.child(Ui.element(name));
        var opener = Ui.button(Component.literal("Open confirmation fixture"), () -> {});
        opener.onPress(button -> {
            var client = net.minecraft.client.Minecraft.getInstance();
            var parent = client.screen;
            if (parent == null) return;
            client.setScreen(Ui.dialog(parent, Ui.element(button), Component.literal("Confirmation fixture"),
                Component.literal("Cancel"), dismiss -> {
                    var content = Ui.column(Sizing.fill(100), Sizing.content());
                    var warning = Ui.text(Component.literal("Confirm this local fixture action? No server request or save will occur."));
                    warning.horizontalSizing(Sizing.fill(100));
                    content.child(warning);
                    content.child(Ui.element(Ui.button(Component.literal("Confirm fixture"), () -> {
                        feedback.text(Component.literal("Confirmed locally."));
                        dismiss.run();
                    })));
                    return content;
                }));
        });
        fields.child(Ui.element(opener));
        var cancel = Ui.button(Component.literal("Clear fixture"), () -> {
            name.text("");
            feedback.text(Component.literal("Cleared locally. Nothing was saved."));
        });
        return Ui.transactionalForm(Sizing.fill(100), Sizing.fixed(165), fields, feedback, submit, cancel);
    }
}
