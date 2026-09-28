package local.minecraftfrontend.primitives;

import local.minecraftfrontend.geometry.Rect;

import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.StackLayout;
import io.wispforest.owo.ui.core.Sizing;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Assigns identical bounds to ordered layers, such as an icon, mask and label. */
public final class Stack {
    private Stack() {}

    public static StackLayout create(Sizing width, Sizing height) {
        return Containers.stack(width, height);
    }

    /** Immutable back-to-front layer bounds. Drawing order remains the caller's responsibility. */
    public static List<Rect> layers(Rect bounds, int count) {
        Objects.requireNonNull(bounds, "bounds");
        if (count < 0) throw new IllegalArgumentException("Negative layer count");
        return Collections.nCopies(count, bounds);
    }
}
