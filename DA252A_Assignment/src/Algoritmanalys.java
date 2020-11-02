import java.util.HashMap;
import java.util.Map;

public class Algoritmanalys {
	public static int find(int[] array) {
		int count=0;
		for(int i=0;i<array.length;i++)
			for(int j=i+1;j<array.length;j++)
				if(array[i]==array[j])count++;

		return count;
	}
//	public static int find2(int[] arr) {
//		HashMap<Integer,Integer> frequency = new HashMap<>(); 
//		int numberOfPairs=0;
//		for(int i = 0; i < arr.length; i++) 
//			if(frequency.containsKey(arr[i])) 
//				frequency.put(arr[i],frequency.get(arr[i]) + 1); 
//			else
//				frequency.put(arr[i], 1);  
//
//		for(Map.Entry<Integer,Integer> it : frequency.entrySet()){  
//			int count = it.getValue(); 
//			numberOfPairs += ((int)Math.pow(count, 2)-count)/2; 
//		} 
//		return numberOfPairs; 
//	}
	public static int find2(int[] arr) {
	   int count=1, pairs=0, i=0, j=i+1;
	   while(i<arr.length)
		   if(j<arr.length && arr[i]==arr[j]) {
			   count++;
			   j++;
		   }
		   else {
			   i=j;
			   j=i+1;
			   pairs += ((int)Math.pow(count, 2)-count)/2;
			   count = 1;
		   }
	   
	   return pairs;
	}

}
