package jfws.util.io;

import io.github.glytching.junit.extension.folder.TemporaryFolder;
import io.github.glytching.junit.extension.folder.TemporaryFolderExtension;
import org.hamcrest.core.StringEndsWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

@ExtendWith(TemporaryFolderExtension.class)
class ApacheFileUtilsTest {

	public static final String TEST_STRING = "test string\nnew line";

	private TemporaryFolder temporaryFolder;
	private ApacheFileUtils apacheFileUtils;

	@BeforeEach
	public void setUp(TemporaryFolder temporaryFolder) {
		this.temporaryFolder = temporaryFolder;
		apacheFileUtils = new ApacheFileUtils();
	}

	// file

	@Test
	public void testReadAndWrite() throws IOException {
		File file = temporaryFolder.createFile("ApacheFileUtilsTest.txt");

		apacheFileUtils.writeWholeFile(file, TEST_STRING);
		assertThat(apacheFileUtils.readWholeFile(file), is(equalTo(TEST_STRING)));
	}

	// getAbsolutePath

	@Test
	public void testGetAbsolutePath() {
		assertThat(apacheFileUtils.getAbsolutePath("test/"), StringEndsWith.endsWith("test"));
	}

}