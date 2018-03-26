
/**
 * Depth Limit Minimax Search.
 * @author gautamshetty
 */
public class DepthLimitMinimax {
	
	private final int POS_INFINITY = 999999999;
	
	private final int NEG_INFINITY = -999999999;
	
	/**
	 * Alpha Beta Search function.
	 * @param boardState Game board whose next move is to be found out.
	 * @param depthLevel The cutoff limit depth. The tree will be searched until this limit for the next move.
	 * @return returns the next move.
	 */
	public GameBoard alphaBetaSearch(GameBoard boardState, int depthLevel) {
		GameBoard nextMoveBoard = maxValue(boardState, NEG_INFINITY, POS_INFINITY, depthLevel);
		return nextMoveBoard;
	}
	
	/**
	 * Max Value function for depth limit minimax search.
	 * @param boardState The node whose maximum value is to be found out.
	 * @param alpha alpha value for alpha-beta pruning.
	 * @param beta beta value for alpha-beta pruning.
	 * @param depth the depth limit until which the tree will be explored for next move.
	 * @return returns the maximum utility value board.
	 */
	private GameBoard maxValue(GameBoard boardState, int alpha, int beta, int depth) {
		
		if (isCutOff(depth, boardState)) {
			boardState.setEvalValue(eval(boardState));
			return boardState;
		}
		
		int v = NEG_INFINITY;
		GameBoard maxBoard = null, saMin = null, sa = null;
		for (int i = 0; i < boardState.getActionStates().size(); i++) {
			
			sa = (GameBoard) boardState.getActionStates().get(i);		//MAX node's children.
			saMin = minValue(sa, alpha, beta, depth - 1);			//MIN node of above MAX child.
			
			if (saMin.getEvalValue() > v) {
				sa.setEvalValue(saMin.getEvalValue());
				v = saMin.getEvalValue();
				maxBoard = sa;
			}
			
			if (v >= beta)
				return maxBoard;
			
			alpha = (alpha > v ? alpha : v);
		}
		
		return maxBoard;
	}
	
	/**
	 * Min Value function for depth limit minimax search.
	 * @param boardState The node whose minimum value is to be found out.
	 * @param alpha alpha value for alpha-beta pruning.
	 * @param beta beta value for alpha-beta pruning.
	 * @param depth the depth limit until which the tree will be explored for next move.
	 * @return returns the minimum utility value board.
	 */
	private GameBoard minValue(GameBoard boardState, int alpha, int beta, int depth) {
		
		if (isCutOff(depth, boardState)) {
			boardState.setEvalValue(eval(boardState));
			return boardState;
		}
		
		int v = POS_INFINITY;
		GameBoard minBoard = null, saMax = null, sa = null;
		for (int i = 0; i < boardState.getActionStates().size(); i++) {
			
			sa = (GameBoard) boardState.getActionStates().get(i);		//MIN node's children.
			saMax = maxValue(sa, alpha, beta, depth - 1);			//MAX node of above MIN child.
			
			if (saMax.getEvalValue() < v) {
				sa.setEvalValue(saMax.getEvalValue());
				v = saMax.getEvalValue();
				minBoard = sa;
			}
			
			if (v <= alpha)
				return minBoard;
			
			beta = (beta < v ? beta : v);
		}
		
		return minBoard;
	}
	
	/**
	 * Cut off function for limiting the number of depths to explore for deciding the next move.
	 * @param depth the depth of the tree.
	 * @param gameBoard the board of the limit depth. 
	 * @return returns true if the limit is reached and false otherwise.
	 */
	private boolean isCutOff(int depth, GameBoard gameBoard) {
		//In case the remaining moves are less than the depth the checking empty states.
		if (depth == 1 || gameBoard.getActionStates().isEmpty())	 
			return true;
		
		return false;
	}
	
	/**
	 * Eval function for the depth limit minimax algorithm.
	 * @param boardState leaf node board state.
	 * @return returns numerical value for the eval value.
	 */
	private int eval(GameBoard boardState) {
		
		int player1Score = 0, player2Score = 0, eval = 0;
		int currentTurn = boardState.getCurrentTurn();
		
		for (int i = 4; i > 0; i--) {
			
			player1Score = boardState.getScore(1, i);
			player2Score = boardState.getScore(2, i);
			
			//Player playing is 1.
			if (currentTurn == 1 && (player1Score != 0 || player2Score != 0)) {
				
				if (player1Score > player2Score)
					eval = player1Score;
				else 
					eval = -player2Score;
				
				break;
			} else if (currentTurn == 2 && (player1Score != 0 || player2Score != 0)) { //Player playing is 2.
				
				if (player2Score > player1Score)
					eval = player2Score;
				else 
					eval = -player1Score;
				
				break;
			}
		}
		
		return eval;
	}
}
