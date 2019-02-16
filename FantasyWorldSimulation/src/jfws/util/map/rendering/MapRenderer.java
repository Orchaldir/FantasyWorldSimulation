package jfws.util.map.rendering;

import jfws.util.map.CellMap2d;
import jfws.util.map.ToCellMapper;
import jfws.util.rendering.ColorSelector;
import jfws.util.rendering.Renderer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class MapRenderer {

	private Renderer renderer;
	private double worldToScreenFactor;

	@Getter
	@Setter
	private double borderBetweenCells;

	public double convertToWorld(double screen) {
		return screen / worldToScreenFactor;
	}

	public <T> void render(ToCellMapper<T> toCellMapper, ColorSelector<T> colorSelector) {
		log.info("render()");

		CellMap2d<T> map = toCellMapper.getMap();
		double resolutionX = toCellMapper.getResolutionX() - borderBetweenCells;
		double resolutionY = toCellMapper.getResolutionY() - borderBetweenCells;

		log.info("render(): clear");
		renderer.clear(0, 0, toCellMapper.getWidth(), toCellMapper.getHeight());
		renderer.setScale(worldToScreenFactor);

		for(int row = 0; row < map.getHeight(); row++) {
			double originY = toCellMapper.getCellOriginY(row);

			for(int column = 0; column < map.getWidth(); column++) {
				double originX = toCellMapper.getCellOriginX(column);

				renderer.setColor(colorSelector.select(map.getCell(column, row)));
				renderer.renderRectangle(originX, originY, resolutionX, resolutionY);
			}
		}

		renderer.setScale(1.0 / worldToScreenFactor);

		log.info("render(): Finished.");
	}
}
