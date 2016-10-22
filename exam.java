import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Exam creation problem: Questions in the exam database have a range of
 * difficulty levels and a diversity of topics. We have a requirement to pick a
 * subset of these questions to create an exam. This program is going to check
 * whether we can fulfill the requirements for a good exam. This is a
 * constrained selection problem which can be solved by network flow.
 * 
 * @author Zhen Chen
 *
 */

public class exam {
	private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	private static int n;
	private static int m;
	private static int size;
	private static Map<String, Integer> difficultyMap;
	private static Map<String, Integer> topicMap;
	private static Map<String, Integer> difficultyIndex;
	private static Map<String, Integer> topicIndex;
	private static int[][] graph;

	private static void getConstraint(Map<String, Integer> map, Map<String, Integer> index) throws IOException {
		String line;
		String[] list;
		line = reader.readLine();
		list = line.split("\\s");
		for (String s : list) {
			if (!index.containsKey(s)) {
				// this is a new node, increase the size of the graph
				index.put(s, size++);
				map.put(s, 1);
			} else {
				map.put(s, map.get(s) + 1);
			}
		}
	}

	private static void getQuestion() throws IOException {
		String line;
		String[] list;
		String difficulty;
		String topic;
		for (int i = 0; i < n; i++) {
			line = reader.readLine();
			list = line.split("\\s");
			difficulty = list[2];
			topic = list[1];
			// this is a question we might want
			if (difficultyIndex.containsKey(difficulty) && topicIndex.containsKey(topic)) {
				graph[difficultyIndex.get(difficulty)][topicIndex.get(topic)]++;
			}
		}
	}

	/**
	 * Find a path in the given graph using DFS from the start node to the
	 * target node and only use edges with weight at least threshold.
	 * 
	 * @param graph
	 * @param start
	 * @param target
	 * @param threshold
	 * @return a double ended queue of integers indicating the path
	 */
	private static Deque<Integer> findPath(final int[][] graph, int start, int target, int threshold) {
		Deque<Integer> path = new LinkedList<Integer>();
		boolean[] visited = new boolean[size];
		boolean found;
		path.add(start);
		visited[start] = true;
		while (!path.isEmpty()) {
			Integer current = path.peekLast();
			if (current == target) {
				return path;
			}
			found = false;
			for (int i = 0; i < size; i++) {
				if (graph[current][i] >= threshold && !visited[i]) {
					path.add(i);
					visited[i] = true;
					found = true;
					break;
				}
			}
			if (!found) {
				path.removeLast();
			}
		}
		return path;
	}

	private static int findBottleneck(final int[][] graph, final Deque<Integer> path) {
		Iterator<Integer> it = path.iterator();
		Integer from = it.next();
		Integer to;
		int bottleneck = Integer.MAX_VALUE;
		int weight;
		while (it.hasNext()) {
			to = it.next();
			weight = graph[from][to];
			if (weight < bottleneck) {
				bottleneck = weight;
			}
			from = to;
		}
		return bottleneck;
	}

	private static void networkFlow() {
		int delta = n;
		Deque<Integer> path;
		Iterator<Integer> it;
		Integer from;
		Integer to;
		int bottleneck;
		while (delta > 0) {
			while (true) {
				path = findPath(graph, 0, size - 1, delta);
				if (path.isEmpty()) {
					break;
				}
				bottleneck = findBottleneck(graph, path);
				it = path.iterator();
				from = it.next();
				while (it.hasNext()) {
					to = it.next();
					graph[from][to] -= bottleneck;
					graph[to][from] += bottleneck;
					from = to;
				}
			}
			delta /= 2;
		}
	}

	private static boolean check() {
		for (int i = 0; i < size; i++) {
			if (graph[0][i] != 0) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		try {
			int numberOfTestCases = Integer.parseInt(reader.readLine());
			for (int i = 0; i < numberOfTestCases; i++) {
				String line;
				String[] list;
				line = reader.readLine();
				list = line.split("\\s");
				n = Integer.parseInt(list[0]);
				m = Integer.parseInt(list[1]);
				size = 1; // source node

				difficultyMap = new HashMap<String, Integer>(m);
				topicMap = new HashMap<String, Integer>(m);
				difficultyIndex = new HashMap<String, Integer>(m);
				topicIndex = new HashMap<String, Integer>(m);
				getConstraint(difficultyMap, difficultyIndex);
				getConstraint(topicMap, topicIndex);
				size++; // target node

				graph = new int[size][size];
				for (String key : difficultyIndex.keySet()) {
					graph[0][difficultyIndex.get(key)] = difficultyMap.get(key);
				}
				for (String key : topicIndex.keySet()) {
					graph[topicIndex.get(key)][size - 1] = topicMap.get(key);
				}
				getQuestion();
				networkFlow();
				if (check()) {
					System.out.println("Yes");
				} else {
					System.out.println("No");
				}
			}
		} catch (Exception e) {
			System.exit(0);
		}
	}

}
