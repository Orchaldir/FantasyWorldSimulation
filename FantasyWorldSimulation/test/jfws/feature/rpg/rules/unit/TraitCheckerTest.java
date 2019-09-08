package jfws.feature.rpg.rules.unit;

import jfws.util.math.random.RandomNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TraitCheckerTest {

	private static final int DIE_SIDES = 10;
	public static final int SIZE_OF_DEGREE = 5;

	private RandomNumberGenerator generator;
	private TraitChecker checker;

	@BeforeEach
	public void setUp() {
		generator = mock(RandomNumberGenerator.class);

		checker = new TraitChecker(generator, DIE_SIDES, SIZE_OF_DEGREE);
	}

	@Test
	public void testDraw() {
		assertCheck(2,1, 4, 5, 0);
	}

	@Test
	public void testOneDegreeOfSuccess() {
		assertCheck(1, 0, 0, 0, 1);
		assertCheck(1, 0, 1, 0, 1);
		assertCheck(1, 0, 2, 0, 1);
		assertCheck(1, 0, 3, 0, 1);
		assertCheck(1, 0, 4, 0, 1);
	}

	@Test
	public void testTwoDegreeOfSuccess() {
		assertCheck(2, 0, 4, 0, 2);
		assertCheck(3, 0, 4, 0, 2);
		assertCheck(4, 0, 4, 0, 2);
		assertCheck(5, 0, 4, 0, 2);
		assertCheck(6, 0, 4, 0, 2);
	}

	@Test
	public void testOneDegreeOfFailure() {
		assertCheck(0, 1, 0, 0, -1);
		assertCheck(0, 1, 0, 1, -1);
		assertCheck(0, 1, 0, 2, -1);
		assertCheck(0, 1, 0, 3, -1);
		assertCheck(0, 1, 0, 4, -1);
	}

	@Test
	public void testTwoDegreeOfFailure() {
		assertCheck(0, 2, 0, 4, -2);
		assertCheck(0, 3, 0, 4, -2);
		assertCheck(0, 4, 0, 4, -2);
		assertCheck(0, 5, 0, 4, -2);
		assertCheck(0, 6, 0, 4, -2);
	}

	private void assertCheck(int positive, int negative, int trait, int difficulty, int result) {
		when(generator.getInteger(DIE_SIDES)).
				thenReturn(positive).
				thenReturn(negative);

		assertThat(checker.check(trait, difficulty), is(result));
	}

}