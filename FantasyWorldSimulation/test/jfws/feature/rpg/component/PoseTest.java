package jfws.feature.rpg.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class PoseTest {

	private static final Pose POSE = new Pose(1.1, 2.22, Math.toRadians(90.5));

	@BeforeEach
	public void setUp() {
		Locale.setDefault(Locale.ENGLISH);
	}

	@Test
	public void testTwoAttributes() {
		assertThat(POSE.toString(), is(equalTo("{x=1.100 y=2.220 orientation=90.5 deg}")));
	}

}