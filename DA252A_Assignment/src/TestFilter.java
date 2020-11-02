

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class TestFilter {

	public static String filterOutChar(String in, char charToGetRidOf) {
		String out = "";
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			if (c != charToGetRidOf) {
				out += c;
			}
		}
		return out;
	}

	public static Object[] print(int value) {
		byte[] encoded = null;
		String s = "";
		Object[] o = new Object[2];
		System.out.println(value);
		try {
			encoded = Files.readAllBytes(Paths.get("files/bible-en.txt"));
			s = new String(encoded, 0, value, "US-ASCII");
		} catch (IOException e) {
			e.printStackTrace();
		}
		long before = System.currentTimeMillis();
		String t = filterOutChar(s, 'e');
		long after = System.currentTimeMillis();
		//  System.out.println("Efter: " + t);
		double time = ((after-before)/1000.00000000);
		System.out.println("Tid: "+time);
		System.out.println("--------------------------------");
		o[0]=value;
		o[1]=time;
		return o;
	}
}
