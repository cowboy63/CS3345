
// Arami Guerra de la Llera
// axg166831

import java.util.Scanner;

public class ARGUP3 {

	public static void main(String[] args) {

		// scanner
		Scanner file = new Scanner(System.in);

		// read type of hash table
		String in = file.next();

		int size = file.nextInt();

		// initialize the hash table
		MyHashTable t = new MyHashTable(size);

		while (!in.equals("E")) {
			// read the command
			in = file.next();

			// variables
			String[] ap;
			boolean success;
			String re;

			switch (in) {
			case "C":
				// clear the table
				t.clear();
				break;
			case "A":
				// read input
				ap = file.next().split(":");

				// overflow
				boolean isOver = t.membership() == t.table.length;

				// check if overflow
				if (isOver) {
					System.out.println("Table has overflowed");
				} else {
					// attempt to insert item
					success = t.insert(ap[0], ap[1]);

					// print out whether the item was inserted or not
					System.out.println(success ? "Key "+ap[0]+" inserted at location " + t.getLocation(ap[0], false)
							: "Key "+ap[0]+" already exists");

				}

				break;
			case "R":
				// read input
				re = file.next();

				// attempt to delete
				success = t.delete(re);

				// print out whether the item was deleted or not
				System.out.println(success ? "Key " + re + " deleted" : "Key " + re + " not found");

				break;
			case "S":
				// read input
				re = file.next();

				// attempt to delete
				String record = t.isPresent(re);

				// if key not found
				if (record != null) {
					int pos = t.getLocation(re, false);
					System.out.println("Key " + re + ":" + record + " found at location " + pos);
				} else {
					System.out.println("Key " + re + " not found");
				}

				break;
			case "M":
				// prints membership
				System.out.println("Membership is " + t.membership());
				break;
			case "P":
				// list all the members
				t.listAll();
				break;
			case "T":
				System.out.println("Total probes during inserts = "+ t.probesI);
				System.out.println("Total probes during unsuccessful searches = "+t.probesUS);
				System.out.println("Total probes during successful searches = "+t.probesSS);
				break;

			default:
				break;
			}

		}

	}

}

// Uses linear probing because last name starts with 'G'
class MyHashTable {
	E[] table;
	int member;
	int probesI;
	int probesUS;
	int probesSS;

	public MyHashTable(int x) {
		table = new E[x];
		member = 0;
		probesI = 0;
		probesUS = 0;
		probesSS = 0;
	}

	public boolean insert(String key, String record) {
		// check if the key is present
		if (getLocation(key, false) != -1) {
			return false;
		}

		// increase probes and members
		member++;
		probesI++;

		// get the hash
		int pos = hash(key, table.length);

		// search for a position
		while (true) {
			
			if(table[pos]==null|| table[pos].key.equals(""))
			{
				break;
			}
		
			pos++;
			pos %= table.length;
			probesI++;
		}

		// insert entry
		table[pos] = new E(key, record);

		return true;
	}
	

	public String isPresent(String key) {
		// get location of key
		int pos = getLocation(key, true);

		// return null or the record corresponding to the key
		return pos == -1 ? null : table[pos].record;
	}

	// get the location of a key
	public int getLocation(String key, boolean S) {

		// counter
		int cnt = 0;

		// probe counter
		int probes = 0;

		// get the hash
		int pos = hash(key, table.length);

		// search for a position
		while (cnt < table.length) {
			probes++;
		
			// check if null
			if(table[pos] == null)
			{
				probesUS += S ? probes : 0;
				return -1;
			}
			
			// if it finds it return the position
			if (table[pos] != null && table[pos].key.equals(key)) {
				// add probes and return position
				probesSS += S ? probes : 0;
				return pos;
			}

			// increment
			pos++;
			pos %= table.length;
			cnt++;

		}

		// did not find it
		System.out.println(S+" "+ probesUS+" "+ probes+" -------");
		probesUS += S ? probes : 0;
		return -1;
	}

	public boolean delete(String key) {
		// check if it exist
		if (getLocation(key, false) == -1) {
			return false;
		}
		
		// decrease membership
		member--;

		// get the hash
		int pos = hash(key, table.length);

		// search for a position
		while (table[pos] == null || !table[pos].key.equals(key)) {
			pos++;
			pos %= table.length;
		}

		
		// delete the key
		table[pos] = new E("",""); 
		
		return true;
	}
	
	// return membership
	public int membership() {
		return member;
	}

	// list all the entries
	public void listAll() {
		for(int i =0; i < table.length; i++)
		{
			if(table[i] != null && !table[i].key.equals(""))
			{
				System.out.println(i+" "+table[i].key+":"+table[i].record);
			}
		}
	}

	// clear the table and resets the stats
	public void clear() {
		table = new E[table.length];
		member = 0;
	}

	// PROVIDED HASH FUNCTION
	public int hash(String ky, int tableSize) {
		int m = ky.length();
		int sum = 0; // use unsigned ints in C/C++
		for (int i = m - 1; i >= 0; i--)
			sum = sum * 31 + (int) (ky.charAt(i)); // automatically applies mod
													// 2^{32}
		sum = sum & 0x7fffffff; // remove the sign bit
		return sum % tableSize; // % works fine with a positive dividend
	}

}

// Entry Class
class E {
	String key;
	String record;

	public E(String k, String r) {
		key = k;
		record = r;
	}
}
