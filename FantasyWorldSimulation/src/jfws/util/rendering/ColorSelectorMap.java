package jfws.util.rendering;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorSelectorMap<T> {

	private final Map<String,ColorSelector<T>> map = new HashMap<>();
	@Getter
	private final ColorSelector<T> defaultColorSelector;

	public ColorSelectorMap(ColorSelector<T> defaultColorSelector) {
		this.defaultColorSelector = defaultColorSelector;
		add(defaultColorSelector);
	}

	public void add(ColorSelector<T> colorSelector) {
		map.put(colorSelector.getName(), colorSelector);
	}

	public ColorSelector<T> get(String name) {
		return map.getOrDefault(name, defaultColorSelector);
	}

	public List<String> getNames() {
		return new ArrayList<>(map.keySet());
	}
}
