package wordchain;

import java.util.*;

public class DiGraph {
    private int V;
    private int E;
    private ArrayList<String>  words;
    private HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
    private List<List<Integer>> adjList = new ArrayList<List<Integer>>();
    private BreadthFirstPaths bfsPath;

    public DiGraph(ArrayList<String> words) {
        this.words = words;
        V = words.size();

        for (int i = 0; i < V; i++) {
            adjList.add(new ArrayList<Integer>());
            wordMap.put(words.get(i), i);
        }

        for (int i = 0; i < V; i++) {
            for (int j = i + 1; j < V; j++) {
                if (ifEdgeExists(words.get(i), words.get(j))) {
                    adjList.get(i).add(j);
                    E++;
                } 
                if (ifEdgeExists(words.get(j), words.get(i))) {
                    adjList.get(j).add(i);
                    E++;
                }
            }
        } 
    }
    
    public int getPaths(String source, String target) {
    	return getPaths(wordMap.get(source), wordMap.get(target));
    }
    
    public int getPaths(int source, int target) {
    	bfsPath = new BreadthFirstPaths(this, source);
    	if(bfsPath.hasPathTo(target)) return bfsPath.pathTo(target).size() - 1;
    	else return -1;
    }

    public List<List<Integer>> getAdjList() {
		return adjList;
	}

	public int V() {
    	return V;
    }
    
    public int E() {
    	return E;
    }
    
    private boolean ifEdgeExists(String word1, String word2) {
        ArrayList<Character> w2 = new ArrayList<Character>();

        for(int i = 0; i < 5; i++) w2.add(word2.charAt(i));

        for(int i = 1; i < word1.length(); i++) {
            char c = word1.charAt(i);
            for(int j = 0; j < w2.size(); j++) 
                if(c == w2.get(j)) {
                    w2.remove(j);
                    break;
                }
        }
        return (w2.size() < 2);
    }
    
    public String toString() {
        return "\nGrafen har " + V + " noder och " + E + " bågar\n";
    }
}