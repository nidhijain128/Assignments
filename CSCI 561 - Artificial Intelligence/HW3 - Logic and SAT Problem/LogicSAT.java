import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

public class LogicSAT {
	// m is the number of tables and n is the number of guests
	static int m, n;
	// 2D array to maintain relationships between the guests
	static int guestDetails[][];
	static List<Clause> clauses = new LinkedList<Clause>();

	public static void main(String[] args) throws FileNotFoundException,
			UnsupportedEncodingException {
		// read the input file
		readFile(args[0]);
		tableConstraint();
		friendConstraint();
		enemyConstraint();
		printClauses();
		LogicSATPLResolution pl = new LogicSATPLResolution();
		boolean isResolved = pl.isResolved(clauses);
		PrintWriter writer = new PrintWriter(args[1], "UTF-8");
		if (isResolved) {
			System.out.println("The arrangement is satisfiable");
			writer.write("1");
		} else {
			System.out.println("The arrangement is not satisfiable");
			writer.write("0");
		}
		writer.close();
	}

	private static void printClauses() {
		System.out.println("List of Clauses generated:");
		for (Clause c : clauses)
			System.out.println(c.toString());
	}

	public static void readFile(String file) {
		try {
			// Reading input file and storing data in 2D array
			InputStream ips = new FileInputStream(file);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line = br.readLine();
			int i = 0;
			while (line != null) {
				String temp[] = line.split(" ");
				int values[] = new int[temp.length];
				for (int j = 0; j < values.length; j++)
					values[j] = Integer.parseInt(temp[j]);
				if (i == 0) {
					n = values[0];
					m = values[1];
					guestDetails = new int[m + 1][m + 1];
				} else {
					for (int j = 0; j < values.length; j++)
						guestDetails[i][j + 1] = values[j];
				}
				i++;
				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void tableConstraint() {
		Clause c;
		for (int i = 1; i <= m; i++) {
			c = new Clause();
			for (int j = 1; j <= n; j++) {
				SubClause sc = new SubClause(i, j, false);
				c.addClause(sc);
			}
			clauses.add(c);
			for (int x = 1; x <= n; x++) {
				for (int y = x + 1; y <= n; y++) {
					c = new Clause();
					SubClause sc1 = new SubClause(i, x, true);
					SubClause sc2 = new SubClause(i, y, true);
					c.addClause(sc1);
					c.addClause(sc2);
					clauses.add(c);
				}
			}
		}
	}

	public static void friendConstraint() {
		for (int i = 1; i <= m; i++)
			for (int j = i + 1; j <= m; j++)
				if (guestDetails[i][j] == 1)
					for (int k = 1; k <= n; k++) {
						SubClause sc1 = new SubClause(i, k, true);
						SubClause sc2 = new SubClause(j, k, false);
						Clause c1 = new Clause();
						c1.addClause(sc1);
						c1.addClause(sc2);
						SubClause sc3 = new SubClause(j, k, true);
						SubClause sc4 = new SubClause(i, k, false);
						Clause c2 = new Clause();
						c2.addClause(sc3);
						c2.addClause(sc4);
						clauses.add(c1);
						clauses.add(c2);
					}
	}

	public static void enemyConstraint() {
		for (int i = 1; i <= m; i++)
			for (int j = i + 1; j <= m; j++)
				if (guestDetails[i][j] == -1)
					for (int k = 1; k <= n; k++) {
						SubClause sc1 = new SubClause(i, k, true);
						SubClause sc2 = new SubClause(j, k, true);
						Clause c1 = new Clause();
						c1.addClause(sc1);
						c1.addClause(sc2);
						clauses.add(c1);
					}
	}
}
