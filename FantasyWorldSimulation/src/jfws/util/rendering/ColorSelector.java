package jfws.util.rendering;

import javafx.scene.paint.Color;

public interface ColorSelector<T> {

	Color select(T parameter);
}
