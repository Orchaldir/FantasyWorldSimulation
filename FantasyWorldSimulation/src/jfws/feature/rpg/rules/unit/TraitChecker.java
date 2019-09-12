package jfws.feature.rpg.rules.unit;

import jfws.util.math.random.RandomNumberGenerator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TraitChecker {

	private final RandomNumberGenerator generator;
	private final int dieSides;
	private final int sizeOfDegree;

	public int check(int trait, int difficulty) {
		final int positiveDie = generator.getInteger(dieSides);
		final int negativeDie = generator.getInteger(dieSides);
		final int random_modifier = positiveDie - negativeDie;
		final int diff = trait - difficulty + random_modifier;

		if(diff > 0) {
			return (int) Math.ceil(diff / (double) sizeOfDegree);
		}
		else if(diff < 0) {
			return (int) Math.floor(diff / (double) sizeOfDegree);
		}

		return 0;
	}
}
