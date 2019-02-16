package jfws.util.rendering;

import javafx.scene.paint.Color;
import jfws.util.math.geometry.Point2d;

import java.util.List;

public interface Renderer {

	void clear(double x, double y, double width, double height);

	void setScale(double scale);

	void setColor(Color color);

	void renderRectangle(double x, double y, double width, double height);

	void renderPolygon(List<Point2d> points);
}
