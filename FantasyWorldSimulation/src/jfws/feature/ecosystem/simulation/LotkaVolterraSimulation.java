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

		log.info("update(): populations={} carryingCapacity={}", populations.size(), format("%.1f", carryingCapacity));

		for (Population population : populations) {
			double currentCapacity = calculateCapacity(populations, population);
			double growthRate = population.getPlant().getGrowthRate();
			double growth = growthRate * population.getArea() * ( 1.0 - currentCapacity / carryingCapacity);

			log.info("update(): \"{}\": area={} capacity={} growth={}", population.getPlant().getName(),
					format("%.1f", population.getArea()), format("%.1f", currentCapacity), format("%.1f", growth));

			growthList.add(growth);
		}

		int i = 0;
		for (Population population : populations) {
			population.setArea(population.getArea() + growthList.get(i++));
		}
	}

	private double calculateCapacity(Collection<Population> populations, Population population) {
		double currentCapacity = 0;

		for (Population p : populations) {
			if(p == population) {
				continue;
			}

			double interactionFactor = p.getPlant().getFitness() / population.getPlant().getFitness();
			currentCapacity += interactionFactor * p.getArea();
		}

		return currentCapacity;
	}
}
