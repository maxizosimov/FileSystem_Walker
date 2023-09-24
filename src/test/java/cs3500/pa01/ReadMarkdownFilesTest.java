package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;


class ReadMarkdownFilesTest {

  // Create a temporary directory for Github testing
  @TempDir
  File tempDir;

  /**
   * Test that makes sure that there is no other files than .md added to our directories
   *
   * @throws IOException makes sure there is a throw when we can't or shouldn't access certain files
   */
  @Test
  void treeWalkerDoesNotAddOtherThanMdFiles() throws IOException {

    File file = tempDir;
    // Make new output file in tempDir ending with "output.md"
    File outputFile = new File(tempDir, "output.md");
    ReadMarkdownFiles readMarkdownFiles = new ReadMarkdownFiles();

    // Creating temporary files, one with path ending in .md and other .txt for coming test
    Files.createTempFile(file.toPath(), "tempFile", ".md");
    Files.createTempFile(file.toPath(), "tempFile", ".txt");

    // Traversing through tree, file being the file we check
    // and outputFile the one we skip which was created to remedy header duplication bug
    readMarkdownFiles.treeWalker(file, outputFile);

    // Using getter to create a list of our files to test with
    ArrayList<File> files = readMarkdownFiles.getFiles();

    // Iterating through our list of temp made files
    // if we hit a file ending .txt --> False || hit .md ending of file --> True
    for (File f : files) {
      assertFalse(f.getName().endsWith(".txt"));
      assertTrue(f.getName().endsWith(".md"));
    }
  }
}