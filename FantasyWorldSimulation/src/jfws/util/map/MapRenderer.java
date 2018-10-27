package jfws.util.map;

import jfws.util.rendering.Renderer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MapRenderer<T> {

	private Renderer renderer;
	private ToCellMapper<T> toCellMapper;

	public void render() {
		Map2d<T> map = toCellMapper.getMap();
		double resolutionX = toCellMapper.getResolutionX();
		double resolutionY = toCellMapper.getResolutionY();

		for(int row = 0; row < map.getHeight(); row++) {
			double originY = toCellMapper.getCellOriginY(row);

			for(int column = 0; column < map.getWidth(); column++) {
				double originX = toCellMapper.getCellOriginX(column);

				renderer.renderRectangle(originX, originY, resolutionX, resolutionY);
			}
		}
	}
}
