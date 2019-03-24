package jfws.features.rainfall;

import javafx.scene.paint.Color;
import jfws.features.AttributeLevel;
import jfws.features.AttributeLevelUtility;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RainfallLevel implements AttributeLevel {
	VERY_WET(Color.BLUE),
	WET(Color.LIGHTBLUE),
	AVERAGE(Color.GREEN),
	DRY(Color.LIGHTGOLDENRODYELLOW),
	VERY_DRY(Color.GOLDENROD);

	public static final AttributeLevelUtility<RainfallLevel> UTILITY = new AttributeLevelUtility<>(values());

	@Getter
	private final Color color;
}
