//***********************
//NAME: <Dennis Vo>
//ID: <A12347682>
//LOGIN: <cs12srk>
//***********************

package hw8;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/** 
 * Tester files will test methods of HashTable.java.
 * @version 1.0
 * @author Dennis Vo
 * @since 2016-05-21
 */

public class HashTableTester {
  private HashTable strings1;
  private HashTable strings2;

  /**
   * Sets up some HashTables to use for testing.
   */
  @Before
  public void setUp() {
    strings1 = new HashTable(3);
    strings1.insert("hi");
    
    strings2 = new HashTable(3, "stats.txt");
    strings2.insert("hello");
  }

  /**
   * This tests the insert method to see if elements are inserted correctly
   * as well as the resize method is working.
   */
  @Test
  public void testInsert() {
    try {
      strings1.insert(null);
    } catch (NullPointerException e) {}
    
    assertTrue(!strings1.insert("hi"));
    assertEquals(1,strings1.getSize());
    
    strings2.insert("hi");
    strings2.insert("Dennis");
    assertTrue(strings2.lookup("Dennis"));
    strings2.printTable();
    strings2.insert("Vo");
    strings2.insert("CSE12");
    strings2.insert("Homework 8");
    strings2.insert("Hash");
    strings2.insert("Table");
    strings2.insert("Tester");
    strings2.insert("September");
    strings2.insert("27");
    strings2.insert("1996");
    strings2.printTable();
    
    assertTrue(strings2.lookup("Dennis"));
    assertTrue(strings2.lookup("Vo"));
    assertTrue(strings2.lookup("CSE12"));
    assertTrue(strings2.lookup("Homework 8"));
  }
  
  /**
   * Checks if the delete method works correctly and returns correct boolean
   * value.
   */
  @Test
  public void testDelete() {
    try {
      strings1.delete(null);
    } catch (NullPointerException e) {}
    
    assertTrue(strings1.delete("hi"));
    assertTrue(!strings1.delete("hi"));
  }
  
  /**
   * Test to see if method returns true only if key is in the table.
   */
  @Test
  public void testLookUp() {
    try {
      strings1.lookup(null);
    } catch (NullPointerException e) {}
    
    assertTrue(strings1.lookup("hi"));
    strings1.delete("hi");
    assertTrue(!strings1.lookup("hi"));
    assertTrue(!strings1.lookup("hello"));
  }
  
  /**
   * This tests the method getSize to see if nelems is correctly updating.
   */
  @Test
  public void testSize() {
    assertEquals(1,strings1.getSize());
    strings1.insert("my bad");
    assertEquals(2,strings1.getSize());
    
    assertEquals(1,strings2.getSize());
    strings2.delete("hello");
    assertEquals(0,strings2.getSize());
  }
}
