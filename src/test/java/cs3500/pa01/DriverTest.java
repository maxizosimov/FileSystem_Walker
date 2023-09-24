package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Testing driver class
 */
public class DriverTest {

  // Creating temp directories for Github testing
  @TempDir
  Path temporaryFolder;

  @Test
  void testMain() throws IOException {
    // Create temporary files and directories
    Path tempSubdirectory = Files.createDirectory(temporaryFolder.resolve("subfolder"));
    Path tempMdFile1 = Files.createFile(tempSubdirectory.resolve("temp1.md"));
    Path tempMdFile2 = Files.createFile(tempSubdirectory.resolve("temp2.md"));

    // Adding content to the temp files to enable checking actual and expected output
    // when methods such as getRightContent are used
    Files.writeString(tempMdFile1, "# Heading 1\nSome text [[important phrase 1]]");
    Files.writeString(tempMdFile2, "# Heading 2\nSome other text [[important phrase 2]]");

    // Convert paths to strings for the main method arguments
    String rootDir = temporaryFolder.toString();
    // first adding "output.md" to end of path by resolving string against temporaryFolder path
    // and then strings it to use in simulating cmdline arguments in String[] args
    String outputFile = temporaryFolder.resolve("output.md").toString();

    // main method takes a String[] args
    String[] args = {rootDir, "filename", outputFile};

    // Making sure we do not throw in the main as long as our cmdline arguments are correct
    // as opposed to throwing when they are wrong - gave same result in Jacoco coverage
    assertDoesNotThrow(() -> Driver.main(args));

    // Verify content of output file using WriteMarkdownFile
    ArrayList<File> sortedFiles = new ArrayList<>();
    sortedFiles.add(tempMdFile1.toFile());
    sortedFiles.add(tempMdFile2.toFile());

    WriteMarkdownFile wmf = new WriteMarkdownFile(sortedFiles);
    String outputContent = wmf.getRightContent(new File(outputFile));

    String expectedOutput = """
        # Heading 1
        - important phrase 1

        # Heading 2
        - important phrase 2
        """;

    // Checking that our getRightContent correctly modifies our sortedFiles
    // to produce desired content (only headers and important phrases)
    assertEquals(expectedOutput, outputContent);
  }
}
