package jfws.util.ecs.component;

import java.util.*;

public class ComponentMap<T> implements ComponentStorage<T> {

	private Map<Integer,T> map = new HashMap<>();

	@Override
	public void add(int id, T component) {
		map.put(id, component);
	}

	@Override
	public Optional<T> get(int id) {
		return Optional.ofNullable(map.get(id));
	}

	@Override
	public Set<Integer> getIds() {
		return map.keySet();
	}

	@Override
	public void remove(int id) {
		map.remove(id);
	}
}
