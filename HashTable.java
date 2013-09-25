///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            HashTable.java
// Files:            n/a
// Semester:         Spring 2012
//
// Author:           Kristin Cox
// CS Login:         kcox
// Lecturer's Name:  Beck Hasti
// Lab Section:      1
//
//                   PAIR PROGRAMMERS COMPLETE THIS SECTION
// Pair Partner:     Will Kraus
// CS Login:         kraus
// Lecturer's Name:  Beck Hasti
// Lab Section:      1
//////////////////////////// 80 columns wide //////////////////////////////////

import java.io.*;
import java.util.LinkedList;

/**
 * This class implements a hashtable that using chaining for collision handling.
 * Any non-<tt>null</tt> item may be added to a hashtable.  Chains are 
 * implemented using <tt>LinkedList</tt>s.  When a hashtable is created, its 
 * initial size, maximum load factor, and (optionally) maximum chain length are 
 * specified.  The hashtable can hold arbitrarily many items and resizes itself 
 * whenever it reaches its maximum load factor or whenever it reaches its 
 * maximum chain length (if a maximum chain length has been specified).
 * 
 * Note that the hashtable allows duplicate entries.
 */
public class HashTable<T> {
    //store info about table parameters
	private int tableSize, maxChainLength, numItems;
    private double loadFactor;
    //the actual hashtable
    private LinkedList<T>[] table;
    
    /**
     * Constructs an empty hashtable with the given initial size, maximum load
     * factor, and no maximum chain length.  The load factor should be a real 
     * number greater than 0.0 (not a percentage).  For example, to create a 
     * hash table with an initial size of 10 and a load factor of 0.85, one 
     * would use:
     * 
     * <dir><tt>HashTable ht = new HashTable(10, 0.85);</tt></dir>
     *
     * @param initSize the initial size of the hashtable.
     * @param loadFactor the load factor expressed as a real number.
     * @throws IllegalArgumentException if <tt>initSize</tt> is less than or 
     *         equal to 0 or if <tt>loadFactor</tt> is less than or equal to 0.0
     **/
    public HashTable(int initSize, double loadFactor) {
    	//throw exception if parameters out of bounds
    	if (initSize <= 0 || loadFactor <=0) {
    		throw new  IllegalArgumentException();
    	}
    	LinkedList<T>[] table = (LinkedList<T>[])(new LinkedList[initSize]);
    	this.table = table;
    	this.loadFactor = loadFactor;
    	this.maxChainLength = -1;
    	this.tableSize = initSize;
    	this.numItems = 0;
    }
    
    
    /**
     * Constructs an empty hashtable with the given initial size, maximum load
     * factor, and maximum chain length.  The load factor should be a real 
     * number greater than 0.0 (and not a percentage).  For example, to create 
     * a hash table with an initial size of 10, a load factor of 0.85, and a 
     * maximum chain length of 20, one would use:
     * 
     * <dir><tt>HashTable ht = new HashTable(10, 0.85, 20);</tt></dir>
     *
     * @param initSize the initial size of the hashtable.
     * @param loadFactor the load factor expressed as a real number.
     * @param maxChainLength the maximum chain length.
     * @throws IllegalArgumentException if <tt>initSize</tt> is less than or 
     *         equal to 0 or if <tt>loadFactor</tt> is less than or equal to 0.0 
     *         or if <tt>maxChainLength</tt> is less than or equal to 0.
     **/
    public HashTable(int initSize, double loadFactor, int maxChainLength) {
    	//throw exception if parameters out of bounds
    	if (initSize <= 0 || loadFactor <=0 || maxChainLength <= 0) {
    		throw new  IllegalArgumentException();
    	}
    	LinkedList<T>[] table = (LinkedList<T>[])(new LinkedList[initSize]);
    	this.table = table;
    	this.loadFactor = loadFactor;
    	this.maxChainLength = maxChainLength;
    	this.tableSize = initSize;
    	this.numItems= 0;
    }
    
    
    /**
     * Determines if the given item is in the hashtable and returns it if 
     * present.  If more than one copy of the item is in the hashtable, the 
     * first copy encountered is returned.
     *
     * @param item the item to search for in the hashtable.
     * @return the item if it is found and <tt>null</tt> if not found.
     **/
    public T lookup(T item) {
    	//iterate through each linked list in the array
    	for (LinkedList<T> list : this.table){
    		//check if the list is empty
    		if (list != null) {
    			//if not go through all items in the list
    			for (int i = 0; i < list.size(); i++) {
    				//if one matches, return it
    				if (list.get(i).equals(item)) {
    					return list.get(i);
    				}
    			}
    		}
    	}
    	//if not found anywhere
    	return null;
    }


    /**
     * Inserts the given item into the hashtable.  The item cannot be 
     * <tt>null</tt>.  If there is a collision, the item is added to the end of
     * the chain.
     * <p>
     * If the load factor of the hashtable after the insert would exceed 
     * (not equal) the maximum load factor (given in the constructor), then the 
     * hashtable is resized.  
     * 
     * If the maximum chain length of the hashtable after insert would exceed
     * (not equal) the maximum chain length (given in the constructor), then the
     * hashtable is resized.
     * 
     * When resizing, to make sure the size of the table is reasonable, the new 
     * size is always 2 x <i>old size</i> + 1.  For example, size 101 would 
     * become 203.  (This guarantees that it will be an odd size.)
     * </p>
     * <p>Note that duplicates <b>are</b> allowed.</p>
     *
     * @param item the item to add to the hashtable.
     * @throws NullPointerException if <tt>item</tt> is <tt>null</tt>.
     **/
    public void insert(T item) {
        //can't insert null into table
    	if ( item == null) {
        	throw new NullPointerException();
        }
    	//increment number of items in list
        numItems++;
        //get location for placing item
        int index = item.hashCode();
        index = (index%this.tableSize);
        if (index < 0) {
        	index = index + tableSize;
        }
        //if index is empty, make a new list and add the item
        if (this.table[index] == null) {
        	this.table[index] = new LinkedList<T>();
            this.table[index].add(item);
        }
        //else just add it
        else {
        	this.table[index].add(item);
        }
 
        //if table is too full (exceed load factor), resize
        if ((double)(numItems/tableSize) > this.loadFactor || 
        		(this.maxChainLength > 1 && table[index].size() + 1 > this.maxChainLength)) {
        	//double table size
        	this.tableSize = (this.tableSize * 2) + 1;
        	//make new table
        	LinkedList<T>[] newTable = (LinkedList<T>[])(new LinkedList[(this.tableSize)]);
        	//go through every item in old table
        	for (LinkedList<T> list : this.table){
        		if (list != null) {
        			//rehash all the items in old table into new table
        			for (int i = 0; i < list.size(); i++) {
        				T newItem = list.get(i);
        				index = newItem.hashCode();
        				index = (index%this.tableSize);
        				if (index < 0) {
        					index = index + tableSize;
        				}
        				if (newTable[index] == null) {
        					newTable[index] = new LinkedList<T>();
        					newTable[index].add(newItem);
        				}
        				else {
        					newTable[index].add(newItem);
        				}
        			}
        		}
        	}
        	//store new table and overwrite old table
        	this.table = newTable;
        }

    }
     
    /**
     * Removes and returns the given item from the hashtable.  If the item is 
     * not in the hashtable, <tt>null</tt> is returned.  If more than one copy 
     * of the item is in the hashtable, only the first copy encountered is 
     * removed and returned.
     *
     * @param item the item to delete in the hashtable.
     * @return the removed item if it was found and <tt>null</tt> if not found.
     **/
    public T delete(T item) {
    	int indexTable = 0;
    	int indexList = 0;
    	//go through each item, keeping track of its index and location in list
    	for (LinkedList<T> list : this.table){
    		for (int i = 0; i < list.size(); i++) {
    			//if found, delete and return it
    			if (list.get(i).equals(item)) {
    				numItems--;
    				return this.table[indexTable].remove(indexList);  
    			}
    			indexList++;
    		}
    		indexTable++;
    	}
    	//if not found
    	return null;
    }
    
    
    /**
     * Prints all the items in the hashtable to the <tt>PrintStream</tt> 
     * supplied.  The items are printed in the order determined by the index of
     * the hashtable where they are stored (starting at 0 and going to 
     * (table size - 1)).  The values at each index are printed according 
     * to the order in the <tt>LinkedList</tt> starting from the beginning. 
     *
     * @param out the place to print all the output.
     **/
    public void dump(PrintStream out) {
    	PrintStream p = new PrintStream(out);
    	p.println("Hashtable contents:");
    	int index = 0;
    	for (LinkedList<T> list : this.table){
    		if (list != null) {
    			p.print(index + ": ");
    			p.println(list);
    		}
    		index++;
    	}
    }
    
  
    /**
     * Prints statistics about the hashtable to the <tt>PrintStream</tt> 
     * supplied.  The statistics displayed are: 
     * <ul>
     * <li>the current table size
     * <li>the number of items currently in the table 
     * <li>the current load factor
     * <li>the length of the largest chain
     * <li>the number of chains of length 0
     * <li>the average length of the chains of length > 0
     * </ul>
     *
     * @param out the place to print all the output.
     **/
    public void displayStats(PrintStream out) {
    	PrintStream p = new PrintStream(out);
    	p.println("Hashtable statistics:\ncurrent table size: " + tableSize);
    	p.println("# items in table: " + numItems);
    	p.println("current load factor: " + (((double)numItems)/tableSize));
    	p.println("longest chain length: " + largestChain());
    	p.println("# 0-length chains: " + emptyChains());
    	p.println("avg (non-0) chain length: " + avgChainLength());
    }
    
    /**
     * Calculates the average length of all non-empty chains in the 
     * table.
     * 
     * @return double--the average chain length
     */
   private double avgChainLength (){
	   double avgLength = 0;
	   //count total number of chains
	   int number = 0;
	   //sum lengths of all chains in the table
	   for(LinkedList<T> list : table){
		   if(list != null && list.size() > 0 ){
			   avgLength += list.size();
			   number++;
		   }
	   }
	   //divide by number of chains
	   return avgLength/number;
   }
   
   /**
    * Calculates the length of the longest chain in the table.
    * 
    * @return int -- length of longest chain
    */
   private int largestChain() {
	   int longest = 0;
	   //go through all chains and find the longest
	   for(LinkedList<T> list : table){
		   if(list != null && list.size() > longest ){
			   longest = list.size();
		   }
	   }
	   return longest;
   }
   
   /**
    * Counts the number of empty chains in the table.
    * 
    * @return int -- number of empty chains
    */
   private int emptyChains() {
	   int empties = 0;
	   //go through all chains and count the empty ones
	   for(LinkedList<T> list : table){
		   if(list == null || list.size() == 0){
			   empties++;
		   }
	   }
	   return empties;
   }
}
