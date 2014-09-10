import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

public class Assn1 {
	static Graph g;
	static String source = "Alice";
	static String destination = "Noah";
	static double cost[];
	static int parent[];
	static Hashtable<String, Integer> names;
	static LinkedList<String> namesNodes;
	static boolean visited[];

	public static void main(String s[]) throws FileNotFoundException,
			UnsupportedEncodingException {
		String file = "social-network.txt";
		PrintWriter writer = new PrintWriter("breadth-first.result.txt",
				"UTF-8");
		PrintWriter writer1 = new PrintWriter("depth-first.result.txt", "UTF-8");
		PrintWriter writer2 = new PrintWriter("uniform-cost.result.txt",
				"UTF-8");

		// Implementing the search techniques and reading and writing files.
		readFile(file);
		System.out.println("Initializing Uninformed Search");
		System.out.println("Implementing Breadth-First Search...");
		writer.write("Breadth:");
		writer.println();
		
		//Call to method that implements breadth first search
		String breadth = bfs();
		writer.write(breadth);
		writer.close();
		System.out.println("Implementing Depth-First Search");
		writer1.write("Depth:");
		writer1.println();
		
		//Call to method that implements depth first search
		String depth = dfs();
		writer1.write(depth);
		writer1.close();
		System.out.println("Implementing Uniform-Cost Search");
		writer2.write("Uniform-Cost:");
		writer2.println();
		
		////Call to method that implements uniform cost search
		String uniform = uc();
		writer2.write(uniform);
		writer2.close();
		System.out.println("Your ouput is ready in text files.");
	}

	public static void readFile(String file) {
		try {
			//Reading input file and storing data in data strutures like Graph, Node, Edge
			InputStream ips = new FileInputStream(file);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			g = new Graph();
			int i = 0;
			String line = br.readLine();
			namesNodes = new LinkedList<String>();
			while (!line.contains("0") && line != null) {
				namesNodes.add(line);
				i = g.addNode(line);
				line = br.readLine();
			}
			cost = new double[i + 1];
			parent = new int[i + 1];
			names = new Hashtable<String, Integer>();
			visited = new boolean[i + 1];
			for (int j = 0; j < i + 1; j++) {
				names.put(g.nodes.get(j), j);
			}

			for (int j = 0; j < i + 1; j++)
				cost[j] = Double.POSITIVE_INFINITY;
			String a[] = new String[i + 1];
			for (int j = 0; j < i + 1; j++) {
				a = line.split(" ");
				for (int k = 0; k < i; k++) {
					if (Integer.parseInt(a[k]) != 0)
						g.addEdge(new Edge(g.getNode(j), g.getNode(k), Double
								.parseDouble(a[k])));
				}
				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String bfs() {
		for (int i = 0; i < cost.length; i++) {
			cost[i] = Double.POSITIVE_INFINITY;
			parent[i] = 0;
			visited[i] = false;
		}
		
		//Queue for BFS implenmentation
		List<String> queue = new ArrayList<String>();
		queue.add(source);
		for (int j = 0; j < cost.length; j++)
			if (g.nodes.get(j).equalsIgnoreCase(source)) {
				cost[j] = 0;
				parent[j] = 0;
			}
		while (!queue.isEmpty()) {
			String t = queue.remove(0);
			visited[names.get(t)] = true;
			String child = destination, string = destination;
			if (t.equalsIgnoreCase(destination)) {
				//Preparing output
				while (parent[names.get(child)] != 0) {
					child = namesNodes.get(parent[names.get(child)]);
					string = child + "-" + string;
				}
				string = source + "-" + string;
				return string;
			} else {
				//Adding all adjacent nodes to queue
				LinkedList<String> adjNodes = g.getAdjacentNodes(t);
				String adjNodes1[] = new String[adjNodes.size()];
				for (int i = 0; i < adjNodes.size(); i++)
					adjNodes1[i] = adjNodes.get(i);

				for (int i = 0; i < adjNodes.size() - 1; i++)
					for (int j = 1; j < adjNodes.size(); j++)
						if (adjNodes1[i].compareTo(adjNodes1[j]) < 0) {
							String temp = adjNodes1[i];
							adjNodes1[i] = adjNodes1[j];
							adjNodes1[j] = temp;
						}
				for (int i = adjNodes1.length - 1; i >= 0; i--) {
					//If a node is visited, it should not be explored again.
					if (visited[names.get(adjNodes1[i])] == false) {
						visited[names.get(adjNodes1[i])] = true;
						queue.add(adjNodes1[i]);
						parent[names.get(adjNodes1[i])] = names.get(t);
						if (parent[names.get(adjNodes1[i])] != -1)
							cost[names.get(adjNodes1[i])] = g.getWeight(t,
									adjNodes1[i])
									+ cost[parent[names.get(adjNodes1[i])]];
						else
							cost[names.get(adjNodes1[i])] = g.getWeight(t,
									adjNodes1[i]);
					}
				}
			}
		}

		return "";
	}

	private static String dfs() {
		//Stack for DFS implementation
		Stack<String> stack = new Stack<String>();
		stack.push(source);
		for (int i = 0; i < cost.length; i++) {
			cost[i] = Double.POSITIVE_INFINITY;
			parent[i] = 0;
			visited[i] = false;
		}
		cost[names.get(source)] = 0;
		visited[names.get(source)] = true;
		while (!stack.isEmpty()) {
			String t = stack.pop();
			visited[names.get(t)] = true;
			String child = destination, string = destination;
			if (t.equalsIgnoreCase(destination)) {
				//Writing output
				while (parent[names.get(child)] != 0) {
					child = namesNodes.get(parent[names.get(child)]);
					string = child + "-" + string;
				}
				string = source + "-" + string;
				return string;
			} else {
				LinkedList<String> adjNodes = g.getAdjacentNodes(t);
				String adjNodes1[] = new String[adjNodes.size()];
				for (int i = 0; i < adjNodes.size(); i++)
					adjNodes1[i] = adjNodes.get(i);

				for (int i = 0; i < adjNodes.size() - 1; i++)
					for (int j = 1; j < adjNodes.size(); j++)
						if (adjNodes1[i].compareTo(adjNodes1[j]) < 0) {
							String temp = adjNodes1[i];
							adjNodes1[i] = adjNodes1[j];
							adjNodes1[j] = temp;
						}
				for (int i = 0; i < adjNodes1.length; i++) {
					//If node visited, need not be visited again
					if (visited[names.get(adjNodes1[i])] == false) {
						stack.push(adjNodes1[i]);
						parent[names.get(adjNodes1[i])] = names.get(t);
						if (parent[names.get(adjNodes1[i])] != -1)
							cost[names.get(adjNodes1[i])] = g.getWeight(t,
									adjNodes1[i])
									+ cost[parent[names.get(adjNodes1[i])]];
						else
							cost[names.get(adjNodes1[i])] = g.getWeight(t,
									adjNodes1[i]);
					}
				}
			}
		}
		return "";
	}

	private static String uc() {
		//Queue for uniform cost implementation
		List<String> queue = new ArrayList<String>();
		for (int i = 0; i < cost.length; i++) {
			cost[i] = Double.POSITIVE_INFINITY;
			parent[i] = 0;
			visited[i] = false;
		}
		queue.add(source);
		for (int j = 0; j < cost.length; j++)
			if (g.nodes.get(j).equalsIgnoreCase(source)) {
				cost[j] = 0;
				parent[j] = -1;
			}
		while (!queue.isEmpty()) {
			String t = queue.remove(0);
			visited[names.get(t)] = true;
			String child = destination, string = destination;
			if (t.equalsIgnoreCase(destination)) {
				while (parent[names.get(child)] != 0) {
					//preparing output
					child = namesNodes.get(parent[names.get(child)]);
					string = child + "-" + string;
				}
				string = source + "-" + string;
				return string;
			} else {
				LinkedList<String> adjNodes = g.getAdjacentNodes(t);
				String adjNodes1[] = new String[adjNodes.size()];
				for (int i = 0; i < adjNodes.size(); i++)
					adjNodes1[i] = adjNodes.get(i);

				for (int i = 0; i < adjNodes.size() - 1; i++)
					for (int j = 1; j < adjNodes.size(); j++)
						if (g.getWeight(t, adjNodes1[i]) > g.getWeight(t,
								adjNodes1[j])) {
							String temp = adjNodes1[i];
							adjNodes1[i] = adjNodes1[j];
							adjNodes1[j] = temp;
						}

				for (int i = 0; i < adjNodes1.length; i++) {
					//visited node should not be visited gaian
					if (visited[names.get(adjNodes1[i])] == false) {
						visited[names.get(adjNodes1[i])] = true;
						queue.add(adjNodes1[i]);
						parent[names.get(adjNodes1[i])] = names.get(t);
						if (parent[names.get(adjNodes1[i])] != -1)
							cost[names.get(adjNodes1[i])] = g.getWeight(t,
									adjNodes1[i])
									+ cost[parent[names.get(adjNodes1[i])]];
						else
							cost[names.get(adjNodes1[i])] = g.getWeight(t,
									adjNodes1[i]);
					}
				}
			}
		}

		return "";
	}
}

class Node {
	public String data; // data element
	public boolean visited = false; // flag to track the already visited node
	public List<Node> adjacentNodes = new LinkedList<Node>(); // adjacency list

	public Node(String data) {
		this.data = data;
	}

	public void addAdjacentNode(final Node node) {
		adjacentNodes.add(node);
		node.adjacentNodes.add(this);
	}
}

class Graph {
	//graph stores all the nodes and edges in vectors
	protected Vector<String> nodes = new Vector<String>();
	protected Vector<Edge> edges = new Vector<Edge>();

	public String getNode(int i) {
		return nodes.get(i);
	}

	//get the adjacent nodes of a particular node
	public LinkedList<String> getAdjacentNodes(String t) {
		Set<String> adjNodes = new TreeSet<String>();
		LinkedList<String> adjNodes1 = new LinkedList<String>();
		for (Edge a : edges)
			if (a.getNodes(t) != null && !adjNodes.contains(a.getNodes(t)))
				adjNodes.add(a.getNodes(t));
		Iterator<String> arrr = adjNodes.iterator();
		while (arrr.hasNext()) {
			adjNodes1.add(arrr.next());
		}
		return adjNodes1;
	}

	//get cost of traversing from 1 node to other
	public double getWeight(String x, String y) {
		Edge edgeW = new Edge();
		for (Edge edge : edges) {
			if (edge.a.equalsIgnoreCase(x) && edge.b.equalsIgnoreCase(y)
					|| edge.a.equalsIgnoreCase(y) && edge.b.equalsIgnoreCase(x)) {
				edgeW = new Edge(x, y);
				edgeW.weight = edge.weight;
				break;
			}
		}
		return edgeW.weight;
	}

	public int addNode(String a) {
		nodes.add(a);
		return nodes.size() - 1;
	}

	public void addEdge(Edge a) {
		edges.add(a);
	}
}

class Edge {
	//holds the cost between 2 nodes
	protected String a, b;
	protected double weight;

	//initially set to positive infinity
	public Edge(String a, String b) {
		this(a, b, Double.POSITIVE_INFINITY);
	}

	public Edge(String a, String b, Double weight) {
		this.a = a;
		this.b = b;
		this.weight = weight;
	}

	public Edge() {
		// TODO Auto-generated constructor stub
	}

	//returns cost of edge
	public double getWeight() {
		return weight;
	}

	//get the adjacent node of an edge and node
	public String getNodes(String a) {
		if (this.a.equalsIgnoreCase(a))
			return this.b;
		else if (this.b.equalsIgnoreCase(a))
			return this.a;
		else
			return null;
	}
}