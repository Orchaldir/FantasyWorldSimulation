package jfws.util.ecs.component;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class ComponentUtil {

	static Set<Integer> getSharedIds(List<ComponentStorage> storages) {
		Set<Integer> sharedIds = new HashSet<>();

		for (ComponentStorage storage : storages) {
			Set<Integer> ids = storage.getIds();

			if(sharedIds.isEmpty()) {
				sharedIds.addAll(ids);
			}
			else {
				sharedIds.retainAll(ids);

				if(sharedIds.isEmpty()) {
					return sharedIds;
				}
			}
		}

		return sharedIds;
	}

	private ComponentUtil() {}

}
