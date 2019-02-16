package jfws.util.rendering;

import javafx.scene.paint.Color;

public interface Renderer {

	void clear(double x, double y, double width, double height);

	void setScale(double scale);

	void setColor(Color color);

	void renderRectangle(double x, double y, double width, double height);

}
