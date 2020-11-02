import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class MyInsertionSort {
	static int[] insertionSort(int[] a, int lo, int hi) {
		for (int i = lo; i <= hi; i++) {
			for (int j = i; j > lo && a[j] < a[j-1]; j--) {
				int x = a[j]; 
				a[j] = a[j-1]; 
				a[j-1] = x;            
			}        
		}
		return a;
	}    
	// Checks if the first n element of a are in sorted order.
	private static boolean isSorted(int[] a, int lo, int hi) {
		int flaws = 0;       
		for (int i = lo+1; i <= hi; i++) {            
			if (a[i] < a[i-1]) {                
				if (flaws++ >= 10) {                    
					System.out.println("...");                    
					break;                
				}                
				System.out.println("a["+(i-1)+"] = "+a[i-1]+", a["+i+"] = "+a[i]);            
			}        
		}        
		return flaws == 0;    
	}    
	// Shuffles the first n elements of a.    
	public static void shuffle(int[] a, int lo, int hi) {        
		Random rand = new Random();        
		for (int i = lo; i <= hi; i++) {            
			int r = i + rand.nextInt(hi+1-i);     
			// between i and hi            
			int t = a[i]; a[i] = a[r]; a[r] = t;        
		}    
	}    
	static int[] readIntfile(String filename) throws Exception {
		// Read file into a byte array, and then combine every group of four bytes to an int. (Not        
		// the standard way, but it works!)        
		byte[] bytes = Files.readAllBytes(Paths.get(filename));        
		int[] ints = new int[bytes.length/4];        
		for (int i = 0; i < ints.length; i++) {            
			for (int j = 0; j < 4; j++) { 
				ints[i] += (bytes[i*4+j] & 255) << (3-j)*8; 
			}        
		}        
		return ints;    
	} 
	
	public static void main(String[] args) throws Exception {        
		int[] data = readIntfile("files/smallints"); // Also try "largeints"!        
		int N = 10000;    // Change to some smaller number to test on part ofarray.        
		// Look at numbers before sorting, unless there are too many of them. 
		data = insertionSort(data, 0, data.length-1);
		for (int i = 0; i < N && i < data.length; i++) { 
			System.out.print(data[i]+" "); 
		}            
		System.out.print("\n\n");
	}

}
