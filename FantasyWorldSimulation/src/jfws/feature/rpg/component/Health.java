package jfws.feature.rpg.component;

import lombok.Getter;

@Getter
public class Health {
	public static final String TOUGHNESS_PENALTY_CAN_NOT_BE_NEGATIVE = "Toughness penalty can not be negative!";

	private int toughnessPenalty;

	public Health(int toughnessPenalty) {
		this.toughnessPenalty = toughnessPenalty;
		validate();
	}

	public void increaseToughnessPenalty() {
		toughnessPenalty++;
	}

	public void decreaseToughnessPenalty() {
		toughnessPenalty--;
		validate();
	}

	private void validate() {
		if(toughnessPenalty < 0) {
			throw new IllegalArgumentException(TOUGHNESS_PENALTY_CAN_NOT_BE_NEGATIVE);
		}
	}
}
