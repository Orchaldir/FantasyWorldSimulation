package jfws.util.map;

import jfws.util.rendering.ColorSelector;
import jfws.util.rendering.Renderer;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class MapRenderer {

	private Renderer renderer;
	private double worldToScreenFactor;
	@Setter
	private double borderBetweenCells;

	public double convertToWorld(double screen) {
		return screen / worldToScreenFactor;
	}

	public <T> void render(ToCellMapper<T> toCellMapper, ColorSelector<T> colorSelector) {
		log.info("render()");

		try {
			tryRender(toCellMapper, colorSelector);
		} catch (OutsideMapException e) {
			log.error("render(): Tried to render cell outside the map! x={} y={}", e.getX(), e.getY());
		}
	}

	private <T> void tryRender(ToCellMapper<T> toCellMapper, ColorSelector<T> colorSelector) throws OutsideMapException {
		CellMap2d<T> map = toCellMapper.getMap();
		double resolutionX = toCellMapper.getResolutionX() - borderBetweenCells;
		double resolutionY = toCellMapper.getResolutionY() - borderBetweenCells;

		log.info("tryRender(): clear");
		renderer.clear(0, 0, toCellMapper.getWidth(), toCellMapper.getHeight());
		log.info("tryRender(): scale");
		renderer.setScale(worldToScreenFactor);

		for(int row = 0; row < map.getHeight(); row++) {
			double originY = toCellMapper.getCellOriginY(row);

			log.info("tryRender(): row={}", row);

			for(int column = 0; column < map.getWidth(); column++) {
				double originX = toCellMapper.getCellOriginX(column);

				renderer.setFillColor(colorSelector.select(map.getCell(column, row)));
				renderer.renderRectangle(originX, originY, resolutionX, resolutionY);
			}
		}

		log.info("tryRender(): revert scale");

		renderer.setScale(1.0 / worldToScreenFactor);
	}
}
