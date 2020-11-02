package wordchain;

import java.util.LinkedList;
import java.util.Stack;

public class BreadthFirstPaths {
	private boolean[] marked;
	private int[] edgeTo;
	private int s = 0;

	public BreadthFirstPaths(DiGraph g, int s) {
		marked = new boolean[g.V()];
		edgeTo = new int[g.V()];
		this.s = s;
		bfs(g, s);
	}

	private void bfs(DiGraph g, int s2) {
		LinkedList<Integer> queue = new LinkedList<Integer>();
		marked[s] = true;
		queue.offer(s);

		while(!queue.isEmpty()) {
			int v = queue.poll();
			for(int w : g.getAdjList().get(v)) {
				if(!marked[w]) {
					edgeTo[w] = v;
					marked[w] = true;
					queue.offer(w);
				}
			}
		}
	}

	public boolean hasPathTo(int v) {
		return marked[v];
	}

	public Stack<Integer> pathTo(int v) {
		if(!hasPathTo(v)) return null;
		Stack<Integer> path = new Stack<Integer>();
		for(int x = v; x != s; x = edgeTo[x]) path.push(x);
		path.push(s);
		return path;
	}

}
