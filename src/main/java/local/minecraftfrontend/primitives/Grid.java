package local.minecraftfrontend.primitives;

import local.minecraftfrontend.geometry.Rect;

import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.GridLayout;
import io.wispforest.owo.ui.core.Sizing;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Arranges cells in row-major order, using Row and Column sizing rules. */
public final class Grid {
    private Grid() {}

    /** Native grid; add children by row/column and use child margins for gaps. */
    public static GridLayout create(Sizing width, Sizing height, int rows, int columns) {
        if (rows < 1 || columns < 1) throw new IllegalArgumentException("Grid dimensions must be positive");
        return Containers.grid(width, height, rows, columns);
    }

    /**
     * columns is the maximum number of cells per row. A partial final row stays
     * left-aligned with the same column widths; it does not stretch its remaining cells.
     */
    public static List<Rect> cells(Rect bounds, int count, int columns, int columnGap, int rowGap) {
        Objects.requireNonNull(bounds, "bounds");
        if (count < 0 || columns <= 0 || columnGap < 0 || rowGap < 0)
            throw new IllegalArgumentException("Invalid grid dimensions");
        if (count == 0) return List.of();
        int actualColumns = Math.min(columns, count);
        int rows = 1 + (count - 1) / actualColumns;
        var cells = new ArrayList<Rect>(count);
        for (Rect row : Column.cells(bounds, rows, rowGap)) {
            for (Rect cell : Row.cells(row, actualColumns, columnGap)) {
                if (cells.size() == count) break;
                cells.add(cell);
            }
        }
        return List.copyOf(cells);
    }
}
