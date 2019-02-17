package jfws.util.rendering;

import javafx.scene.paint.Color;
import jfws.util.math.random.RandomNumberGenerator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RandomColorSelector<T> implements ColorSelector<T> {

	public static final int MAX_VALUE = 255;

	RandomNumberGenerator generator;

	@Override
	public String getName() {
		return "Random";
	}

	@Override
	public Color select(T parameter) {
		int red = generator.getInteger(MAX_VALUE);
		int green = generator.getInteger(MAX_VALUE);
		int blue = generator.getInteger(MAX_VALUE);

		return Color.rgb(red, green, blue);
	}

	@Override
	public void reset() {
		generator.restart();
	}
}
