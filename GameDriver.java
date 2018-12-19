import java.util.Scanner;
import java.time.LocalTime;
import java.lang.Character;
import java.util.Random;
import java.lang.Math;

public class GameDriver{
	
	public static void main (String[] args){
		
		boolean gameOver = false;
		boolean isPlayerTurn = false;
		
		GameBoard gBoard = new GameBoard();
		gBoard.boardToString();
		
		Scanner k = new Scanner(System.in);
		System.out.print("Would you like to go first? (y/n): ");
		String str = k.next();
		System.out.println("");
		
		// First move
		if(str.charAt(0) == 'y'){
			
			// Opponent goes first
			isPlayerTurn = true;
			System.out.print("Make your move: ");
			str = k.next();
			char r = str.charAt(0);
			char c = str.charAt(1);
			int row = Character.getNumericValue(r);
			int col = Character.getNumericValue(c);
			gBoard.setBoard(row-1,col-1,isPlayerTurn);
			gBoard.boardToString();
			System.out.println("Player Move: " + str);
			isPlayerTurn = false;
		}else{
			
			// AI goes first
			isPlayerTurn = false;
			Random r = new Random();
			int row = r.nextInt(8);
			int col = r.nextInt(8);
			gBoard.setBoard(row,col,isPlayerTurn);
			gBoard.boardToString();
			System.out.println("Computer Move: " + (row+1)+""+(col+1));
			isPlayerTurn = true;
		}
		
		// Rest of the moves are conducted here
		while(gameOver == false){
			if(isPlayerTurn == true){
				boolean isValid = false;
				System.out.print("Make your move: ");
				str = k.next();
				char r = str.charAt(0);
				char c = str.charAt(1);
				int row = Character.getNumericValue(r);
				int col = Character.getNumericValue(c);
				while(isValid == false){
					if(gBoard.validMove(row-1,col-1) == false){
						System.out.println("Not Valid move try again: ");
						str = k.next();
						r = str.charAt(0);
						c = str.charAt(1);
						row = Character.getNumericValue(r);
						col = Character.getNumericValue(c);
					} else {
						isValid = true;
					}
				}
				gBoard.setBoard(row-1,col-1,isPlayerTurn);
				gBoard.boardToString();
				
				System.out.println("Player Move: " + str);
				
				isPlayerTurn = false;
			} else {
				LocalTime start = LocalTime.now();
				LocalTime end = start.plusSeconds((long)30);
				
				int[] move = bestMove(gBoard, end, str);
				
				int row = move[0];
				int col = move[1];
				gBoard.setBoard(row,col,isPlayerTurn);
				gBoard.boardToString();
				
				System.out.println("Computer Move: " + (row+1) +""+(col+1) );
				
				isPlayerTurn = true;
			}
			if(isOver(gBoard) == true){
				gameOver = true;
			}
		}
		System.out.println("Game Over!!!!!!!!");
		gBoard.boardToString();
	}
	
	private static int[] bestMove(GameBoard board, LocalTime endTime, String oppMove){
		int[] bestMove = new int[2];
		int max = 10000;
		int min = -10000;
		int bestVal = -10000;
		int bestRow = -1;
		int bestCol = -1;
		char r = oppMove.charAt(0);
		char c = oppMove.charAt(1);
		int row = Character.getNumericValue(r);
		int col = Character.getNumericValue(c);
		
		String[][] currentBoard = board.getBoard();
		
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(currentBoard[i][j]=="_"){
					board.setBoard(i, j, false);
					int moveVal = minMax(board, 4, false, endTime,min,max);
					board.removeFromBoard(i, j, false);
					
					if(moveVal > bestVal){
						bestRow = i;
						bestCol = j;
						bestVal = moveVal;
						
						bestMove[0] = bestRow;
						bestMove[1] = bestCol;
					}
				}
			}
		}
		
		return bestMove;
	}
	
	
	private static int minMax (GameBoard board, int depth, boolean isMax, LocalTime endTime, int alpha, int beta){

		String[][] currentBoard = board.getBoard();
		
		int score = evaluateBoard(currentBoard);
		int num = 0;

		
		// AI win leaf node
		if(score == 100){
			return (score-depth);
		}
		
		// Opponent win leaf node
		if(score == (-100)){
			return (score+depth);
		}
		
		// Draw leaf node
		if(board.isMovesRemaining() == false){
			return 0;
		}
		
		// If time has ellapsed return score of current node
		LocalTime now = LocalTime.now();
		if(now.isAfter(endTime) || depth == 0){
			if(isMax == true){
				return score-depth;
			} else{
				return score+depth;
			}
		}
		
		if(isMax){
			int best = -1000;
			for(int i = 0; i < 8; i++){
				for(int j = 0; j < 8; j++){
					if(currentBoard[i][j] == "_"){
						board.setBoard(i, j, false);
						int val = minMax(board,depth-1,!isMax,endTime,alpha,beta);
						best = Math.max(best,val);
						alpha = Math.max(alpha,best);
						board.removeFromBoard(i, j, false);
						if(beta <= alpha){
							break;
						}
					}
				}
			}
			return best;
		}else{
			int best = 1000;
			for(int i = 0; i < 8; i++){
				for(int j = 0; j < 8; j++){
					if(currentBoard[i][j] == "_"){
						board.setBoard(i, j, true);
						int val = minMax(board,depth-1,!isMax,endTime,alpha,beta);
						best = Math.min(best,val);
						beta = Math.min(beta,best);
						board.removeFromBoard(i, j, true);
						if(beta <= alpha){
							break;
						}
					}
				}
			}
			return best;
		}
	}
	
	private static int evaluateBoard(String[][] board){
		String[][] currentBoard = board;
		
		// Check for winner in rows case
		for (int row = 0; row < 8;row++){
			for(int i = 0; i < 5; i++){
				if((currentBoard[row][i] == currentBoard[row][i+1])&&(currentBoard[row][i+1] == currentBoard[row][i+2])&&(currentBoard[row][i+2] == currentBoard[row][i+3])){
					if(currentBoard[row][i] == "X"){
						return 100;
					} else if(currentBoard[row][i] == "O"){
						return (-100);
					}
				}
			}
		}
		
		// Check for winner in cols case
		for (int col = 0; col < 8;col++){
			for(int i = 0; i < 5; i++){
				if((currentBoard[i][col] == currentBoard[i+1][col])&&(currentBoard[i+1][col] == currentBoard[i+2][col])&&(currentBoard[i+2][col] == currentBoard[i+3][col])){
					if(currentBoard[i][col] == "X"){
						return 100;
					} else if(currentBoard[i][col] == "O"){
						return (-100);
					}
				}
			}
		}
		
		// Return 0 for no winner case
		return 0;
	}
	
	private static boolean isOver(GameBoard board){
		String[][] currentBoard = board.getBoard();
		int score = evaluateBoard(currentBoard);
		if(score == 100){
			System.out.println("Computer Won!!!!!!!");
			return true;
		}
		
		// Opponent win leaf node
		if(score == (-100)){
			System.out.println("Opponent Won!!!!!!!");
			return true;
		}
		
		// Draw leaf node
		if(board.isMovesRemaining() == false){
			System.out.println("It's a Draw!!!!!!!");
			return true;
		}
		
		return false;
	}
}