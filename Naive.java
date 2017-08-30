/* COMP30024 Artificial Intelligence
 * Author: Pei-Yun Sun <peiyuns> 667816
 * Author: Wenqiang Kuang <wenqiangk> 733272
 */
package aiproj.slider;

import java.util.ArrayList;
import java.util.Random;

public class Naive implements SliderPlayer{

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
		Random random = new Random();
		if(player == 'H') {  // player H
			ArrayList<Move> rightMoves = new ArrayList<>();
			ArrayList<Move> moves = board.getAllMoves('H');
			
			for(Move move: moves) {
				if(move.d.equals(Move.Direction.RIGHT)) {
					rightMoves.add(move);
				}
			}
			
			if(!rightMoves.isEmpty()) {
				return rightMoves.get(random.nextInt(rightMoves.size()));  // take a random rightMove
			} else if(!moves.isEmpty()){
				return moves.get(random.nextInt(moves.size()));  // otherwise take a random move
			} else {
				return null;
			}
		} else {   // player V
			ArrayList<Move> upMoves = new ArrayList<>();
			ArrayList<Move> moves = board.getAllMoves('V');
			
			for(Move move: moves) {
				if(move.d.equals(Move.Direction.UP)) {
					upMoves.add(move);
				}
			}
			
			if(!upMoves.isEmpty()) {
				return upMoves.get(random.nextInt(upMoves.size()));  // take a random upMove
			} else if(!moves.isEmpty()){
				return moves.get(random.nextInt(moves.size()));  // otherwise take a random move
			} else {
				return null;
			}
		}
	}
}
