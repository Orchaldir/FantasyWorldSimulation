package jfws.feature.world.attribute.magic;

import javafx.scene.paint.Color;
import jfws.feature.world.attribute.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ManaLevel implements AttributeLevel {
	NO_MANA(Color.DARKRED),
	LOW_MANA(Color.BLACK),
	AVERAGE_MANA(Color.GRAY),
	HIGH_MANA(Color.WHITE),
	VERY_HIGH_MANA(Color.YELLOW);

	@Getter
	private final Color color;

	public static final AttributeLevelUtility<ManaLevel> UTILITY = new AttributeLevelUtility<>(values());
	public static final String ATTRIBUTE_NAME = "ManaLevel";

	@Override
	public double getValue() {
		return UTILITY.getValue(this);
	}

	public static <T extends AttributeCell>
	AttributeColorSelector<T> createColorSelector(int index) {
		return new AttributeColorSelector<>(ATTRIBUTE_NAME, UTILITY, index);
	}

	public static <T extends AttributeCell>
	ClosestLevelColorSelector<T> createClosestLevelColorSelector(int index) {
		return new ClosestLevelColorSelector<>(ATTRIBUTE_NAME, UTILITY, index);
	}
}
