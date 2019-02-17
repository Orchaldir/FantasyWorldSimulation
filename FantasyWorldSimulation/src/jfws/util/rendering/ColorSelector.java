package jfws.util.rendering;

import javafx.scene.paint.Color;

public interface ColorSelector<T> {

	String getName();

	Color select(T parameter);

	default void reset() { }
}
