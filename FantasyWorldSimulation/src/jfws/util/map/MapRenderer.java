package jfws.util.map;

import jfws.util.rendering.ColorSelector;
import jfws.util.rendering.Renderer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MapRenderer<T> {

	private ColorSelector<T> colorSelector;
	private Renderer renderer;
	private ToCellMapper<T> toCellMapper;

	public void render() throws OutsideMapException {
		Map2d<T> map = toCellMapper.getMap();
		double resolutionX = toCellMapper.getResolutionX();
		double resolutionY = toCellMapper.getResolutionY();

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
