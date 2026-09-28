package local.minecraftfrontend;

import local.minecraftfrontend.geometry.*;
import local.minecraftfrontend.primitives.Row;
import local.minecraftfrontend.primitives.Column;
import local.minecraftfrontend.primitives.Grid;
import local.minecraftfrontend.primitives.Stack;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Pure Ciel geometry contracts. Native widgets require an owo-loaded Minecraft client. */
class PrimitivesTest {
    @Test void fillClampsAndRejectsNonfiniteFractions() {
        assertEquals(0, Fill.size(10, -1));
        assertEquals(10, Fill.size(10, 2));
        assertEquals(5, Fill.size(10, 0.5));
        assertEquals(0, Fill.size(10, Double.NaN));
        assertEquals(0, Fill.size(10, Double.POSITIVE_INFINITY));
        assertEquals(0, Fill.size(-1, 1));
    }
    @Test void rectangleEdgesAreExclusive() {
        var rect = new Rect(2, 3, 4, 5);
        assertTrue(rect.contains(new Point(2, 3)));
        assertFalse(rect.contains(new Point(6, 3)));
        assertFalse(rect.contains(new Point(2, 8)));
        assertTrue(rect.intersection(new Rect(6, 3, 2, 2)).isEmpty());
        assertEquals(new Rect(4, 4, 2, 2), rect.intersection(new Rect(4, 4, 3, 2)).orElseThrow());
    }
    @Test void invalidGeometryAndOverflowAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Size(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> Insets.all(-1));
        assertThrows(IllegalArgumentException.class, () -> new Circle(new Point(0, 0), -1));
        assertThrows(ArithmeticException.class, () -> new Rect(Integer.MAX_VALUE, 0, 1, 0));
        assertThrows(ArithmeticException.class, () -> new Point(Integer.MAX_VALUE, 0).translate(1, 0));
    }
    @Test void insetsKeepTheirOriginalTopRightBottomLeftOrder() {
        assertEquals(new Rect(4, 1, 14, 16), new Rect(0, 0, 20, 20).inset(new Insets(1, 2, 3, 4)));
        assertThrows(IllegalArgumentException.class, () -> new Rect(0, 0, 2, 2).inset(Insets.all(2)));
    }
    @Test void circleAndArcUseClockwiseScreenAngles() {
        var circle = new Circle(new Point(10, 10), 5);
        assertEquals(new Rect(5, 5, 11, 11), circle.bounds());
        assertEquals(new Point(15, 10), circle.at(0));
        assertEquals(new Point(10, 15), circle.at(90));
        assertEquals(5, Circle.halfSpan(5, 0));
        assertEquals(0, Circle.halfSpan(5, 6));
        assertEquals(new Point(10, 5), new Arc(circle, 0, -90).at(1));
        assertThrows(IllegalArgumentException.class, () -> new Arc(circle, 0, 361));
    }
    @Test void lineInterpolationIsBounded() {
        var line = new Line(new Point(0, 0), new Point(6, 8));
        assertEquals(10, line.length());
        assertEquals(new Point(3, 4), line.at(0.5));
        assertThrows(IllegalArgumentException.class, () -> line.at(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> line.at(2));
    }
    @Test void rowsDistributeRemaindersWithoutEscapingBounds() {
        var cells = Row.cells(new Rect(0, 0, 11, 4), 3, 1);
        assertEquals(List.of(new Rect(0, 0, 3, 4), new Rect(4, 0, 3, 4), new Rect(8, 0, 3, 4)), cells);
        assertEquals(List.of(new Rect(0, 0, 3, 1), new Rect(4, 0, 2, 1)), Row.cells(new Rect(0, 0, 6, 1), 2, 1));
        assertThrows(UnsupportedOperationException.class, () -> cells.clear());
    }
    @Test void columnsAndPartialGridRowsKeepAlignment() {
        assertEquals(List.of(new Rect(0, 0, 4, 3), new Rect(0, 4, 4, 2)), Column.cells(new Rect(0, 0, 4, 6), 2, 1));
        var cells = Grid.cells(new Rect(0, 0, 10, 10), 3, 2, 2, 2);
        assertEquals(List.of(new Rect(0, 0, 4, 4), new Rect(6, 0, 4, 4), new Rect(0, 6, 4, 4)), cells);
    }
    @Test void emptyAndInvalidLayoutRequestsAreExplicit() {
        var rect = new Rect(0, 0, 2, 2);
        assertTrue(Row.cells(rect, 0, 0).isEmpty());
        assertThrows(IllegalArgumentException.class, () -> Row.cells(rect, 3, 2));
        assertThrows(IllegalArgumentException.class, () -> Column.cells(rect, -1, 0));
        assertThrows(IllegalArgumentException.class, () -> Grid.cells(rect, 1, 0, 0, 0));
    }
    @Test void stackBoundsAreImmutableAndShared() {
        var rect = new Rect(1, 2, 3, 4);
        assertEquals(List.of(rect, rect), Stack.layers(rect, 2));
        assertThrows(UnsupportedOperationException.class, () -> Stack.layers(rect, 2).set(0, rect));
        assertThrows(IllegalArgumentException.class, () -> Stack.layers(rect, -1));
    }
}
