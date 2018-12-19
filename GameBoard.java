public class GameBoard{
	
	private String[][] board = new String[8][8];
	
	public GameBoard(){
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				board[i][j] = "_";
			}
		}
	}
	
	public boolean validMove(int row, int col){
		if(board[row][col] != "_"){
			return false;
		}
		return true;
	}
		
	public void setBoard(int row, int col, boolean val){
		if(val == false){
			board[row][col] = "X";
		} else {
			board[row][col] = "O";
		}
	}
	
	public void removeFromBoard(int row, int col, boolean val){
		if(val == false){
			board[row][col] = "_";
		} else {
			board[row][col] = "_";
		}
	}
	
	public String[][] getBoard(){
		return board;
	}
	
	public boolean isMovesRemaining(){
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(board[i][j] == "_"){
					return true;
				}
			}
		}
		return false;
	}
	
	public void boardToString(){
		for(int i = 0 ; i < 9; i++){
			for(int j = 0 ; j < 9; j++){
				if(i == 0){
					if(j != 0){
						System.out.print(j+" ");
					}else{
						System.out.print("  ");
					}
				}else if(j == 0){
					System.out.print(i+" ");
				}else{
					System.out.print(board[i-1][j-1]+" ");
				}
			}
			System.out.println("");
		}
	}
}