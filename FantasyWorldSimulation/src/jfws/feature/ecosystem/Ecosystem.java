package jfws.feature.ecosystem;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

@AllArgsConstructor
public class Ecosystem {
	@Getter
	private final double area;
	private final Map<Plant, Population> populations = new HashMap<>();

	public void addPopulation(Population population) {
		populations.put(population.getPlant(), population);
	}

	public Collection<Population> getPopulations() {
		return populations.values();
	}
}
