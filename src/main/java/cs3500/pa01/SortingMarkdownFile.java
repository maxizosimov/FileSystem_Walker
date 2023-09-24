package cs3500.pa01;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Represents ordering of .md files
 */
public class SortingMarkdownFile {

  private final ArrayList<File> files;

  public SortingMarkdownFile(ArrayList<File> files) {

    this.files = files;
  }

  /**
   * Determines which type of ordering of files we want to do
   *
   * @param orderType type of way to order file (by filename, creation date or last modified date)
   */
  public ArrayList<File> orderingFlag(String orderType) throws IOException {
    switch (orderType) { //more efficient than if statement for strings
      case "filename":
        files.sort(Comparator.comparing(File::getName));
        break;
      case "created date":
        creationDateOrder();
        break;
      case "last modified date":
        lastModifiedDate();
        //files.sort(Comparator.comparing(File::lastModified)); does not work
        break;
      default:
        throw new IllegalArgumentException("filename, created date or last modified date are the "
            + "only valid arguments for ordering");
    }
    for (File file : files) {
      System.out.println(Files.getLastModifiedTime(file.toPath()));
    }
    return files;
  }

  /**
   * Helper method to check the last modified date and compares the last modified times
   * among the files in the directory
   */
  private void lastModifiedDate() {
    this.files.sort((f, f1) -> {
      try {
        BasicFileAttributes file1 = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
        BasicFileAttributes file2 = Files.readAttributes(f1.toPath(), BasicFileAttributes.class);
        return file2.lastModifiedTime().compareTo(file1.lastModifiedTime());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  /**
   * Helper method to sort by creation date and compares the creation dates of files
   */
  private void creationDateOrder() { //redo this method
    this.files.sort((f1, f2) -> {
      try {
        BasicFileAttributes file1 = Files.readAttributes(f1.toPath(), BasicFileAttributes.class);
        BasicFileAttributes file2 = Files.readAttributes(f2.toPath(), BasicFileAttributes.class);
        return file1.creationTime().compareTo(file2.creationTime());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
