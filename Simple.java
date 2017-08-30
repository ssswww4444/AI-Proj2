/* COMP30024 Artificial Intelligence
 * Author: Pei-Yun Sun <peiyuns> 667816
 * Author: Wenqiang Kuang <wenqiangk> 733272
 */
package aiproj.slider;

import java.util.ArrayList;
import java.util.Random;

public class Simple implements SliderPlayer{

	private Board board;
	private char player;
	
	@Override
	public void init(int dimension, String board, char player) {
		this.board = new Board(board, dimension);
		this.player = player;
	}

	@Override
	public void update(Move move) {
		/*Game just starts or opponent does not move*/
		if(move == null){
			
		} else{
			board.movePiece(move.i, move.j, move.d);
		}
	}

	@Override
	public Move move() {
		Move move = simpleAlgo();	
		// update board as well
		if(move != null) {
			board.movePiece(move.i, move.j, move.d);
		}
		return move;
	}

	public Move simpleAlgo() {
		// random select a piece
		Piece piece = null;
		int i = 0;
		int j = 0;
		Move.Direction d = null;
		
		board.getAllMoves('V');
		board.getAllMoves('H');
		  
		Random random = new Random();
		if(player == 'V') {  // player V
			// aaaaaa
			ArrayList<Piece> pieces = board.getPieces('V');
			while(true) {
				piece = pieces.get(random.nextInt(pieces.size()));
				i = piece.xPos;
				j = piece.yPos;
				if (piece.canMoveUp()) {
				     d = Move.Direction.UP;
				     break;
				} else if (((Vpiece)piece).canMoveLeft()) {
				     d = Move.Direction.LEFT;
				     break;
				} else if (piece.canMoveRight()) {
				     d = Move.Direction.RIGHT;
				     break;
				}
			}
		} else {  // player H
			// aaaaaa
			ArrayList<Piece> pieces = board.getPieces('H');
			while(true) {
				piece = pieces.get(random.nextInt(pieces.size()));
				i = piece.xPos;
				j = piece.yPos;
				if (piece.canMoveRight()) {
					d = Move.Direction.RIGHT;
					break;
				} else if (((Hpiece)piece).canMoveDown()) {
					d = Move.Direction.DOWN;
					break;
				} else if (piece.canMoveUp()) {
					d = Move.Direction.UP;
					break;
				}
			}
		}
		return new Move(i, j, d);
	}
}
