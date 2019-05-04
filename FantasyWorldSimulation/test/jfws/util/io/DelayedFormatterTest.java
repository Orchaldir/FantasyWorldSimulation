package jfws.util.io;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class DelayedFormatterTest {

	@Test
	public void testTwoAttributes() {
		assertThat(DelayedFormatter.format("Test with %d %s", 2, "attributes").toString(), is(equalTo("Test with 2 attributes")));
	}

	@Test
	public void testFormattedFloat() {
		assertThat(DelayedFormatter.format("Float %.3f", 1.234567).toString(), is(equalTo("Float 1,235")));
	}
}