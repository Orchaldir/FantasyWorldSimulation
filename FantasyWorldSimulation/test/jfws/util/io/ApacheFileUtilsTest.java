package jfws.util.io;

import org.hamcrest.core.StringEndsWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

class ApacheFileUtilsTest {

	public static final String TEST_STRING = "test string\nnew line";

	private ApacheFileUtils apacheFileUtils;

	@BeforeEach
	public void setUp() {
		apacheFileUtils = new ApacheFileUtils();
	}

	// file

	@Test
	public void testReadAndWrite() throws IOException {
		File file = new File("target/ApacheFileUtilsTest.txt");

		apacheFileUtils.writeWholeFile(file, TEST_STRING);
		assertThat(apacheFileUtils.readWholeFile(file), is(equalTo(TEST_STRING)));
	}

	// getAbsolutePath

	@Test
	public void testGetAbsolutePath() {
		assertThat(apacheFileUtils.getAbsolutePath("test/"), StringEndsWith.endsWith("test"));
	}

}