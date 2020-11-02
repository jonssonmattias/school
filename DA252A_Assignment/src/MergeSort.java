import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class MergeSort {

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

	public static int[] merge(int[] a, int hi, int lo) {
		int mid = lo + (hi-lo)/2;
		int[] b = new int[a.length];
		for(int i=lo; i<=hi; i++) b[i]=a[i];
		int i=lo, j=mid+1;
		for(int k=lo;k<=hi;k++) 
			if(i>mid) a[k] = b[j++];
			else if(j>hi) a[k]=b[i++];
			else if(b[i]<b[j]) a[k]=b[i++];
			else a[k]=b[j++];
		return a;
	}

	public static int[] sort(int[] a, int lo, int hi) {
		if(lo>=hi)return a;
		int mid = (lo+hi)/2;
		sort(a, lo, mid);
		sort(a, mid+1, hi);
		a = merge(a, hi, lo);
		return a;
	}

	public static void main(String[] args) throws Exception {
		int a[] = readIntfile("files/smallints");
		long before = System.currentTimeMillis();
		a = sort(a, 0, a.length-1);
		long after = System.currentTimeMillis();
		double time = ((after-before)/1000.00000000);
		System.out.println("Tid: "+time);

	}
}
