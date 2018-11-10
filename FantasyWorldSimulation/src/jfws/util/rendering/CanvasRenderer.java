package jfws.util.rendering;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;

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
	public void setFillColor(Color color) {
		graphicsContext.setFill(color);
	}

	@Override
	public void renderRectangle(double x, double y, double width, double height) {
		graphicsContext.fillRect(x, y, width, height);
	}
}
