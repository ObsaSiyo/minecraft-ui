package local.minecraftfrontend.primitives;

import local.minecraftfrontend.tokens.Tokens;
import local.minecraftfrontend.geometry.Rect;

import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Sizing;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Divides bounds vertically without drawing or owning child state. */
public final class Column {
    private Column() {}

    public static FlowLayout create(Sizing width, Sizing height) {
        return Containers.verticalFlow(width, height).gap(Tokens.GAP);
    }

    /** Equal-height cells; leftover pixels go to the first cells. Gaps must fit inside the bounds. */
    public static List<Rect> cells(Rect bounds, int count, int gap) {
        Objects.requireNonNull(bounds, "bounds");
        if (count < 0 || gap < 0) throw new IllegalArgumentException("Negative count or gap");
        if (count == 0) return List.of();
        long spacing = (long) gap * (count - 1);
        if (spacing > bounds.height()) throw new IllegalArgumentException("Column gaps exceed available height");
        int usable = bounds.height() - (int) spacing;
        int base = usable / count, extra = usable % count;
        var cells = new ArrayList<Rect>(count);
        int y = bounds.y();
        for (int i = 0; i < count; i++) {
            int height = base + (i < extra ? 1 : 0);
            var cell = new Rect(bounds.x(), y, bounds.width(), height);
            cells.add(cell);
            if (i + 1 < count) y = Math.addExact(cell.bottom(), gap);
        }
        return List.copyOf(cells);
    }
}
