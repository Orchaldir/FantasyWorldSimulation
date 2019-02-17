package jfws.util.rendering;

import javafx.scene.paint.Color;
import jfws.util.math.random.RandomNumberGenerator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RandomColorSelector<T> implements ColorSelector<T> {

	RandomNumberGenerator generator;

	@Override
	public String getName() {
		return "Random";
	}

	@Override
	public Color select(T parameter) {
		return null;
	}
}
