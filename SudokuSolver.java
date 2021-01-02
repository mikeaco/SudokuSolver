import java.util.*;
public class SudokuSolver {
	public static char[][] board;
	public static char[][] originalBoard = new char[9][9];
	public static ArrayList<int[]> animate;
	public SudokuSolver() {
		randomBoard();
		copyBoard(originalBoard,board);
		animate = new ArrayList<>();
	}
	public SudokuSolver(char[][] b) {
		board = b;
		copyBoard(originalBoard,b);
		animate = new ArrayList<>();
	}
	private void copyBoard(char[][] b, char[][] original) {
		for(int r = 0 ; r < original.length ; r++) {
			for(int c = 0 ; c < original[r].length ; c++) {
				b[r][c]=original[r][c];
			}
		}
	}
	public char[][] getBoard() {
		return board;
	}
	public void setBoard(char[][] b) {
		board = b;
		copyBoard(originalBoard,b);
		
	}
	public ArrayList<int[]> getAnim() {
		return animate;
	}
	public void setAnim(ArrayList<int[]> anim) {
		animate = anim;
		
	}
	public boolean validBoard(char[][] board) {
		if(getSolvedBoard()[0][0]=='u') {
			return false;
		}
		else {
			return true;
		}
	}

	public void printBoard() {
		if (board[0][0] == 'u') {
			System.out.println("NO SOLUTION");
		} else {
			for (int r = 0; r < board.length; r++) {
				if ((r) % 3 == 0 && r != 0) {
					System.out.print("\n- - - - - - - - - - -");
				}
				System.out.println("");
				for (int c = 0; c < board[r].length; c++) {
					if (c % 3 == 0 && c != 0) {
						System.out.print("| ");
					}
					System.out.print(board[r][c] + " ");
				}

			}
		}
	}
	public char[][] getSolvedBoard(){
		char[][] solved = new char[9][9];
		for(int r = 0 ; r < solved.length ; r++) {
			for(int c = 0 ; c < solved[r].length ; c++) {
				solved[r][c] = board[r][c];
			}
		}
		solveSudoku(solved,new ArrayList<>());
		return solved;	
	}
	public void resetBoard() {
		copyBoard(board,originalBoard);
	}
	public void solveSudoku(char[][] board, ArrayList<int[]> anim) {
		char[][] change = new char[9][9];
		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board[r].length; c++) {
				if (board[r][c] == '.') {
					int i = 1;
					while (i < 10) {
						if (check(i, r, c, board)) {
							board[r][c] = (char) (i + '0');
							anim.add(new int[]{i,0,r,c});
							break;
						}
						anim.add(new int[]{i,1,r,c});
						i++;
						if (i == 10) {
							if (c - 1 < 0) {
								r--;
								c = change[r].length - 1;
							} else {
								c--;
							}
							backtrack(board, r, c, change,anim);
							if (board[0][0] == 'u') {

								return;
							}
							// System.out.println("hello");

						}
					}

				} else {
					change[r][c] = '-';
				}
			}
		}
	}

	public void backtrack(char[][] board, int r, int c, char[][] change, ArrayList<int[]> anim) {
		if (board[0][0] == 'u') {
			return;
		}
		while (change[r][c] == '-') {

			if (c - 1 < 0) {
				if (r - 1 < 0) {

					board[0][0] = 'u';

					return;
				}
				r--;
				c = change[r].length - 1;
			} else {
				c--;
			}
		}

		int i = (board[r][c] == '.') ? 0 : Character.getNumericValue(board[r][c]);
		while (i < 10) {
			i++;
			if (check(i, r, c, board) && i < 10) {
				board[r][c] = (char) (i + '0');
				anim.add(new int[]{i,0,r,c});
				break;
			}
			else if(i<10) {
				anim.add(new int[]{i,1,r,c});
			}
			if (i == 10) {
				int temp1 = r;
				int temp2 = c;
				board[r][c] = '.';
				if (c - 1 < 0) {
					r--;
					c = change[r].length - 1;
				} else {
					c--;
				}
				backtrack(board, r, c, change,anim);
				if (board[0][0] == 'u') {
					return;
				}
				r = temp1;
				c = temp2;
				i = 0;
			}
		}

	}

	public boolean check(int num, int i, int j, char[][] board) {
		for (int x = 0; x < board[i].length; x++) {
			if (Character.getNumericValue(board[i][x]) == num) {
				return false;
			}
		}
		for (int x = 0; x < board.length; x++) {
			if (Character.getNumericValue(board[x][j]) == num) {
				return false;
			}
		}
		int r = i;
		int c = j;
		while (r >= 3)
			r -= 3;
		while (c >= 3)
			c -= 3;
		r = i - r;
		c = j - c;
		for (int x = r; x < r + 3; x++) {
			for (int y = c; y < c + 3; y++) {
				if (Character.getNumericValue(board[x][y]) == num)
					return false;
			}
		}
		return true;

	}
	public void randomBoard() {
		char[][] newBoard = new char[9][9];
		for(int r = 0 ; r < newBoard.length ; r++) {
			for(int c = 0 ; c < newBoard[r].length ; c++ ){
				int count = 0 ;
				while(count < 9) {
					int i = (int)(Math.random()*9)+1;
					if (check(i, r, c, newBoard)) {
						newBoard[r][c] = (char) (i + '0');
						break;
					}
					if(i+1>9) {
						i=0;
					}
					i++;
					count++;
					if(count==9) {
						if (c - 1 < 0) {
							r--;
							c = newBoard[r].length - 1;
						} else {
							c--;
						}
						backtrack(newBoard, r, c, new char[9][9],new ArrayList<>());
					}
					
				}
				
			}
		}
		for(int r = 0 ; r < newBoard.length ; r++) {
			for(int c = 0 ; c < newBoard[r].length ; c++ ){
			    if((int)(Math.random()*2)+1==1){
			        newBoard[r][c] = '.';
			    }
			}
		} 
		board = newBoard;
		copyBoard(originalBoard,board);
		
	}
}
