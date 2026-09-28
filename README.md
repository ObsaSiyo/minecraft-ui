# Minecraft Frontend

A small, client-only Java component layer over **owo-ui**, using Ciel's existing palette and shadcn-inspired small, composable factories. This is an initial local toolkit, not a React runtime or a complete shadcn port.

## Boundary

- Library: shallow `interfaces/`, `pages/`, `patterns/`, `geometry/`, `primitives/`, `components/` and `tokens/` packages under `src/main/java/local/minecraftfrontend/`. `Ui` is the stable consumer entry point; `tokens/Tokens.java` assembles shared tokens.
- All 16 Ciel shared UI primitives, 4 layout helpers and 8 geometry helpers are represented below. Compose supplied components; there is no list-data model.
- Callers own filtering, selection, data, actions, persistence and gameplay requests.
- owo/Minecraft own layout, native editing, scrolling, input dispatch and focus. No custom hooks, application-state layer, renderer engine or networking.
- No HUD, preset/gameplay workflows, trees, drag/drop, controller adapter, theme editor, XML, installer or cross-loader abstraction. Tabs, breadcrumbs, dialogs and list rows provide presentation/basic input only.
- The fixture mod is a separate `demo` source set. Its search/selection logic is not shipped in the library JAR. Ciel was not modified.

## Structure and reference hierarchy

```text
src/main/java/local/minecraftfrontend/
├── Ui.java
├── ContentScreen.java  # Internal Minecraft/owo lifecycle adapter
├── interfaces/   # Workspace, SearchDiscovery, KnowledgeReference, TransactionalForm
├── pages/        # SearchResults, Reference, Form
├── patterns/     # ListDetail, TitledSections, CollapsiblePanels, ActionPanel
├── geometry/     # Point, Size, Rect, Insets, Line, Circle, Arc, Fill
├── primitives/   # Controls, drawing primitives and layout containers
├── components/   # IconButton, Slot, Tabs, Breadcrumbs, Dialog, ListRow
└── tokens/
    ├── Brand.java
    ├── Alias.java
    ├── Map.java
    └── Tokens.java
```

These are shallow sibling packages, not a deeply nested hierarchy. Checkbox remains a semantic input primitive even though its native implementation reuses Button. `Paint` stays package-private inside primitives.

The reference hierarchy describes composition: **Interface → Page/Screen type → Pattern → Component → Primitive → Token**. Pages build into interfaces: `SearchDiscovery` uses `pages/SearchResults` (which uses `patterns/ListDetail`); `KnowledgeReference` uses `pages/Reference`; `TransactionalForm` uses `pages/Form`. `Workspace` supplies the application shell around caller-supplied page content. Page layouts live only in `pages/`, not duplicated inside the interface-family entry points. Geometry is supporting math, not an additional product layer. Framework interfaces are reusable presentation families, not Java `interface` declarations or domain controllers. Application-specific content, workflows and navigation remain consumer-owned; the fixture application stays under `src/demo`. The four named families have starter compositions, not exhaustive implementations of those interface types. No placeholders are generated for the other eight. The hierarchy is a design model, not a requirement to route every Java call through every layer.

## Stable imports

Consumer screens need one framework import:

```java
import local.minecraftfrontend.Ui;

var action = Ui.button(Component.literal("Apply"), onApply);
var row = Ui.row(Sizing.fill(100), Sizing.content());
row.child(Ui.element(action));
int accent = Ui.color(Ui.ColorRole.FOCUS);
```

Keep Minecraft text and owo layout imports as usual. `Ui` exposes the frontend factories, color roles, glyphs and slot states. Its public signatures use only Java, Minecraft, owo and its own nested enums—not implementation-package types. The demo uses this entry point exclusively. API-boundary tests reject implementation types in facade signatures or internal-package references in demo code.

Java does not provide JavaScript-style re-exports or configurable import aliases. Keep `local.minecraftfrontend.Ui` stable when relocating implementations and update its internal imports/delegation. This protects source imports for facade consumers, not arbitrary API changes or Minecraft/owo upgrades. Direct imports from implementation packages remain available for advanced use, but those consumers must update when those packages move. No reflection-based loader, registry or duplicate compatibility classes are introduced.

`Tokens.java` is the central entry point for implementation styling. Its siblings `Brand.java`, `Alias.java` and `Map.java` own the existing color stages; there are no nested token folders.

## Interface compositions, pages, patterns and screen hosting

| Stable entry point | Composition | Caller supplies/owns |
| --- | --- | --- |
| `Ui.workspace(...)` | Heading, navigation, scrolling content, footer | Navigation controls, section selection and close callback |
| `Ui.searchDiscovery(...)` | Search/discovery interface built from a SearchResults page | Existing search control with callback, filtered result components, selected-item details |
| `Ui.knowledgeReference(...)` | Knowledge/reference interface built from a Reference page | Text, hierarchy, links and their actions |
| `Ui.transactionalForm(...)` | Transactional interface built from a Form page | Validation, enabled/pending state, request handling, confirmation safeguards and persistence |
| `Ui.searchResultsPage(...)` | Search field, summary, list/detail pattern | Caller-owned filtering and selection; reusable across interface families |
| `Ui.referencePage(...)` | Heading, breadcrumbs, reading area and related content | Supplied content and navigation |
| `Ui.formPage(...)` | Scrolling fields, feedback and actions | Supplied controls, validation and action callbacks |
| `Ui.screen(title, close -> content)` | Native non-pausing owo screen host | Content factory; receives a close callback; not a router or state store |
| `Ui.listDetail(...)` | Vertical independently scrolling list/detail areas | Supplied content; no search or selection behavior |

The compositions return native owo layouts. Callers retain their input/button/label handles and can update them without rebuilding the whole screen. No controls are implicitly submitted, enabled, saved or connected to gameplay. Create fresh child components for each composition—an owo component cannot belong to two parents simultaneously.

Use bounded fixed/fill/expand height for these compositions, not content-derived height: their viewports expand into remaining space. `ListDetail` divides that space 60/40; reference body/related content uses 75/25. Navigation, feedback and action areas take their normal height. These are initial vertical layouts, not complete responsive templates; tiny windows or very long header/feedback content require consumer constraints and in-client checking. The fixture places fixed-height examples inside a scrolling Workspace body.

`ContentScreen.java` is a package-private lifecycle adapter beside `Ui.java`, not a Settings/Detail/Search Results template. It remains accessible through `Ui.screen(...)`; the old `screens` package is removed. Reusable page/screen layouts now live in `pages/`; there is no separate `screens/` folder. `ContentScreen` builds its content once and delegates resize/focus/input to owo. It does not fetch data, poll state or manage Ciel's screen history. The fixture demonstrates all four families through the stable `Ui` import, with local search, selection and form callbacks outside the library.

## Reusable patterns

| Stable entry point | Vault pattern | Contract |
| --- | --- | --- |
| `Ui.listDetail(...)` | Master-detail | Independently scrolling supplied list/detail content |
| `Ui.titledSection(width, title, description, content)` | Titled Sections | Bold wrapped heading, optional wrapped description and supplied content; compose multiple sections in a column |
| `Ui.collapsiblePanel(width, title, content, expanded, onExpandedChange)` | Collapsible Panels | Header requests expansion/collapse; only caller-supplied state controls whether content is mounted |
| `Ui.actionPanel(width, actions)` | Action Panel | Contextual buttons with visible supplied availability explanations; no action or authorization policy |

A `Ui.Action` contains a label, enabled supplier, explanation supplier and callback. Availability/explanations refresh with the presentation, and activation rechecks enabled state. Blank explanations occupy no extra row. Callbacks are invoked once per activation, not retried or treated as successful persistence. Suppliers must be cheap, side-effect-free and return current values on the client thread; explanations must be nonnull (use empty text to omit them).

Collapsible content is retained while detached, preserving its widget values. A click never changes the supplied expansion state optimistically; ignoring a request leaves the panel unchanged. External collapse removes the content from the input tree and redirects focus from a hidden descendant to the header when mounted. This is widget presentation state, not a hooks/store layer. Native button activation provides keyboard operation; no custom animation or gameplay logic is introduced. Focus behavior still requires in-client verification.

These three additions are the first agreed batch. `InputHints`, `ProgressiveDisclosure` and `Filtering` remain deferred, not implemented placeholders.

## Navigation, dialog and list components

| Stable entry point | Contract |
| --- | --- |
| `Ui.tabs(width, choices, selected, onSelect)` | Horizontally scrollable section buttons. Caller owns selected ID; disabled or already-selected entries emit no selection request. Selected entries have a marker and narration. |
| `Ui.breadcrumbs(width, path, onNavigate)` | Horizontally scrollable supplied path with separators. Earlier enabled entries navigate; the final entry is a non-actionable current location. |
| `Ui.listRow(label, supporting, icon, selected, enabled, onPress)` | Optional full-texture icon, clipped primary/supporting text, live selected/enabled suppliers and a guarded callback. Selection and focus have distinct markers. |
| `Ui.dialog(parent, opener, title, dismissLabel, contentFactory)` | Separate non-pausing modal screen: parent controls are outside its focus/input tree. Initial focus is dismiss; Escape/dismiss returns to the parent. Restores the supplied opener if it survives parent reinitialization and remains focusable. |

Navigation uses `new Ui.Choice(id, label, enabled)` snapshots. IDs must be unique/nonblank; the options list and labels are copied. Rebuild the navigation group when its path or availability changes; its selected-ID supplier is read live. Native owo Tab/Shift+Tab and directional traversal are retained—not a custom tablist keybinding model. Callbacks request navigation; the framework never changes sections itself.

The dialog does not render the parent behind it, save changes or decide confirmation eligibility. Its content factory receives a dismiss callback. Callers create any Confirm button, implement validation/pending/error behavior and decide when to dismiss. Pass the actual opening widget via `Ui.element(opener)`; a replaced/removed opener cannot be restored by reference. For unrelated custom screens that rebuild their widget tree, callers still need their own stable-ID focus strategy. No focus or input claim here is runtime-verified yet.

The fixture includes Library/Primitives tabs, a disabled tab, breadcrumbs, selected/disabled list rows and a confirmation dialog with a Cancel-first focus policy. Its confirmation only changes local fixture text; no Ciel or server actions occur.

## Primitive inventory

| Ciel primitive(s) | owo adaptation |
| --- | --- |
| `Button` | owo ButtonComponent subclass; default, row, left-row and menu styles; clipped text and accessible-name override |
| `IconButton` | Button subclass with all six Ciel pixel glyphs, optional caret and attached tooltip |
| `Checkbox` | Controlled Button subclass; caller supplies checked state and callback; box-only or labeled row |
| `Input`, `Label`, `Icon` | owo text box, label and whole-texture components; clipped label drawing retained for custom controls |
| `Texture` | Layout-aware atlas sprite; preserves Minecraft nine-slice metadata and optional supplied tint |
| `Progress`, `Orb`, `Mask` | Display-only owo BaseComponents reading caller-supplied fractions |
| `Slot` | owo StackLayout with supplied NORMAL/SELECTED/DISABLED frame state |
| `Surface` | Composable owo background/outline functions |
| `Tooltip` | Attached owo tooltip, drawn in the overlay pass rather than at manual coordinates |
| `Clip` | Clipped owo container plus balanced scissor scope for custom drawing |
| `Scrollbar` | owo ScrollContainer; native pixel scrolling replaces Ciel's standalone row-offset scrollbar |
| `ResizeHandle` | Native widget participating in owo layout/input; emits pointer or keyboard deltas only |
| `Row`, `Column`, `Grid`, `Stack` | Native owo container factories plus retained pure Ciel cell/bounds calculations |
| `Point`, `Size`, `Rect`, `Insets`, `Line`, `Circle`, `Arc`, `Fill` | Retained validated, renderer-independent geometry math |

`Paint` is a package-private adapter for display-only primitives, not a renderer engine. No Ciel assets, feature controllers, hooks, integration stores or gameplay APIs were copied.

### Migration notes

- This is a source adaptation, **not a drop-in Ciel API**. Component factories replace screen-owned x/y coordinates with owo `Sizing`. Add native widgets using `Ui.element(...)`; other components can be added directly.
- Fractions, checked state, selection and actions remain caller-owned. A disabled `Slot` is only a frame appearance; callers must disable interactive children separately.
- Attach tooltips through the stable API with `Ui.tooltip(Ui.element(button), text)`. Plain decorative components do not supply accessible value narration; callers must pair meters/icons with meaningful text.
- `Scrollbar.create(...)` owns pixel scrolling through owo, not a caller-owned row offset. Use the returned ScrollContainer API, such as `scrollTo(...)`. Ciel's standalone scrollbar keyboard/narration contract is not claimed equivalent; verify upstream behavior in-client.
- `ResizeHandle` emits GUI-space deltas. It does not resize a panel, save settings, or implement Escape/rollback. The gallery only displays requested deltas.
- `Row.cells`/`Column.cells`/`Grid.cells` remain pure equal-cell math. Native `create(...)` factories use owo child sizing; they do not promise identical equal-cell distribution. Native grid gaps use child margins.
- Ciel `Insets` order is **top/right/bottom/left**, while owo's is **top/bottom/left/right**. Use fully qualified owo Insets for component padding/margins; never pass the Ciel tuple unchanged.
- `Texture`, `Progress`, `Orb` and `Mask` have no intrinsic content measurement: use fixed/fill sizing, not `Sizing.content()` on these display components. `Orb.create` starts with a fixed diameter.

## Pinned platform

| Dependency | Pin |
| --- | --- |
| Java | 21 |
| Minecraft | 1.21.1 |
| NeoForge | 21.1.244 |
| owo-lib | NeoForge `0.12.15-beta.10+1.21`, immutable Modrinth ID `nQeQz7rx` |
| ModDevGradle | 2.0.147 |

The inspected owo JAR declares NeoForge `[21,)` and Minecraft `[1.21,)`, which admit these versions. This is a **beta dependency**. Compilation is not runtime compatibility evidence. owo-ui is distributed inside the broader owo-lib mod, which also includes nested dependencies; this project does not extract a UI-only artifact or bundle owo into its own JAR.

## Compose with Java

Use these factories inside an owo `BaseOwoScreen<FlowLayout>` or an existing owo adapter:

```java
var panel = Ui.panel(Sizing.fill(90), Sizing.content());
panel.child(Ui.text(Component.literal("Library")));
panel.child(Ui.element(Ui.input(Component.literal("Search"), "", 128, onQueryChanged)));
panel.child(Ui.scroll(Sizing.fill(100), Sizing.fixed(88), suppliedRows));
panel.child(Ui.element(Ui.button(Component.literal("Close"), this::onClose)));
root.child(panel);
```

`Component` above is Minecraft's text component; `Sizing` is from `io.wispforest.owo.ui.core`. `suppliedRows` is an owo component. See `src/demo/java/local/minecraftfrontend/demo/DemoScreen.java` for a complete compiling example.

Factories return owo controls or styled subclasses, leaving their input/content APIs available. Button styling is defined by its class/variant rather than the inherited `renderer(...)` hook. For example, callers can use `button.active(false)`, `label.text(...)` or `input.text(...)`. Setting input text after construction can emit its change callback; the caller must avoid feedback loops. The input's initial value is applied before subscribing. `Ui.element(widget)` exposes owo's mixin-injected component interface: use it only in an owo-loaded client, not a headless JVM. No extra widget wrapper is created.

Labels accept Minecraft text components, including translated text. Callers must choose widths/wrapping and supply short button labels, explanatory text and meaningful input labels. Keep essential information in text rather than icons alone. Buttons retain Ciel's mapped-color rendering, clipped labels and visible hover/focus feedback; input chrome retains upstream rendering. This is not pixel-identical shadcn styling.

## Theme

Existing Ciel values follow **Brand → Alias → Map → UI**. There are no component tokens. Shared spacing, padding and control height live alongside the theme. This project does not add a palette editor or multiple themes.

## Build and use

Requires an existing Java 21 and Gradle 8 installation (verified with Gradle 8.14.5):

```sh
gradle --no-daemon build
```

On the authoring Mac, Java is installed but not the shell default:

```sh
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
gradle --no-daemon build
```

`build` compiles the library **and demo**, runs the tests, and creates `build/libs/minecraft-frontend-0.1.0.jar` plus a sources JAR. The library is **not a standalone mod to drop into `mods/`**. A consuming mod must include these client-side classes in its own artifact (or configure a deliberate library packaging arrangement), declare its owo dependency, and make the pinned owo mod available at runtime. Keep library references out of dedicated-server/common initialization. No publishing or installer is supplied.

## Isolated fixture — launch only with approval

`gradle --no-daemon runDemo` is configured for `run/demo`, not Ciel's profile. Create a disposable world, then press **F8**. This fixture uses synthetic entries, a vanilla book texture and a primitive gallery; no Tensura dependency or live player data. No game was launched during implementation.

Manual acceptance still required:

- Search typing, empty results, selection and Close/Escape.
- Tab/Shift+Tab, Enter/Space, focus visibility and actual narration.
- Mouse wheel and nested scrollbars; small windows and multiple GUI scales.
- Resize with input, selection and scroll in progress. owo's base screen retains its component tree on resize; this has not been tested here.
- Resource reload and long translated labels; no clipped or unreachable controls.
- Gallery checkbox updates, icon tooltips, meter/slot states, clipping and resize-handle pointer/keyboard deltas.
- Workspace scrolling/Close, SearchDiscovery input/results/details, Reference content/links and Form disabled-submit/feedback/cancel. Check nested scroll focus and GUI-scale resizing.
- Tabs/breadcrumb overflow, disabled/current actions, list-row focus versus selection, and dialog focus containment, Cancel/Confirm/Escape, resize and return-to-opener.
- Titled-section wrapping, accepted/rejected disclosure requests, external collapse while a child is focused, retained child values on reopen, and action explanation/availability updates.

## Verification evidence

`gradle --no-daemon build` passed with Java 21 / Gradle 8.14.5: main and demo compile, **24 tests passed** (3 tokens + 10 geometry/cell-layout + 3 API-boundary + 4 navigation-input + 4 action-contract tests). Tests cover bounded fractions, overflow, rectangle edges, inset order, circle/arc/line math, layout remainder distribution, partial rows, immutable layer bounds, navigation ID validation, defensive navigation snapshots, live action eligibility checks and callback error propagation without retries. Archive inspection found only library classes; no fixture, Tensura or upstream owo classes are bundled. The interface/page compositions, screen host, components and four patterns are compiled and wired into the fixture, but are not runtime-tested. These checks do not establish native layout rendering, mixin loading, input, narration or modpack compatibility. Minecraft was not launched.

## References

- Ciel source: `../ciel-ui/src/main/java/ciel/shared/{ui,display,geometry,theme}/`; primitives and palette adapted, gameplay code not copied. Ciel is unchanged.
- Local design hierarchy: `../frontend-reference-vault/00-Frontend-Reference-Index.md`, `04-Components.md`, `05-Primitives.md`. Its optional component-token layer is intentionally not adopted.
- [owo UI guide](https://docs.wispforest.io/owo/ui/getting-started) — general docs; actual API usage was checked against the pinned NeoForge sources.
- [Pinned owo release](https://modrinth.com/mod/owo-lib/version/0.12.15-beta.10%2B1.21).
- [shadcn introduction](https://ui.shadcn.com/docs) — composition reference only; no React/Radix code or copy/install tooling.
