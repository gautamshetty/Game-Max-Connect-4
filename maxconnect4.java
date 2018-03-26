import java.io.*;

/**
 * @author James Spargo This class controls the game play for the Max
 *         Connect-Four game. To compile the program, use the following command
 *         from the maxConnectFour directory: javac *.java
 *
 *         the usage to run the program is as follows: ( again, from the
 *         maxConnectFour directory )
 *
 *         -- for interactive mode: java MaxConnectFour interactive [ input_file
 *         ] [ computer-next / human-next ] [ search depth]
 *
 *         -- for one move mode java maxConnectFour.MaxConnectFour one-move [
 *         input_file ] [ output_file ] [ search depth]
 * 
 *         description of arguments: [ input_file ] -- the path and filename of
 *         the input file for the game
 * 
 *         [ computer-next / human-next ] -- the entity to make the next move.
 *         either computer or human. can be abbreviated to either C or H. This
 *         is only used in interactive mode
 * 
 *         [ output_file ] -- the path and filename of the output file for the
 *         game. this is only used in one-move mode
 * 
 *         [ search depth ] -- the depth of the minimax search algorithm
 */
public class maxconnect4 {

	private static GameBoard currentGame = null;
	
	public GameBoard getCurrentGame() {
		return currentGame;
	}
	
	public void setCurrentGame(GameBoard currentGame) {
		maxconnect4.currentGame = currentGame;
	}
	
	public static void main(String[] args) {

		 // check for the correct number of arguments
		 if( args.length != 4 )
		 {
			 System.out.println("Four command-line arguments are needed:\n"
							 + "Usage: java [program name] interactive [input_file] [computer-next / human-next] [depth]\n"
							 + " or: java [program name] one-move [input_file] [output_file] [depth]\n");
			 
			 exit_function( 0 );
		 }
		
		// Game mode
		String game_mode = args[0].toString();		
		
		// Input game file.
		String input_file = args[1].toString();
		
		// Depth level of the ai search
		int depthLevel = Integer.parseInt(args[3]);
		
		System.out.print("\nMaxConnect-4 Game\n");
		
		// create and initialize the game board
		currentGame = new GameBoard(input_file);
		
		if (game_mode.equalsIgnoreCase("interactive")) {
			
			String nextPlayer = args[2].toString();
			
			System.out.println("Game Start");
			while (true) {
				
				currentGame.printGameBoard();
				
				// print the current scores
				System.out.println("Score: Player 1 = " + currentGame.getScore(1) + 
								   ", Player2 = " + currentGame.getScore(2) + "\n ");
				
				if (currentGame.getPieceCount() >= 42) {
					System.out.println("\nBoard is Full\n\nGame Over");
					System.out.println("Score: Player 1 = " + currentGame.getScore(1) + 
							   		   ", Player2 = " + currentGame.getScore(2));
					if (currentGame.getScore(1) > currentGame.getScore(2))
						System.out.println("Congratulations. Player 1 Won.");
					else if (currentGame.getScore(1) < currentGame.getScore(2))
						System.out.println("Congratulations. Player 2 Won.");
					else 
						System.out.println("Its a Draw.");
					break;
				}
				
				int currentTurn = currentGame.getCurrentTurn();
				if (nextPlayer.equals("computer-next")) {
					
					System.out.println("Computer next move : ");
					
					// create the Ai Player
					AiPlayer calculon = new AiPlayer();
					
					// AI play - set next move board in currentGame.
					currentGame = calculon.findBestPlay(currentGame, depthLevel);
					
					nextPlayer = "human-next";
				} else {
					
					BufferedReader buffRead = new BufferedReader(new InputStreamReader(System.in));
					System.out.println("Human next move : ");
					
					int colMove = -1;
					while (true) {
						
						System.out.print("Enter the column on board to play (1 - 7) : ");
						
						try {
							colMove = Integer.parseInt(buffRead.readLine()) - 1;
						} catch (IOException ioEx) {
							ioEx.printStackTrace();
						}
						
						if (!currentGame.isValidPlay(colMove)) {
							System.out.println("\nInvalid column entry. Please retry.");
							continue;
						}
						
						break;
					}
					
					currentGame.playPiece(colMove);
					currentGame.setLastColumnPlayed(colMove + 1);
					
					nextPlayer = "computer-next";
				}
				
				// display the current game board
				System.out.println("Move: " + currentGame.getPieceCount()
								 + ", Player: " + currentTurn 
								 + ", Column: " + currentGame.getLastColumnPlayed());
			}
			
			return;
		} else if (game_mode.equalsIgnoreCase("one-move")) {
			
			///////////// one-move mode ///////////
			// get the output file name
			String output = args[2].toString(); // the output game file
			
			System.out.print("game state before move:\n");
			
			// print the current game board
			currentGame.printGameBoard();
			
			// print the current scores
			System.out.println("Score: Player 1 = " + currentGame.getScore(1) 
							 + ", Player2 = " + currentGame.getScore(2) + "\n ");
			
			// ****************** this chunk of code makes the computer play
			if (currentGame.getPieceCount() < 42) {
				
				// create the Ai Player
				AiPlayer calculon = new AiPlayer();
				
				// AI play - set next move board in currentGame.
				currentGame = calculon.findBestPlay(currentGame, depthLevel);
				
				// play the piece
				// currentGame.playPiece( playColumn );
				
				// display the current game board
				System.out.println("Move: " + currentGame.getPieceCount()
								 + ", Player: " + currentGame.getCurrentTurn()  
								 + ", Column: " + currentGame.getLastColumnPlayed());
				
				System.out.print("Game state after move:\n");
				currentGame.printGameBoard();
				
				// print the current scores
				System.out.println("Score: Player 1 = " + currentGame.getScore(1) 
								 + ", Player2 = " + currentGame.getScore(2) + "\n ");
				
				currentGame.printGameBoardToFile(output);
				
			} else {
				
				System.out.println("\nBoard is Full\n\nGame Over");
			}
			
			// ************************** end computer play
		}
	} // end of main()
	
	/**
	 * This method is used when to exit the program prematurly.
	 * 
	 * @param value
	 *            an integer that is returned to the system when the program exits.
	 */
	private static void exit_function(int value) {
		
		System.out.println("exiting from MaxConnectFour.java!\n\n");
		System.exit(value);
	}
	
} // end of class connectFour
