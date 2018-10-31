Creates a Hashtable that stores strings. Any collisions in the insert process are resolved with separate chaining using a
singly-linked list. When the load factor becomes greater than 2/3, the table must expand and rehash. 

This hash table is used in a Line Counter program. This program creates a hash table for individual .txt files. The table
stores each line of a file in the table. For any number of input files, the program will print out what percentage of the 
first file is also in each of the other files. 
