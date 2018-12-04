package jfws.util.map;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import jfws.util.rendering.ColorSelector;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageRenderer {

	public <T> WritableImage render(Map2d<T> map, ColorSelector<T> colorSelector) {
		CellMap2d<T> cellMap = map.getCellMap();
		WritableImage writableImage = new WritableImage(cellMap.getWidth(), cellMap.getHeight());

		try {
			tryRender(writableImage, cellMap, colorSelector);
		} catch (OutsideMapException e) {
			log.warn("render(): OutsideMapException");
		}

		return writableImage;
	}

	private  <T> void tryRender(WritableImage writableImage, CellMap2d<T> cellMap, ColorSelector<T> colorSelector) throws OutsideMapException {
		PixelWriter pixelWriter = writableImage.getPixelWriter();

		for(int y = 0; y < cellMap.getHeight(); y++) {
			for (int x = 0; x < cellMap.getWidth(); x++) {
				T cell = cellMap.getCell(x, y);
				Color color = colorSelector.select(cell);
				pixelWriter.setColor(x, y, color);
			}
		}
	}
}
