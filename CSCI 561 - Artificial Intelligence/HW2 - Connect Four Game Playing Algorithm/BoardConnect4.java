import java.util.ArrayList;

public class BoardConnect4 {

	final int ROWLENGTH = 6;
	final int COLUMNLENGTH = 7;
	char[][] boardConfiguration = new char[ROWLENGTH][COLUMNLENGTH];
	ArrayList<BoardConnect4> childNodes;
	int level = 0, xA = 0, xB = 0, yA = 0, yB = 0, alpha = 0, beta = 0,
			heuristicValue = 0,indexA1=0;
	boolean hasPrunedNodes = false;
	
	public BoardConnect4(char[][] startConfiguration) {
		super();
		for (int i = 0; i < ROWLENGTH; i++)
			for (int j = 0; j < COLUMNLENGTH; j++)
				if (startConfiguration[i][j] == 'a'
						|| startConfiguration[i][j] == 'b')
					boardConfiguration[i][j] = startConfiguration[i][j];
				else
					boardConfiguration[i][j] = 'e';
		this.childNodes = new ArrayList<BoardConnect4>();
	}

	public void CountUnterminatedLines() {
		this.xA += CountHorizontal(2, 'a');
		this.xA += CountVertical(2, 'a');
		this.xA += CountDiagonal(2, 'a');

		this.yA += CountHorizontal(3, 'a');
		this.yA += CountVertical(3, 'a');
		this.yA += CountDiagonal(3, 'a');

		this.xB += CountHorizontal(2, 'b');
		this.xB += CountVertical(2, 'b');
		this.xB += CountDiagonal(2, 'b');

		this.yB += CountHorizontal(3, 'b');
		this.yB += CountVertical(3, 'b');
		this.yB += CountDiagonal(3, 'b');
		
		this.xA -= this.yA;
		this.xB -= this.yB;
	}

	private int CountHorizontal(int length, char player) {
		int countOfUnterminatedLines = 0;
		for (int i = 0; i < ROWLENGTH; i++)
			for (int j = 0; j < COLUMNLENGTH; j++) {
				if (this.boardConfiguration[i][j] == player) {
					if (length == 2) {
						if ((j - 1 >= 0
								&& this.boardConfiguration[i][j - 1] == 'e'
								&& j + 1 < COLUMNLENGTH && this.boardConfiguration[i][j + 1] == player)
								|| (j - 1 >= 0
										&& this.boardConfiguration[i][j - 1] == player
										&& j + 1 < COLUMNLENGTH && this.boardConfiguration[i][j + 1] == 'e'))
							countOfUnterminatedLines++;
					} else if (length == 3) {
						if ((j - 1 >= 0
								&& this.boardConfiguration[i][j - 1] == 'e'
								&& j + 1 < COLUMNLENGTH
								&& this.boardConfiguration[i][j + 1] == player
								&& j + 2 < COLUMNLENGTH && this.boardConfiguration[i][j + 2] == player)
								|| (j + 1 < COLUMNLENGTH
										&& this.boardConfiguration[i][j + 1] == 'e'
										&& j - 1 >= 0
										&& this.boardConfiguration[i][j - 1] == player
										&& j - 2 >= 0 && this.boardConfiguration[i][j - 2] == player))
							countOfUnterminatedLines++;
					}
				}
			}
		return countOfUnterminatedLines;
	}

	private int CountVertical(int length, char player) {
		int countOfUnterminatedLines = 0;
		for (int i = 0; i < ROWLENGTH; i++)
			for (int j = 0; j < COLUMNLENGTH; j++) {
				if (this.boardConfiguration[i][j] == player) {
					if (length == 2) {
						if ((i - 1 >= 0
								&& this.boardConfiguration[i - 1][j] == 'e'
								&& i + 1 < ROWLENGTH && this.boardConfiguration[i + 1][j] == player)
								|| (i - 1 >= 0
										&& this.boardConfiguration[i - 1][j] == player
										&& i + 1 < ROWLENGTH && this.boardConfiguration[i + 1][j] == 'e'))
							countOfUnterminatedLines++;
					} else if (length == 3) {
						if ((i - 1 >= 0
								&& this.boardConfiguration[i - 1][j] == 'e'
								&& i + 1 < ROWLENGTH
								&& this.boardConfiguration[i + 1][j] == player
								&& i + 2 < ROWLENGTH && this.boardConfiguration[i + 2][j] == player)
								|| (i + 1 < ROWLENGTH
										&& this.boardConfiguration[i + 1][j] == 'e'
										&& i - 1 >= 0
										&& this.boardConfiguration[i - 1][j] == player
										&& i - 2 >= 0 && this.boardConfiguration[i - 2][j] == player))
							countOfUnterminatedLines++;
					}
				}
			}
		return countOfUnterminatedLines;
	}

	private int CountDiagonal(int length, char player) {
		int countOfUnterminatedLines = 0;
		for (int i = 0; i < ROWLENGTH; i++)
			for (int j = 0; j < COLUMNLENGTH; j++) {
				if (this.boardConfiguration[i][j] == player) {
					if (length == 2) {
						if (i + 1 < ROWLENGTH
								&& j - 1 >= 0
								&& this.boardConfiguration[i + 1][j - 1] == 'e'
								&& i - 1 >= 0
								&& j + 1 < COLUMNLENGTH
								&& this.boardConfiguration[i - 1][j + 1] == player)
							countOfUnterminatedLines++;
						if (i + 1 < ROWLENGTH
								&& j + 1 < COLUMNLENGTH
								&& this.boardConfiguration[i + 1][j + 1] == 'e'
								&& i - 1 >= 0
								&& j - 1 >= 0
								&& this.boardConfiguration[i - 1][j - 1] == player)
							countOfUnterminatedLines++;
						if (i - 1 >= 0
								&& j + 1 < COLUMNLENGTH
								&& this.boardConfiguration[i - 1][j + 1] == 'e'
								&& i + 1 < ROWLENGTH
								&& j - 1 >= 0
								&& this.boardConfiguration[i + 1][j - 1] == player)
							countOfUnterminatedLines++;
						if (i - 1 >= 0
								&& j - 1 >= 0
								&& this.boardConfiguration[i - 1][j - 1] == 'e'
								&& i + 1 < ROWLENGTH
								&& j + 1 < COLUMNLENGTH
								&& this.boardConfiguration[i + 1][j + 1] == player)
							countOfUnterminatedLines++;
					} else if (length == 3) {
						if (i + 1 < ROWLENGTH
								&& j - 1 >= 0
								&& this.boardConfiguration[i + 1][j - 1] == 'e'
								&& i - 1 >= 0
								&& j + 1 < COLUMNLENGTH
								&& this.boardConfiguration[i - 1][j + 1] == player
								&& i - 2 >= 0
								&& j + 2 < COLUMNLENGTH
								&& this.boardConfiguration[i - 2][j + 2] == player)
							countOfUnterminatedLines++;
						if (i + 1 < ROWLENGTH
								&& j + 1 < COLUMNLENGTH
								&& this.boardConfiguration[i + 1][j + 1] == 'e'
								&& i - 1 >= 0
								&& j - 1 >= 0
								&& this.boardConfiguration[i - 1][j - 1] == player
								&& i - 2 >= 0
								&& j - 2 >= 0
								&& this.boardConfiguration[i - 2][j - 2] == player)
							countOfUnterminatedLines++;
						if (i - 1 >= 0
								&& j + 1 < COLUMNLENGTH
								&& this.boardConfiguration[i - 1][j + 1] == 'e'
								&& i + 1 < ROWLENGTH
								&& j - 1 >= 0
								&& this.boardConfiguration[i + 1][j - 1] == player
								&& i + 2 < ROWLENGTH
								&& j - 2 >= 0
								&& this.boardConfiguration[i + 2][j - 2] == player)
							countOfUnterminatedLines++;
						if (i - 1 >= 0
								&& j - 1 >= 0
								&& this.boardConfiguration[i - 1][j - 1] == 'e'
								&& i + 1 < ROWLENGTH
								&& j + 1 < COLUMNLENGTH
								&& this.boardConfiguration[i + 1][j + 1] == player
								&& i + 2 < ROWLENGTH
								&& j + 2 < COLUMNLENGTH
								&& this.boardConfiguration[i + 2][j + 2] == player)
							countOfUnterminatedLines++;
					}
				}
			}
		return countOfUnterminatedLines;
	}

	public boolean CheckIfWon(int level) 
	{
		char whoseTurn=' ';
		if(level%2==1)
			whoseTurn='a';
		else
			whoseTurn='b';
		for(int i=0;i<ROWLENGTH;i++)
			for(int j=0;j<COLUMNLENGTH;j++)
				if(this.boardConfiguration[i][j]==whoseTurn)
				{
					if(j+3<COLUMNLENGTH && this.boardConfiguration[i][j+1]==whoseTurn && this.boardConfiguration[i][j+2]==whoseTurn && this.boardConfiguration[i][j+3]==whoseTurn)
						return true;
					if(i+3<ROWLENGTH && this.boardConfiguration[i+1][j]==whoseTurn && this.boardConfiguration[i+2][j]==whoseTurn && this.boardConfiguration[i+3][j]==whoseTurn)
						return true;
					if(i+3<ROWLENGTH && j+3<COLUMNLENGTH && this.boardConfiguration[i+1][j+1]==whoseTurn && this.boardConfiguration[i+2][j+2]==whoseTurn && this.boardConfiguration[i+3][j+3]==whoseTurn)
						return true;
					if(i+3<ROWLENGTH && j-3>=0 && this.boardConfiguration[i+1][j-1]==whoseTurn && this.boardConfiguration[i+2][j-2]==whoseTurn && this.boardConfiguration[i+3][j-3]==whoseTurn)
						return true;
				}
		return false;
	}	
}
