package wordchain;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Utils {
	private ArrayList<String>[] testWords = new ArrayList[2];
	
	public ArrayList<String> loadData(String filename) throws IOException {
		ArrayList<String> words = new ArrayList<>();
		BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		while (true) {
			String word = r.readLine();
			if (word == null) break;
			assert word.length() == 5;  // indatakoll, om man kör med assertions på
			words.add(word);
		}
		return words;
	}

	public void loadTestData(String filename) throws IOException {
		for (int i = 0; i < 2; i++) testWords[i] = new ArrayList<String>(); 
		BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		while (true) {
			String line = r.readLine();
			if (line == null) break; 
			assert line.length() == 11; // indatakoll, om man kör med assertions på
			String start = line.substring(0, 5);
			String goal = line.substring(6, 11);
			testWords[0].add(start);
			testWords[1].add(goal);
		}
	}
	
	public ArrayList<String> getStartWords(){
		return testWords[0];
	}
	
	public ArrayList<String> getTargetWords(){
		return testWords[1];
	}
}
