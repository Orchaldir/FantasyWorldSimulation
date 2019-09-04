package jfws.util.ecs.component;

import java.util.Optional;
import java.util.Set;

public interface ComponentStorage<T> {

	void add(int entityId, T component);

	Optional<T> get(int entityId);

	Set<Integer> getIds();

	void remove(int entityId);

}
