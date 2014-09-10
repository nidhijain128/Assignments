import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class ClassConnect4 {

	// Constants
	private static final String INPUTFILENOTPRESENT = "Specified input file not present. Program exited!!!";
	private static final String ERRORREADINGFILE = "Error occured while reading file. Program terminated!!!";
	private static final int ROWLENGTH = 6;
	private static final int COLUMNLENGTH = 7;
	private static final int INFINITY = 2147483647;
	
	private static HashMap<Integer, Integer> indexToAlphaMap=new HashMap<Integer, Integer>();
	private static Queue<BoardConnect4> childrenNodes = new LinkedList<BoardConnect4>();
	static int totalCount = 0, maxDepth = 4;

	public static void main(String[] args) {
		BoardConnect4 elementNextInQueue;
		int level = 0, maxIndex=-INFINITY,maxHeuristicValue=-INFINITY;;
		char[][] inputConfiguration = ReadFileAndGetInput(args[0]);
		BoardConnect4 gameInitialConfiguration = new BoardConnect4(
				inputConfiguration);
		gameInitialConfiguration.level = 0;
		gameInitialConfiguration.alpha = -INFINITY;
		gameInitialConfiguration.beta = INFINITY;
		gameInitialConfiguration.CountUnterminatedLines();
		childrenNodes.add(gameInitialConfiguration);
		while ((elementNextInQueue = childrenNodes.poll()) != null) {
			if (elementNextInQueue.level > level) {
				level = elementNextInQueue.level;
				if (level == maxDepth)
					break;
			}
			GenerateGameTree(elementNextInQueue, args[1]);
		}
		CalculateAlphaBeta(gameInitialConfiguration, maxDepth, -INFINITY,
				INFINITY, 'a');
		PrintTree(gameInitialConfiguration, args[1]);
		if(indexToAlphaMap.size()>0)
		{
			
			for(Integer index: indexToAlphaMap.keySet())
			{
				if(maxHeuristicValue<=indexToAlphaMap.get(index))
				{
					maxIndex=index+1;
					maxHeuristicValue=indexToAlphaMap.get(index);
				}
			}
		}
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(args[1], true)));
			out.println("first Move: " + maxIndex);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished...!!!");
	}

	private static void GenerateGameTree(BoardConnect4 root, String fileName) {
		int startPosition = 0, newOpenPosition[] = { -1, -1 };
		while ((newOpenPosition = FindOpenPosition(root.boardConfiguration,
				startPosition)) != null) {
			BoardConnect4 newChild = new BoardConnect4(root.boardConfiguration);
			newChild.level = root.level + 1;
			if(newChild.level==1)
			{
				indexToAlphaMap.put(newOpenPosition[1], 0);
				newChild.indexA1=newOpenPosition[1];
			}
			else
				newChild.indexA1=root.indexA1;
			if (root.level % 2 == 0)
				newChild.boardConfiguration[newOpenPosition[0]][newOpenPosition[1]] = 'a';
			else if (root.level % 2 == 1)
				newChild.boardConfiguration[newOpenPosition[0]][newOpenPosition[1]] = 'b';
			root.childNodes.add(newChild);
			totalCount++;
			if (newChild.CheckIfWon(newChild.level)) {
				if (newChild.level % 2 == 1)
					newChild.heuristicValue = 1000;
				else
					newChild.heuristicValue = -1000;
			}
			startPosition = newOpenPosition[1] + 1;
		}
		childrenNodes.addAll(root.childNodes);
	}

	private static int[] FindOpenPosition(char[][] root, int startPosition) {
		int foundPosition[] = { -1, -1 };
		for (int i = startPosition; i < COLUMNLENGTH; i++)
			for (int j = ROWLENGTH - 1; j >= 0; j--)
				if (root[j][i] == 'e') {
					foundPosition[0] = j;
					foundPosition[1] = i;
					return foundPosition;
				}
		return null;
	}

	private static char[][] ReadFileAndGetInput(String fileName) {
		String line = "";
		int columnIndex = 0, rowIndex = 0, counter = 0;
		char[][] input = new char[ROWLENGTH][COLUMNLENGTH];
		BufferedReader bufferedReader;
		try {
			InputStream fileInputStream = new FileInputStream(fileName);
			bufferedReader = new BufferedReader(new InputStreamReader(
					fileInputStream, Charset.forName("UTF-8")));
			if (bufferedReader != null) {
				while ((line = bufferedReader.readLine()) != null) {
					char[] lineChar = line.toCharArray();
					for (rowIndex = 5, counter = 0; counter < lineChar.length; rowIndex--) {
						input[rowIndex][columnIndex] = lineChar[counter++];
					}
					columnIndex++;
				}
				bufferedReader.close();
				return input;
			}

		} catch (FileNotFoundException e) {
			System.out.println(INPUTFILENOTPRESENT);
			System.exit(0);
		} catch (Exception e) {
			System.out.println(ERRORREADINGFILE);
			System.exit(0);
		}
		return input;
	}

	private static void PrintTree(BoardConnect4 initialConfiguration,
			String fileName) {
		int differentColumnIndex = -1;
		String lineToPrint = "";
		
		for (int i = 0; i < initialConfiguration.childNodes.size(); i++) {
			BoardConnect4 childBoard = initialConfiguration.childNodes.get(i);
			differentColumnIndex = Difference(
					initialConfiguration.boardConfiguration,
					childBoard.boardConfiguration);
			if (childBoard.level % 2 == 1)
				lineToPrint = "A";
			else if (childBoard.level % 2 == 0)
				lineToPrint = "B";
			lineToPrint += childBoard.level + ": " + (differentColumnIndex + 1);
			for (int j = 0; j < childBoard.level - 1; j++)
				lineToPrint = "|-" + lineToPrint;
			if (childBoard.level == maxDepth
					|| (childBoard.heuristicValue == 1000)) {
				lineToPrint += "; h=" + childBoard.heuristicValue;
			}
			if (childBoard.hasPrunedNodes
					&& differentColumnIndex + 1 != COLUMNLENGTH) {
				try {
					PrintWriter out = new PrintWriter(new BufferedWriter(
							new FileWriter(fileName, true)));
					out.println(lineToPrint);
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (childBoard.childNodes != null
						&& childBoard.childNodes.size() > 0
						&& childBoard.heuristicValue != 1000)
					PrintTree(childBoard, fileName);
				lineToPrint = lineToPrint.substring(0,
						lineToPrint.indexOf(":") + 1);
				String prunedNodes = GetListOfPrunedNodes(initialConfiguration,
						differentColumnIndex, i);
				if(childBoard.heuristicValue == 1000)
					childBoard.alpha = 1000;
				lineToPrint += " pruning " + prunedNodes + "; alpha="
						+ childBoard.alpha + ", beta=" + childBoard.beta;
				try {
					PrintWriter out = new PrintWriter(new BufferedWriter(
							new FileWriter(fileName, true)));
					out.println(lineToPrint);
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			try {
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new FileWriter(fileName, true)));
				out.println(lineToPrint);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (childBoard.heuristicValue != 1000)
				PrintTree(childBoard, fileName);
		}
	}

	private static String GetListOfPrunedNodes(BoardConnect4 parent,
			int differentColumnIndex, int index) {
		String prunedNodes = "";
		for (int i = index + 1; i < parent.childNodes.size(); i++) {
			BoardConnect4 child = parent.childNodes.get(i);
			for (int k = 0; k < ROWLENGTH; k++)
				for (int j = differentColumnIndex + 1; j < COLUMNLENGTH; j++)
					if (parent.boardConfiguration[k][j] == 'e'
							&& (child.boardConfiguration[k][j] == 'b' || child.boardConfiguration[k][j] == 'a'))
						prunedNodes += (j + 1) + ", ";
		}
		if (prunedNodes.length() > 0)
			return prunedNodes.substring(0, prunedNodes.length() - 2);
		else
			return "";
	}

	private static int Difference(char[][] parentConfiguration,
			char[][] childConfiguration) {
		for (int i = 0; i < ROWLENGTH; i++)
			for (int j = 0; j < COLUMNLENGTH; j++)
				if (parentConfiguration[i][j] == 'e'
						&& (childConfiguration[i][j] == 'b' || childConfiguration[i][j] == 'a'))
					return j;
		return -1;
	}

	private static int CalculateAlphaBeta(BoardConnect4 root, int depth,
			int alpha, int beta, char player) {
		if (root.childNodes.size() == 0 || depth <= 0) {
			if (root.CheckIfWon(player)) {
				if (player == 'a')
					root.heuristicValue = 1000;
				else
					root.heuristicValue = -1000;
			} else {
				root.CountUnterminatedLines();
				root.heuristicValue = (root.xA - root.xB)
						+ (5 * (root.yA - root.yB));
			}
			if(indexToAlphaMap.get(root.indexA1)<root.heuristicValue)
				indexToAlphaMap.put(root.indexA1, root.heuristicValue);
			return root.heuristicValue;
		}
		if (player == 'a') {
			for (BoardConnect4 child : root.childNodes) {
				alpha = Math.max(alpha,
						CalculateAlphaBeta(child, depth - 1, alpha, beta, 'b'));
				if (beta <= alpha) {
					child.hasPrunedNodes = true;
					child.alpha = alpha;
					child.beta = beta;
					break;
				}
			}
			return alpha;
		} else {
			for (BoardConnect4 child : root.childNodes) {
				beta = Math.min(beta,
						CalculateAlphaBeta(child, depth - 1, alpha, beta, 'a'));
				if (beta <= alpha) {
					child.hasPrunedNodes = true;
					child.alpha = alpha;
					child.beta = beta;
					break;
				}
			}
			return beta;
		}
	}
}