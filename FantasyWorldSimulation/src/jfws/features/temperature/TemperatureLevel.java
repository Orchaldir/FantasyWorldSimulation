package jfws.features.temperature;

import javafx.scene.paint.Color;
import jfws.features.AttributeLevel;
import jfws.features.AttributeLevelUtility;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TemperatureLevel implements AttributeLevel {
	VERY_COLD(Color.WHITE),
	COLD(Color.CYAN),
	COOL(Color.BLUE),
	TEMPERATE(Color.GREEN),
	WARM(Color.YELLOW),
	HOT(Color.ORANGE),
	VERY_HOT(Color.RED);

	public static final AttributeLevelUtility<TemperatureLevel> UTILITY = new AttributeLevelUtility<>(values());

	@Getter
	private final Color color;
}
