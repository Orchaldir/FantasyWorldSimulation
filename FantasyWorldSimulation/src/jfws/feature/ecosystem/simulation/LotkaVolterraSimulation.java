package jfws.feature.ecosystem.simulation;

import jfws.feature.ecosystem.Ecosystem;
import jfws.feature.ecosystem.Population;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static jfws.util.io.DelayedFormatter.format;

// See https://en.wikipedia.org/wiki/Competitive_Lotka–Volterra_equations

@Slf4j
public class LotkaVolterraSimulation implements EcosystemSimulation {
	@Override
	public void update(Ecosystem ecosystem) {

		Collection<Population> populations = ecosystem.getPopulations();
		List<Double> growthList = new ArrayList<>(populations.size());

		double carryingCapacity = ecosystem.getArea();
		double currentCapacity = calculateCapacity(populations);

		log.info("update(): populations={} capacity: {}", populations.size(), format("%.1f/%.1f", currentCapacity, carryingCapacity));

		for (Population population : populations) {
			double growthRate = population.getPlant().getGrowthRate();
			double growth = growthRate * population.getArea() * ( 1.0 - currentCapacity / carryingCapacity);

			log.info("update(): \"{}\": area={} growth={}", population.getPlant().getName(),
					format("%.1f", population.getArea()), format("%.1f", growth));

			growthList.add(growth);
		}

		int i = 0;
		for (Population population : populations) {
			population.setArea(population.getArea() + growthList.get(i++));
		}
	}

	private double calculateCapacity(Collection<Population> populations) {
		double currentCapacity = 0;
		double interactionFactor = 1.0;

		for (Population p : populations) {
			currentCapacity += interactionFactor * p.getArea();
		}

		return currentCapacity;
	}
}
