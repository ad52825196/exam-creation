import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

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
			}
		} catch (Exception e) {
			System.exit(0);
		}
	}

}
