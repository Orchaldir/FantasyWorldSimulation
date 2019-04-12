package jfws.feature.world.attribute.rainfall;

import javafx.scene.paint.Color;
import jfws.feature.world.attribute.AttributeCell;
import jfws.feature.world.attribute.AttributeColorSelector;
import jfws.feature.world.attribute.AttributeLevel;
import jfws.feature.world.attribute.AttributeLevelUtility;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RainfallLevel implements AttributeLevel {
	VERY_WET(Color.BLUE),
	WET(Color.LIGHTBLUE),
	AVERAGE(Color.GREEN),
	DRY(Color.LIGHTGOLDENRODYELLOW),
	VERY_DRY(Color.GOLDENROD);

	@Getter
	private final Color color;

	public static final AttributeLevelUtility<RainfallLevel> UTILITY = new AttributeLevelUtility<>(values());
	public static final String ATTRIBUTE_NAME = "Rainfall";

	public static <T extends AttributeCell>
	AttributeColorSelector<T> createColorSelector(int index) {
		return new AttributeColorSelector<>(ATTRIBUTE_NAME, UTILITY, index);
	}
}
