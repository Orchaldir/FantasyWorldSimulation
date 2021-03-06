package jfws.feature.world.attribute;

import javafx.scene.paint.Color;
import jfws.util.rendering.ColorSelector;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClosestLevelColorSelector<T extends AttributeCell> implements ColorSelector<T> {

	private final String name;
	private final AttributeLevelUtility utility;
	private final int index;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Color select(T cell) {
		double temperature = cell.getAttribute(index);

		return utility.getClosestColor(temperature);
	}
}