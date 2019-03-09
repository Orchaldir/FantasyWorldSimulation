package jfws.util.map.rendering;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import jfws.util.map.CellMap2d;
import jfws.util.map.Map2d;
import jfws.util.rendering.ColorSelector;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageRenderer {

	public <T> WritableImage render(CellMap2d<T> cellMap, ColorSelector<T> colorSelector) {
		colorSelector.reset();

		WritableImage writableImage = new WritableImage(cellMap.getWidth(), cellMap.getHeight());
		PixelWriter pixelWriter = writableImage.getPixelWriter();

		for(int y = 0; y < cellMap.getHeight(); y++) {
			for (int x = 0; x < cellMap.getWidth(); x++) {
				T cell = cellMap.getCell(x, y);
				Color color = colorSelector.select(cell);
				pixelWriter.setColor(x, y, color);
			}
		}

		return writableImage;
	}
}
