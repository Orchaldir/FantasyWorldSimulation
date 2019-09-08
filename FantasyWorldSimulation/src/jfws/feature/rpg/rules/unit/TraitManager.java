package jfws.feature.rpg.rules.unit;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class TraitManager {

	private Map<String,Trait> traits = new HashMap<>();

	public void add(Trait trait) {
		if (traits.put(trait.getName(), trait) != null) {
			throw new IllegalArgumentException(String.format("Trait '%s' already exists!", trait.getName()));
		}

		log.info("Add trait '{}'.", trait.getName());
	}

	public Trait get(String name) {
		Trait trait = traits.get(name);

		if(trait == null) {
			throw new NoSuchElementException(String.format("Did not find trait '%s'!", name));
		}

		return trait;
	}

	public Collection<Trait> getAll() {
		List<Trait> sortedByName = traits.
				values().
				stream().
				sorted(Comparator.comparing(Trait::getName)).
				collect(Collectors.toList());

		return new ArrayList<>(sortedByName);
	}
}
