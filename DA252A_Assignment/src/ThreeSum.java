

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ThreeSum {    
	public static int count(int[] a){      
		int N = a.length;       
		int count = 0;       
		for (int i = 0; i < N; i++)             
			for (int j = i+1; j < N; j++)             
				for (int k = j+1; k < N; k++)                
					if (a[i] + a[j] + a[k] == 0)                  
						count++;       
		return count;    
	}   
	public static Object[] print(String file) {
		List<String> encoded = null;
		String s = "";
		Object[] o = new Object[3];
		try {
			encoded = Files.readAllLines(Paths.get(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int[] values = new int[encoded.size()];
		for(int i = 0;i<values.length; i++)
			values[i]=Integer.parseInt(encoded.get(i));
		long before = System.currentTimeMillis();
		System.out.println("Start");
		int count = count(values);
		System.out.println("End");
		long after = System.currentTimeMillis();
		double time = ((after-before)/1000.00000000);
		o[0]="Values: "+values.length;
		o[1]="Time: "+time;
		o[2]="Antal: "+count;
		return o;
	}
} 
