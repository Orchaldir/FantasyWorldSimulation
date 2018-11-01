package jfws.util.map;

import jfws.util.rendering.ColorSelector;
import jfws.util.rendering.Renderer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class MapRenderer<T> {

	@Getter
	@Setter
	private ColorSelector<T> colorSelector;
	private Renderer renderer;
	private ToCellMapper<T> toCellMapper;
	private double borderBetweenCells;

	public void render() {
		try {
			tryRender();
		} catch (OutsideMapException e) {
			log.error("render(): Tried to render cell outside the map! x={} y={}", e.getX(), e.getY());
		}
	}

	private void tryRender() throws OutsideMapException {
		Map2d<T> map = toCellMapper.getMap();
		double resolutionX = toCellMapper.getResolutionX() - borderBetweenCells;
		double resolutionY = toCellMapper.getResolutionY() - borderBetweenCells;

		for(int row = 0; row < map.getHeight(); row++) {
			double originY = toCellMapper.getCellOriginY(row);

			for(int column = 0; column < map.getWidth(); column++) {
				double originX = toCellMapper.getCellOriginX(column);

				renderer.setFillColor(colorSelector.select(map.getCell(column, row)));
				renderer.renderRectangle(originX, originY, resolutionX, resolutionY);
			}
		}
	}
}
