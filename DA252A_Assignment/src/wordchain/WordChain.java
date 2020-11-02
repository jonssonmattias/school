package wordchain;
import java.io.*;
import java.util.*;

public class WordChain {
	private static ArrayList<String> words, startWords, targetWords;;
	private static Utils utils = new Utils();
	private static DiGraph graph;
	private static String[] levels = {"13", "250", "5757"};
	private static int level = 2;
	
	private static void load() throws IOException {
		utils.loadTestData("files/words-"+levels[level]+"-test.txt");
		startWords = utils.getStartWords();
		targetWords = utils.getTargetWords();
		words = utils.loadData("files/words-"+levels[level]+"-data.txt");
		graph = new DiGraph(words);
	}
	
	private static void findChain() {
		for(int i = 0; i < startWords.size(); i++) {
			System.out.println(graph.getPaths(startWords.get(i), targetWords.get(i)));
		}
	}
	
	public static void main(String[] args) throws IOException {
		load();
		findChain();
		System.out.println(graph.toString());
	}
}
