import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;
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
	private static int[][] residualGraph;

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
				residualGraph[difficultyIndex.get(difficulty)][topicIndex.get(topic)]++;
			}
		}
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
				residualGraph = new int[size][size];
				for (String key : difficultyIndex.keySet()) {
					graph[0][difficultyIndex.get(key)] = difficultyMap.get(key);
					residualGraph[0][difficultyIndex.get(key)] = difficultyMap.get(key);
				}
				for (String key : topicIndex.keySet()) {
					graph[topicIndex.get(key)][size - 1] = topicMap.get(key);
					residualGraph[topicIndex.get(key)][size - 1] = topicMap.get(key);
				}
				getQuestion();
			}
		} catch (Exception e) {
			System.exit(0);
		}
	}

}
