//***********************
//NAME: <Dennis Vo>
//ID: <A12347682>
//LOGIN: <cs12srk>
//***********************

package hw8;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/** 
 * This compares two files by storing their contents in individual hash tables
 * and comparing to see similarities between the tables.
 * @version 1.0
 * @author Dennis Vo
 * @since 2016-05-21
 */

public class LineCounter {
  private static final int SIZE = 11; //number used to initialize table size
  private static final int HUNDRED = 100; // used for calculating percent
  private static int match; // counter for number of matches with each file
  private static int percent; // variable to hold percentage calculated
  private static int numLines; // counts number of lines in the file
  
  /**
   * This method is called to print to console the right output.
   * @param filename is name of first file
   * @param compareFileName is name of file you're comparing to
   * @param percentage is percentage of lines in first file in next file
   */
  public static void printToConsole(String filename,
      String compareFileName, int percentage) {

    if(!filename.isEmpty())
      System.out.println("\n"+filename+":");

    if(!compareFileName.isEmpty())
      System.out.println(percentage+"% of lines are also in "+compareFileName);
  }

  /**
   * Main method controls populating an array of hashtables with values from
   * the argument text files. Then, we compare each hashtable with the other
   * ones in the array.
   * @param args are the input files included in command line
   */
  public static void main(String[] args) {

    if(args.length<1) {
      System.err.println("Invalid number of arguments passed");
      return;
    }

    int numArgs = args.length;
    HashTable[] tableList = new HashTable[numArgs];

    //Preprocessing: Read every file and create a HashTable

    for(int i=0; i<numArgs; i++) {
      File file = new File(args[i]); // treat each 
      tableList[i] = new HashTable(SIZE);
      // create hashtable at each index then we scan each file
      try {
        Scanner reader = new Scanner(file);
        while (reader.hasNextLine()) {
          String temp = reader.nextLine();
          tableList[i].insert(temp); 
          // fill each hashtable w/ lines from argument files
        }
        reader.close();
      } catch (FileNotFoundException e) {}
    }

  //Find similarities across files
    for(int i=0; i<numArgs; i++) {
      String fileName = args[i]; 
      // set string var to args[i], this will reset every iteration of loop
      File file = new File(args[i]);
      try {
        for (int j = 0; j < numArgs; j++) { // iterate through other tables
          Scanner reader = new Scanner(file);
          String compareFile = args[j]; 
          // set string var to arg we're comparing to, resets for every new file
        
          while (reader.hasNextLine()) { // scan file to compare
            numLines++;
            if (tableList[j].lookup(reader.nextLine())) {
              match++; // if line found in other file, increment match
            }
          }
        
          percent = percentage(match,numLines);
          // percent found by number of matches/total lines 

          if (j != i ) { // to prevent comparing file to itself
            printToConsole(fileName, compareFile, percent);
            fileName = "";
            // on first print, we set fileName too "" to get right output
          }
          match = 0;
          numLines = 0; // need to reset these vars for each comparison
          reader.close();
        }
      } catch (FileNotFoundException e) {}
    }
  }
  
  /**
   * A helper method to calculate the percentage of similarities.
   * @param matches is the number of matches
   * @param divisor is the total number of inputs/lines
   * @return the percentage from dividing matches by total lines
   */
  private static int percentage(int matches, int divisor) {
    return  (int) (((float) matches / divisor) * HUNDRED);
    // convert one int to float so division will return a float, then we can
    // multiply that by 100 and cast that result to int
  }
}
