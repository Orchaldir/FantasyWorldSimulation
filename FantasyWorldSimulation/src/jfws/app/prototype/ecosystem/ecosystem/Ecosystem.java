package jfws.app.prototype.ecosystem.ecosystem;

import lombok.*;

import java.util.*;

@AllArgsConstructor
@Data
public class Ecosystem {
	private final double area;

	@Setter(AccessLevel.NONE)
	private final Map<Plant, Population> populations = new HashMap<>();

	// attributes
	private double mana;
	private double rainfall;
	private double temperature;

	public void addPopulation(Population population) {
		populations.put(population.getPlant(), population);
	}

	public Collection<Population> getPopulations() {
		return populations.values();
	}
}
