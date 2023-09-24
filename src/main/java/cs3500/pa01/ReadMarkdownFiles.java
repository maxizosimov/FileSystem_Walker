package cs3500.pa01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

/**
 * Represents the traversing of a filesystem to be able to read markdown files
 */
public class ReadMarkdownFiles {

  ArrayList<File> fileList = new ArrayList<>();

  /**
   * Constructor
   */
  public ReadMarkdownFiles() {

  }

  /**
   * Traverses a system of files (directories) to find files ending in .md
   *
   * @param file is the file we are checking
   * @param outputFile is the file we have created and written the output to (so we skip it)
   * @throws IOException is the correct exception when we can't/shouldn't access certain files
   */
  public void treeWalker(File file, File outputFile) throws IOException {

    Path start = Paths.get(file.toURI());

    try {
      Files.walkFileTree(start, new SimpleFileVisitor<>() {

        /**
         * Makes sure it is a regular file and a markdown file (ends with .md)
         */
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
          if (file.toString().endsWith(".md") && Files.isRegularFile(file)
              && !file.toFile().equals(outputFile)) {
            fileList.add(file.toFile());
          }
          return FileVisitResult.CONTINUE;
        }
      });
    } catch (NoSuchFileException e) {
      // returns the name of the directory of file in a error message
      throw new FileNotFoundException("The directory or file does not exist: " + e.getFile());
    }
  }

  /**
   * Getter to get .md files
   */
  public ArrayList<File> getFiles() {
    return fileList;
  }
}
