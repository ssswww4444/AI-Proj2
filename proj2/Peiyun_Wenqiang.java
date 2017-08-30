/* Class MyPlayer, used to play the role of gaming agent. 
 * It is called by Referee to start the game. 
 * MiniMax and alpha-beta pruning is used in the agent. 
 * Temporal difference learning is being added. 
 * 
 * COMP30024 Artificial Intelligence
 * Author: Pei-Yun Sun <peiyuns> 667816
 * Author: Wenqiang Kuang <wenqiangk> 733272
 */
package aiproj.slider;

import java.util.ArrayList;

/** Class MyPlayer, used to play the role of gaming agent. It implements 
 * the Slider player interface. */
public class Peiyun_Wenqiang implements SliderPlayer {
	
	private ArrayList<Double> weights;
	private ArrayList<Double> featureSet;  // features of the bestMove
	public static final int DEPTH_LIMIT = 5;  // depth limit of minimax

	private char player;  // either 'H' or 'V'
	private Board board;  // internal representation of board
	private double alpha = Double.MIN_VALUE, beta;

	/** Init the player */
	@Override
	public void init(int dimension, String board, char player) {
		this.board = new Board(board, dimension);
		this.player = player;
		TDLeaf.newGame();  // reset variables in TDLeaf
		weights = TDLeaf.getWeights(dimension, player == 'H');  // read weights from file
	}
	
	/** Update the player's board with opponent's move */
	@Override
	public void update(Move move) {
		if(move!=null) {
			board.movePiece(move.i, move.j, move.d);  // update board according to opponent's last move
		} 
	}

	/** Choose a move of the player */
	@Override
	public Move move() {
		Move move = algo();
		if(move!=null) {
			board.movePiece(move.i, move.j, move.d);  // update own board
		}
		return move;
	}
	
	/** Calls the minimax method to explore children of given move and player
	 * and hence find out the best move. */
	private Move algo() {
		double bestMoveValue = Double.MIN_VALUE;  // Init it to be infinitely small at start
		Move bestMove = null;
		
		ArrayList<Move> moves = board.getAllMoves(this.player);
		
		// Loop through all possible moves and update bestMove
		alpha = -1.0E9;
		beta = 1.0E9;
		for(Move move: moves) {
			//System.out.println("L="+DEPTH_LIMIT+", "+this.player+" M("+move.i+","+move.j+","+move.d+")");
			double value = minimax(board, true, move, DEPTH_LIMIT-1, this.player);
			//System.out.println("L="+DEPTH_LIMIT+", V="+value);
			if(value > bestMoveValue) {
				bestMoveValue = value;
				bestMove = move;
			}
		}
		
		// Do not find any good move but have legal moves
		if(bestMove == null && !moves.isEmpty()) {  
			bestMove = moves.get(0);  // take any move ****
		}
		
		if(bestMove != null) {  // going to take a move
			Board temp = new Board(board.toString(), board.getDimension());  // clone a temp board
			temp.movePiece(bestMove.i, bestMove.j, bestMove.d);  // take the bestMove
			
			featureSet = new ArrayList<>();  // features of the bestMove
			
			evaluateBoard(temp, true);  // Evaluate board, add features
			
			TDLeaf.recordState(bestMoveValue, featureSet);  // record bestMoveValue and featureSet in TD leaf
			
			if(temp.getPieces(player).isEmpty()) {  // only learn if this player won
				TDLeaf.updateWeights();
			}
		}
		return bestMove;
	}
	
	/** Recursive minimax method with alpha-beta pruning to iteratively explore all child moves to depth limit */
	private double minimax(Board board_in, boolean isMax, Move lastMove, int level, char turn) {
		
		double value;
		Board tmpBoard = new Board(board_in.toString(), board_in.getDimension());  // clone board_in
		tmpBoard.movePiece(lastMove.i, lastMove.j, lastMove.d);  // take lastMove
		//System.out.println(" L"+(level+1)+" "+turn+"-M("+lastMove.i+","+lastMove.j+","+lastMove.d+"), a="+alpha+",b="+beta);
		
		// base case, cut-off, reach bottom and calculate score of the board
		if(level == 0 || tmpBoard.getPieces(turn).isEmpty()) {  // reach limit or Game is over
			value = evaluateBoard(tmpBoard, false);
			return value;  // evaluate without recording features
		}
		
		turn = (turn == 'H')? 'V':'H';  // switch turn
		
		ArrayList<Move> moves = tmpBoard.getAllMoves(turn);  // Get all legal moves of given player
		
		/* recursively exploring, using alpha-beta pruning to ignore some branches. */
		for(Move nextMove: moves) {
			value = minimax(tmpBoard, !isMax, nextMove, level-1, turn);
			//System.out.println(" N"+level+","+turn+"-M("+nextMove.i+","+nextMove.j+","+nextMove.d+"), V="+value); 
			if(isMax) {
                if(value > alpha) 
                    alpha = value;
        		if(alpha >= beta && beta < 1.0E8) {   // alpha or beta cut-off
        			//System.out.println(" DB"+level+", V="+beta);
        			return beta;
                } 
			} else {
                if(value < beta) 
				    beta = value;
        		if(alpha >= beta && alpha > -1.0E8) {  // alpha or beta cut-off
        			//System.out.println(" DA"+level+", V="+alpha);
        			return alpha;
        		}
			}
			
		}
		value = isMax? alpha: beta;
		//System.out.println(" D-"+level+", V="+value);
		return value;
	}
	
	
	/** Evaluation function to calculate board score */
	private double evaluateBoard(Board board, boolean evalBest) {
		// get all pieces
		ArrayList<Piece> Hpieces = board.getPieces('H');
		ArrayList<Piece> Vpieces = board.getPieces('V');
			
		// features
		double HBlockScore = 0;
		double VBlockScore = 0;

		int HBlockedScore = 0;  // negative score
		int VBlockedScore = 0;  // negative score

		// dist from initial point
		int HMoveScore = 0;
		int VMoveScore = 0;

		// no. of pieces out of board
		int HOutScore = board.getDimension() - 1 - Hpieces.size();
		int VOutScore = board.getDimension() - 1 - Vpieces.size();
			
		// Evaluate Hpieces
		HMoveScore = HOutScore * board.getDimension();  // include moves of pieces out of board
		for(Piece piece: Hpieces) {
			HMoveScore += piece.getxPos();
			if(board.horizontalBlocked(piece))  // barrier just on its right
				HBlockedScore--;
			int n = board.SearchV_BlockedByH(piece);  // dist from the Vpiece blocking current Hpiece
			if(n >= 0) {  // blocked by Vpiece
//				if(evalBest) 
//					System.out.println("H["+piece.getxPos()+","+piece.getyPos()+"]="+n);
				HBlockScore += 1 - n * 0.1;  // further dist has lower score
			}
		}
			
		// Evaluate Vpieces
		VMoveScore = VOutScore * board.getDimension();  // include moves of pieces out of board
		for(Piece piece: Vpieces) {
			VMoveScore += piece.getyPos();
			if(board.verticalBlocked(piece))  // barrier just upon it
				VBlockedScore--;
			int n = board.SearchH_BlockedByV(piece);  // dist from the Hpiece blocking current Vpiece
			if(n >= 0) {  // blocked by Hpiece
//				if(evalBest) 
//					System.out.println("V["+piece.getxPos()+","+piece.getyPos()+"]="+n);
				VBlockScore += 1 - n * 0.1;  // further dist has lower score
			}
		}
			
		// score = own score - opponent's score, assuming player is H first
		double blockScore = HBlockScore - VBlockScore;
		double blockedScore = HBlockedScore - VBlockedScore;
		double outScore = HOutScore - VOutScore;
		double moveScore = HMoveScore - VMoveScore;
			
		if(this.player == 'V') {  // the other way round if player is V
			blockScore = -blockScore;
			blockedScore = -blockedScore;
			moveScore = -moveScore;
			outScore = -outScore;
		}
			
		// total score of each feature * weight
		double score = blockScore * weights.get(0) + blockedScore * weights.get(1) + 
				moveScore * weights.get(2) + outScore * weights.get(3);

		// only record featureSet when evaluating the bestMove of this.player
		if(evalBest) {
//						System.out.println("EV H(" + HBlockScore + "," + HBlockedScore + "," + HMoveScore + "," + HOutScore + "), V(" + 
//												   VBlockScore + "," + VBlockedScore + "," + VMoveScore + "," + VOutScore + ")");

			// record the features for TD leaf
			featureSet.add(blockScore);
			featureSet.add(blockedScore);
			featureSet.add(moveScore);
			featureSet.add(outScore);
//			System.out.println("FV:"+featureSet.toString());
//			System.out.println("score="+score);
		}
		return score;
	}
}
