package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for SortingMarkdownFile class
 */
public class SortingMarkdownFileTest {

  private ArrayList<File> files;

  // For creating temp directories for Github testing
  @TempDir
  Path tempDir;

  /**
   * Adds all the files to a list in order to test
   */
  @BeforeEach
  public void setUp() throws IOException {
    files = new ArrayList<>();
    Files.createFile(tempDir.resolve("arrays.md"));
    Files.createFile(tempDir.resolve("vectors.md"));
    Files.createFile(tempDir.resolve("test.md"));
    Files.createFile(tempDir.resolve("test2.md"));

    files.add(tempDir.resolve("arrays.md").toFile());
    files.add(tempDir.resolve("vectors.md").toFile());
    files.add(tempDir.resolve("test.md").toFile());
    files.add(tempDir.resolve("test2.md").toFile());
  }

  /**
   * To test the ordering depending on a given ordering flag such as filename
   *
   * @throws IOException to throw the correct type of exception if orderingFlag would be wrong
   */
  @Test
  public void testOrderingFlag_filename() throws IOException {
    SortingMarkdownFile smf = new SortingMarkdownFile(files);
    ArrayList<File> sortedFiles = smf.orderingFlag("filename");

    assertEquals("arrays.md", sortedFiles.get(0).getName());
    assertEquals("test.md", sortedFiles.get(1).getName());
    assertEquals("test2.md", sortedFiles.get(2).getName());
    assertEquals("vectors.md", sortedFiles.get(3).getName());
  }

  /**
   * Testing IOException for ordering flags (created date and last modified date methods)
   *
   * @throws IOException in order to throw if rmd.treeWalker get a wrong argument
   */
  @Test
  public void testCorrectException() throws IOException {

    // Creating a temp dir and then making it file to make more readable
    // as it allows to skip using new File(tempDir.toFile().toURI()) in on line
    String mainDir = tempDir.toString();
    File dirArg = new File(mainDir);

    ReadMarkdownFiles rmd = new ReadMarkdownFiles();

    // dirArg being the file we are checking and the "output" the file we exclude
    rmd.treeWalker(dirArg, new File("output"));

    // Uses a getter to make an ArrayList of the files in the tree we just traversed above
    ArrayList<File> files = rmd.getFiles();
    System.out.println("FILE " + files);

    // Using the SortingMarkdownFile to order our ArrayList of files we just created above
    SortingMarkdownFile smf = new SortingMarkdownFile(files);
    System.out.println("ORDERED" + smf);

    // Because my methods wanted to wrap my IOException in a runtime exception
    // I had to handle it by first checking if it is a IOException otherwise RuntimeException
    try {
      smf.orderingFlag("created date");
    } catch (IOException e) {
      if (e.getCause() instanceof IOException) {
        System.out.println("Expected IOException encountered");
      } else {
        fail("Unexpected RuntimeException encountered");
      }
    }

    // Because my methods wanted to wrap my IOException in a runtime exception
    // I had to handle it by first checking if it is a IOException otherwise RuntimeException
    try {
      smf.orderingFlag("last modified date");
    } catch (IOException e) {
      if (e.getCause() instanceof IOException) {
        System.out.println("Expected IOException encountered");
      } else {
        fail("Unexpected RuntimeException encountered");
      }
    }
  }

  /**
   * Tests that the correct throw happens if the orderingFlag cmdline argument is
   * not one of the desired three ("filename", "last modified date" or "created date")
   */
  @Test
  public void testInvalidOrderingFlag() {
    // making a local variable that sorts the files given (from constructor)
    SortingMarkdownFile smf = new SortingMarkdownFile(files);

    // checking if there is an illegal argument for ordering flag - then we should throw correctly
    assertThrows(IllegalArgumentException.class, () -> smf.orderingFlag(
        "filename, created date or last modified date are the "
            + "only valid arguments for ordering"));
  }
}