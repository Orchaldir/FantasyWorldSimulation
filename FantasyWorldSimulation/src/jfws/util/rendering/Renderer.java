package jfws.util.rendering;

import javafx.scene.paint.Color;

public interface Renderer {

	void setFillColor(Color color);

	void renderRectangle(double x, double y, double width, double height);
}
