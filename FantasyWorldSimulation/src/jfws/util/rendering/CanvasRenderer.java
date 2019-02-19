package jfws.util.rendering;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import jfws.util.math.geometry.Point2d;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class CanvasRenderer implements Renderer {

	private final GraphicsContext graphicsContext;

	@Override
	public void clear(double x, double y, double width, double height) {
		graphicsContext.clearRect(x, y, width, height);
	}

	@Override
	public void setScale(double scale) {
		graphicsContext.scale(scale, scale);
	}

	@Override
	public void setColor(Color color) {
		graphicsContext.setFill(color);
	}

	@Override
	public void renderRectangle(double x, double y, double width, double height) {
		graphicsContext.fillRect(x, y, width, height);
	}

	@Override
	public void renderPoint(Point2d point, double radius) {
		double size = radius * 2.0;
		graphicsContext.fillOval(point.getX() - radius, point.getY() - radius, size, size);
	}

	@Override
	public void renderPolygon(List<Point2d> points) {
		double[] xPoints = points.stream().mapToDouble(Point2d::getX).toArray();
		double[] yPoints = points.stream().mapToDouble(Point2d::getY).toArray();

		graphicsContext.fillPolygon(xPoints, yPoints, points.size());
	}
}
