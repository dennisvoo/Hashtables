//***********************
//NAME: <Dennis Vo>
//ID: <A12347682>
//LOGIN: <cs12srk>
//***********************

package hw8;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/** 
 * This class initializes a HashTable using chaining, where we store strings
 * and collisions are handled by adding the new element to the linkedlist at the
 * index we want to add to.
 * @version 1.0
 * @author Dennis Vo
 * @since 2016-05-21
 */

public class HashTable implements IHashTable {
  private int nelems;  //Number of element stored in the hash table
  private int expand;  //Number of times that the table has been expanded
  private int collision;  //Number of collisions since last expansion
  private String statsFileName; //FilePath to write statistics every rehash
  private boolean printStats = false; //determines if we will write statistics 
  private LinkedList<String>[] array; //using array of linkedlists to make table
  private LinkedList<String>[] newArray; // used for expanding
  private double threshold = (double) 2/3; // load factor for rehash comparison
  private int index; // used to store index returned from hash function
  private static final int DOUBLE = 2; // constant used to double "M"
  private static final int BASE = 27; // base used in hash method ex. in class
  
  /**
   * Constructor for hash table
   * @param size Initial size of the hash table
   */
  @SuppressWarnings("unchecked")
  public HashTable(int size) {
    nelems = 0;
    array = new LinkedList[size];
    // fill out array w/ linked lists
    for (int i = 0; i < size; i ++) {
      array[i] = new LinkedList<String>();
    }
  }

  /**
   * Constructor for hash table
   * @param size Initial size of the hash table
   * @param fileName File path to write statistics
   */
  @SuppressWarnings("unchecked")
  public HashTable(int size, String fileName){
    nelems = 0;
    array = new LinkedList[size];
    // fill out array with linked lists
    for (int i = 0; i < size; i ++) {
      array[i] = new LinkedList<String>();
    }
    statsFileName = fileName;
    printStats = true;
  
  }

  /**
   * This helper method acts as the hash function that determines where a string
   * is stored.
   * @param value is string being stored
   * @param arraySize is the mod size in the function
   * @return an int representing the index we are putting the string in
   */
  private int hashFunction(String value, int arraySize) {
    /* The hash method I am using is the most revised one we saw in class 
       where base is 27. I chose to make the size of the array a parameter
       because I will be using a new arraysize to rehash the contents of my
       old array into my new array(with twice the capacity) when expanding 
       I didn't subtract 96 so I could account for integers/whitespace */
    int hashValue = 0;
    for (int i = 0; i < value.length(); i++) {
      int letter = value.charAt(i) - 1;
      hashValue = Math.abs((hashValue * BASE + letter) % arraySize);
    }
    return hashValue;
  }
  
  /**
   * Inserts a string into the table if it's not there already.
   * @param value is string being inserted.
   * @return True only if string doesn't already exist
   * @throws NullPointerException
   */
  @Override
  public boolean insert(String value) {
    if (value == null) {
      throw new NullPointerException();
    }
    
    if (lookup(value) == true) {
      return false; //element already exists in table
    } else {
      if ( ((double) nelems/array.length) > threshold) {
        expand(); // need to expand,rehash if our load factor > 2/3
      }
      //first we check if we need to expand our table first, then we can insert
      
      index = hashFunction(value, array.length);
      if (array[index].size() != 0) {
        collision++; // increment collision if linked list already has 1 string
      }
      array[index].add(value); //add just adds the value onto the linked list
      nelems++;
      return true;
    }
  }

  /**
   * Deletes the value from the table, if it was already there.
   * @param value is string you're deleting
   * @return true only if string can be deleted
   * @throws NullPointerException
   */
  @Override
  public boolean delete(String value) {
    if (value == null) {
      throw new NullPointerException();
    }
  
    if (lookup(value) == false) {
      return false;
    } else {
      index = hashFunction(value, array.length);
      array[index].remove(value);
      // remove(Object o) removes first instance of that object in linked list
      nelems--;
      return true;
    }
  }

  /**
   * Checks to see if table contains the string specified.
   * @param value is string you're looking for
   * @return true if table has the string
   * @throws NullPointerException
   */
  @Override
  public boolean lookup(String value) {
    if (value == null) {
      throw new NullPointerException();
    }
    
    index = hashFunction(value, array.length);
    // using contains method from LinkedList javadoc to check if value is in
    // our hashtable
    if (array[index].contains(value)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Prints out the contents of the hashtable.
   */
  @Override
  public void printTable() {
    if (nelems == 0) {
      return; // only print if table isn't empty
    } else {
      System.out.println("Printing table:");
      for (int i = 0; i < array.length; i++) {
        System.out.print(i + ": ");
        if (array[i].isEmpty()) {
          System.out.println("\n"); // if linked list empty, go to next line
        } else {
          // create an iterator to traverse through linked lists
          Iterator<String> iter = array[i].listIterator(0);
          while (iter.hasNext()) {
            System.out.print(iter.next());
            if (iter.hasNext()) {
              System.out.print(", ");
            }
          }
          System.out.println("\n");
        }
      }
    }
  }

  /**
   * Gives number of key values in table.
   * @return nelems
   */
  @Override
  public int getSize() {
    return nelems;
  }

  /**
   * Helper method the array when the load factor exceeds the threshold.
   */
  @SuppressWarnings("unchecked")
  private void expand() {
    expand++; // increment b/c we know we will be expanding in this method
    if (printStats) {
      printStatistics(); // print stats for each time we expand
    }
    
    newArray = new LinkedList[(DOUBLE * array.length) + 1];
    for (int i = 0; i < newArray.length; i ++) {
      newArray[i] = new LinkedList<String>();
    }
    // initialize an array with double the capacity and fill w/ linked lists
    for (int i = 0; i < array.length; i++) {
    // iterate through entire array, going through each linked list of strings
      while (!array[i].isEmpty()) {
        String temp = array[i].remove();
        newArray[rehash(temp)].add(temp);
      }
      /* What this while loop does is that, while the linked list at the
       * specified has elements in the linked list, we will grab the first
       * element of the list, call the rehash method and find a new place to
       * put it in the new array, then add the element to the new index that
       * the rehash method will give us
       */
    }
    array = newArray; 
    // set array equal to newArray after we've copied everything over correctly
  }
  
  /**
   * Helper method used to rehash the old string values into the new array.
   * @param value is the string we are rehashing
   * @return the int value of the new index location
   */
  private int rehash(String value) {
    int newIndex = hashFunction(value, newArray.length);
    // provides way to find new place to put each value from old array
    collision = 0;
    /* only resetting collision in rehash because loadfactor and longest chain
     * are calculated again each time printStatistics is called 
     */
    return newIndex;
  }
  
  /**
   * This prints the current statistics after a resize if printStats is true.
   */
  private void printStatistics() {
    if (printStats) {
      double alpha = (double) nelems/array.length;
      String load = String.format("%1.2f",alpha); // rounds to 2 decimal places
      int n = 0;
      for (int i = 0; i < array.length; i++) {
        if (array[i].size() > n) {
          n = array[i].size();
        }
      }
      
      // using FileWriter to constantly append new stats to the file
      try { 
        FileWriter writer = new FileWriter(statsFileName, true);
        writer.append(expand + " resizes, load factor " + load + ", " +
           collision + " collisions, " + n + " longest chain");
        writer.append("\n");
        writer.close();
      } catch (IOException e) {}
    }
  }
}
