package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;


class WriteMarkdownFileTest {

  private WriteMarkdownFile wmf;

  // Creating temp directory for Github tests
  @TempDir
  Path tempDir;

  /**
   * Sets up temp files uses directory to complete the paths using resolve as well as adding
   * contents to the files in order to compare files in testing
   *
   * @throws IOException in case an I/O error occurs or the parent directory does not exist
   */
  @BeforeEach
  void setUp() throws IOException {
    // Create temporary files with some content
    Path file1 = Files.createFile(tempDir.resolve("arrays.md"));
    Files.writeString(file1, "This is arrays.md content");
    Path file2 = Files.createFile(tempDir.resolve("vectors.md"));
    Files.writeString(file2, "This is vectors.md content");

    // Adding temporary contents to a file in order to check if it can be read
    ArrayList<File> fileArrayList = new ArrayList<>();
    fileArrayList.add(file1.toFile());
    fileArrayList.add(file2.toFile());
    wmf = new WriteMarkdownFile(fileArrayList);
  }

  /**
   * Tests checking if it is possible to write to a non-existing Path/File in a directory
   */
  @Test
  void testWriteToFileInvalidPath() {
    // Creating a File object resolved against tempDir converting the path to a string
    // with an invalid path as it begins with //
    File invalidPathFile = new File(tempDir.resolve("//path/output.md").toString());
    assertThrows(IllegalArgumentException.class, () ->
        wmf.writeToFile(invalidPathFile, "Content trying to be written to file"));
  }

  /**
   * Tests to check the branches if an excluded file and this file are equal
   *
   * @throws IOException to throw the correct exception if the dir or file needs it
   */
  @Test
  void testGetRightContent_excludedFile() throws IOException {
    // temp directory created
    Path tempDir = Files.createTempDirectory("tempDir");

    // Two temp files in the temp directory in order to check if the names are equal
    File tempFile1 = Files.createFile(tempDir.resolve("file1.md")).toFile();
    File tempFile2 = Files.createFile(tempDir.resolve("excluded.md")).toFile();

    // Fill temp files with string to show that one is excluded
    Files.writeString(tempFile1.toPath(), "Content of file1");
    Files.writeString(tempFile2.toPath(), "Content of excluded file");

    // Added the temp files to a list to use with WriteMarkdownFile in order to create instance
    ArrayList<File> files = new ArrayList<>();
    files.add(tempFile1);
    files.add(tempFile2);

    // Create a .md instance
    WriteMarkdownFile wmf = new WriteMarkdownFile(files);

    // Call getRightContent with outputFile having the same name as tempFile2
    File outputFile = new File(tempDir.resolve("excluded.md").toString());
    String result = wmf.getRightContent(outputFile);

    // Check that the two temp files do not contain the same content as private fileReader method
    // is called through getRightContent and hence we check the excluded filename branches
    assertFalse(result.contains("Content of file1"));
    assertFalse(result.contains("Content of excluded file"));
  }
}