import java.util.*;

/**
 * This is the AiPlayer class.  It simulates a minimax player for the max
 * connect four game.
 * The constructor essentially does nothing. 
 * 
 * @author james spargo
 */
public class AiPlayer 
{
	
	private static int boardCount = 1;
	
	/**
	 * The constructor essentially does nothing except instantiate an
	 * AiPlayer object.
	 *
	 */
	public AiPlayer() 
	{
		// nothing to do here
	}
	
	/**
	 * This method plays a piece randomly on the board
	 * @param currentGame The GameBoard object that is currently being used to
	 * play the game.
	 * @return an integer indicating which column the AiPlayer would like
	 * to play in.
	 */
	public GameBoard findBestPlay(GameBoard currentGame, int depthLevel) 
	{
		
		List currentLevelStates = new ArrayList();
		currentLevelStates.add(currentGame);
		
		currentGame.setEvalValue(0);
		currentGame.setActionStates(new ArrayList());
		
		formStateTree(currentLevelStates, depthLevel);
		
		currentGame = (GameBoard) currentLevelStates.get(0);
		DepthLimitMinimax minimax = new DepthLimitMinimax();
		GameBoard nextMoveBoard = minimax.alphaBetaSearch(currentGame, depthLevel);
		
		return nextMoveBoard;
	}
	
	/**
	 * Forms the tree structure for the possible moves for boards in the list.
	 * @param gameBoardlist List of boards whose possible moves tree is to be formed.
	 * @param depthLevel The depth limit upto which the tree will be formed.
	 */
	private void formStateTree(List gameBoardlist, int depthLevel) {
		
		if (depthLevel == 1)
			return;
		
		List levelStates = new ArrayList();
		
		GameBoard gBoard = null, succBoard = null;
		for (int i = 0; i < gameBoardlist.size(); i++) {
			
			gBoard = (GameBoard) gameBoardlist.get(i);
			
			for (int column = 0; column < gBoard.getBoardSize(2); column++) {
				
				succBoard = gBoard.copy();
				if (succBoard.playPiece(column) == false)  //If column is full skip adding that piece.
					continue;
				
				succBoard.setLastColumnPlayed(column + 1);
				succBoard.setParentBoard(gBoard);
				gBoard.getActionStates().add(succBoard);
				
				levelStates.add(succBoard);
			}
		}
		
		formStateTree(levelStates, depthLevel - 1);
	}
	
}
