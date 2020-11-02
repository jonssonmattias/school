import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class QuickSort {

	private static int[] readIntfile(String filename) throws Exception {
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

	private static void swap(int[] a, int i, int j) {
		int x = a[i]; 
		a[i] = a[j]; 
		a[j] = x;  
	}

	public static void shuffle(int[] a, int lo, int hi) {        
		Random rand = new Random();        
		for (int i = lo; i <= hi; i++) {            
			int r = i + rand.nextInt(hi+1-i);                 
			int t = a[i]; 
			a[i] = a[r]; 
			a[r] = t;        
		}    
	}    

	private static int partition(int[] a, int lo, int hi) {
		int p = a[lo]; // lowest
//		int p = a[hi]; // highest
//		int p = a[(hi+lo)/2]; // middle
//		int p = a[new Random().nextInt(hi)]; // random
		int i=lo, j=hi+1;
		while(i<j) {
			i++;
			j--;	
			while(a[i]<p && i<hi) i++;
			while(a[j]>p)j--;
			if(i>=j)break;
			swap(a, i, j);
		}
		swap(a, lo, j);
		return j;
	}

	private static int[] sort(int[] a, int lo, int hi) {
		if(hi>lo) {
			int pIndex = partition(a, lo, hi);
			sort(a, lo, pIndex-1);
			sort(a, pIndex+1, hi);
		}
		return a;
	}

	public static void main(String[] args) throws Exception {
		int a[] = readIntfile("files/smallints");
		shuffle(a, 0, a.length-1);
		long before = System.currentTimeMillis();
		a = sort(a, 0, a.length-1);
		long after = System.currentTimeMillis();
		double time = ((after-before)/1000.00000000);
		System.out.println("Tid: "+time);
	}
}
