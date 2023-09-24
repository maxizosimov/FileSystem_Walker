package cs3500.pa01;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This is the main driver of this project.
 */
public class Driver {
  /**
   * Project entry point
   *
   * @param args - no command line args required
   */
  public static void main(String[] args) throws IOException {

    File dirArg = new File(args[0]);
    String orderArg = args[1];
    File outputArg = new File(args[2]);

    // walks the tree when given the directory and output target
    ReadMarkdownFiles rmd = new ReadMarkdownFiles();
    rmd.treeWalker(dirArg, outputArg);
    // sorts the files (and their contents) after a given cmdline (an ordering flag)
    SortingMarkdownFile smf = new SortingMarkdownFile(rmd.getFiles());
    ArrayList<File> sortedFiles = smf.orderingFlag(orderArg.toLowerCase());
    // writes content of files to the output file in, ordered in the way given above
    WriteMarkdownFile wmf = new WriteMarkdownFile(sortedFiles);
    String fileContent = wmf.getRightContent(outputArg);

    // Used in order to catch if the cmdline arguments are wrong
    try {
      wmf.writeToFile(outputArg, fileContent);
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    }
  }
}