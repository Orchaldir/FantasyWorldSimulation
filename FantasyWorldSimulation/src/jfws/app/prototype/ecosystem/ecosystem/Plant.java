package jfws.app.prototype.ecosystem.ecosystem;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Plant {
	private final String name;
	private final double growthRate;
	private final double fitness;
}
