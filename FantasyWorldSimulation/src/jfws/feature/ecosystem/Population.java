package jfws.feature.ecosystem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Population {
	private final Plant plant;
	@Setter
	private double area;
}
