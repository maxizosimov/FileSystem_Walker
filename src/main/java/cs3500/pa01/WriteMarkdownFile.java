package cs3500.pa01;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents reading a file, extracting the desired contents and writing it to a file
 */
public class WriteMarkdownFile {

  private final ArrayList<File> fileArrayList;

  //Constructor
  public WriteMarkdownFile(ArrayList<File> fileArrayList) {

    this.fileArrayList = fileArrayList;
  }

  /**
   * Method to read the desired file in a directory
   */
  private String fileReader(String excludeFileName) {

    Scanner scanner; //null is a redundant initializer in this case
    StringBuilder sb = new StringBuilder();

    for (File file : fileArrayList) {
      if (file.getName().equals(excludeFileName)) {
        continue;
      }
      try {
        scanner = new Scanner(new FileInputStream(file));
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }
      while (scanner.hasNextLine()) {
        sb.append(scanner.nextLine()).append("\n"); // Read the line previously mentioned
      }
      scanner.close();
    }
    return sb.toString();
  }

  /**
   * Extracts the desired content inside the texts
   */
  public String getRightContent(File outputFile) {
    //to remove null value I had at the beginning of the first line
    String fileContent = fileReader(outputFile.getName()).trim();
    //splits the string into an array based on newline characters in order to loop through
    String[] lines = fileContent.split("\n");
    StringBuilder output = new StringBuilder();

    //loops through the list ([]) of split strings
    for (int i = 0; i < lines.length; i++) {
      //remove whitelines on the i th line, to help ensure there is not whitespaces if
      //at a start or part of a line to have regular behavior when checking for # or smth else
      String line = lines[i].trim();

      // checks if the string starts with a hash sign for headings
      if (line.startsWith("#")) {
        if (i != 0) {
          output.append("\n");
        }
        // simply creates a new line by appending \n
        output.append(line).append("\n");
      }

      // find the index of the first occurrence of "[[" and "]]" in the string, respectively.
      int startIndex = line.indexOf("[[");
      while (startIndex != -1) {
        int endIndex = line.indexOf("]]", startIndex);
        if (endIndex != -1) {
          //extracts a part of the string from startIndex to endIndex.
          String importantPhrase = line.substring(startIndex + 2, endIndex);
          output.append("- ").append(importantPhrase).append("\n");
          startIndex = line.indexOf("[[", endIndex);
        } else {
          break;
        }
      }
    }
    return output.toString();
  }

  /**
   * create an output file containing the desired parts of the searched .md files
   */
  public void writeToFile(File file, String contents) {

    Path filePath = file.toPath();
    Path dirPath = filePath.getParent();

    // creates a directory if it does not exist
    if (Files.notExists(dirPath)) {
      //Files.createDirectories(dirPath);
      throw new IllegalArgumentException("You provided " + dirPath + " which does not exist");
    }

    // Convert String to data for writing ("raw" byte data)
    byte[] data = contents.getBytes();

    // The path may not exist, or we may not have permissions to write to it,
    // in which case we need to handle that error (hence try-catch)
    try {
      // Built-in convenience method for writing data to a file
      // add `.md` to the file-path to write a Markdown file.
      Files.write(filePath, data);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}