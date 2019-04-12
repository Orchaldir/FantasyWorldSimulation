package jfws.feature.world.attribute.magic;

import javafx.scene.paint.Color;
import jfws.feature.world.attribute.AttributeCell;
import jfws.feature.world.attribute.AttributeColorSelector;
import jfws.feature.world.attribute.AttributeLevel;
import jfws.feature.world.attribute.AttributeLevelUtility;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ManaLevel implements AttributeLevel {
	NO_MANA(Color.BLACK),
	LOW_MANA(Color.DARKGRAY),
	AVERAGE_MANA(Color.LIGHTGRAY),
	HIGH_MANA(Color.WHITE),
	VERY_HIGH_MANA(Color.YELLOW);

	@Getter
	private final Color color;

	public static final AttributeLevelUtility<ManaLevel> UTILITY = new AttributeLevelUtility<>(values());
	public static final String ATTRIBUTE_NAME = "ManaLevel";

	public static <T extends AttributeCell>
	AttributeColorSelector<T> createColorSelector(int index) {
		return new AttributeColorSelector<>(ATTRIBUTE_NAME, UTILITY, index);
	}
}
