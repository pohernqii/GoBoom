import java.util.Map;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//Game.java file typically represents the main game logic. 
//It includes the gameplay flow and interactions between players, the deck, and cards.
public class Game {
    private CloseDeck deck;
    private CenterDeck centerDeck;
    private Player[] players;
    private String turn;
	private int trick;
	private int playerSequence;
    private Card leadCard;
    private int currentPlayerIndex;
    private int trickWinnerIndex;
	private int winnerIndex;
    private Map<String, Integer> cardValues;
	private Map<String, Integer> cardScoreValues;
    private Set<String> suitsSet;
    private Set<String> ranksSet;
    private boolean remainSamePlayer = false; // flag that prevents from continuing, remains the same player
	private	boolean initializeNewGame = true;
	private	boolean isEndGame = false;
	private	boolean isQuitGame = false;
	private	boolean clearScore = false;
	private final int cardCountPerPlayer = 7;
	private final int playerCount = 4;
	private final int maxEndGameScore = 50; // change form 50 to 10 for testing

	
    public Game() {
        deck = new CloseDeck();
        centerDeck = new CenterDeck();
		leadCard = new Card();
				
        players = new Player[playerCount];
        for (int i = 0; i < playerCount; i++) {
            players[i] = new Player();
        }
		
		cardValues = leadCard.getCardValues();
		cardScoreValues = leadCard.getCardScoreValues();
        suitsSet = leadCard.getSuitsSet();
        ranksSet = leadCard.getRanksSet();
   }

    private void dealCards() { // deal cards to every player
        clearCardsDecks();
		deck.shuffle();
        for (int i = 0; i < cardCountPerPlayer; i++) {    
            for (Player player : players) {
                player.addCard(deck.deal());
				if (clearScore) player.setScore(0); // reset players' score
            }
        }
    }

	private void clearCardsDecks() {
		for (Player player : players) {
			player.clearRemoveCardInHand();
			player.clearCardInHand();
		}
		centerDeck.clearDeck();
		deck.clearDeck();
		((DeckInitialization) deck).initDeck();
    }
	
    private void displayTrick() {
        System.out.println("Trick #" + trick);
    }
	
    private void displayPlayer() {
        for (int i = 0; i < playerCount; i++) {
            System.out.print("Player " + (i + 1) + ": ");
            players[i].showHand();
            System.out.print("\n");
        }
    }

    private void displayCenter() {
        System.out.print("Center: ");
        centerDeck.showDeck();
    }

    private void displayDeck() {
        System.out.print("Deck: ");
        deck.showDeck();
    }

	private void displayScore() {
		displayScore(false);
	}
	
    private void displayScore(boolean isFinal) {
        if(isFinal) System.out.print("Final score: ");
		else System.out.print("Score: ");
        int i = 0;
        for (Player player : players) {
            System.out.print("Player " + (i + 1) + "= ");
            System.out.print(player.getScore() + " |  ");
            i++;
        }
    }

    private void initializeVariables() { // initialize variables when start the game
 		centerDeck.addCard(deck.cards.remove(0));
		leadCard.leadCard(centerDeck.getDeck().get(0)); // set the first card in centerdeck as lead card

		setWinnerIndex(-1); //reset winnner's index
    }

   
    private void displayTurn(boolean trickWinnerFound, Scanner input) {
        if (trickWinnerFound) {
            currentPlayerIndex = trickWinnerIndex - 1;
        } else {
             if(!getRemainSamePlayer())
                 currentPlayerIndex = (currentPlayerIndex + 1) % playerCount;
        }
        turn = "Player " + (currentPlayerIndex + 1);
  
        System.out.println("\nTurn: " + turn);

        inputCommand(input);
     }

    
    private void inputCommand(Scanner input){
        boolean validInput = false;
        String inputString = null;
        String suit = null;
        String rank = null;
		ArrayList<Card> hand = null;
		ArrayList<Card> removedCard = null;
		Card inputCard = null;

		while (!validInput) {
            System.out.print("> ");
            inputString = input.next();

            suit = inputString.substring(0, 1);
            rank = inputString.substring(1);
                
            if (isValidCommand(inputString)) {
                inputCard = new Card(suit, rank);
                hand = players[currentPlayerIndex].getCardInHand();
                removedCard = players[currentPlayerIndex].getRemoveCardInHand();

                if (inputString.equals("d")) {
                    handleDrawInput(hand, deck, leadCard);
					validInput = true;

                    if (!getRemainSamePlayer()) {
                        break;
                    }
                }
				else if(inputString.equals("s"))
				{
					validInput = true;
                    saveGame(input);
				} 
				else if (inputString.equals("l")) 
				{
					validInput = true;
					loadGame(input);
 				} 
				else if (inputString.equals("q")) 
				{
					validInput = true;
					quitGame();
                } 
				else if (inputString.equals("r")) 
				{
					validInput = true;
					resetGame();					
                }
				else if (inputString.equals("h")) 
				{
					validInput = true;
					setRemainSamePlayer(true);
					displayHelpMenu();
                }				
				else 
				{
                    //If the inputs is not any of the commands, it is card, go to verifycardsinhand
                    validInput=verifyCardsInHand(inputCard, hand, removedCard);
                } 
            } else {
				System.out.println("Invalid entry. Please input a valid input.");
            }
	    }
    }


    private void saveGame(Scanner input) {
        String filename = null;
		BufferedWriter writer = null;
        try {
			System.out.print("Enter filename > ");
			filename = input.next();
            writer = new BufferedWriter(new FileWriter(filename));

            // Write in the players card in hand and players removed card
            for (int i = 0; i < players.length; i++) {
                writer.write(i + "\n");
                ArrayList<Card> cardInHand = players[i].getCardInHand();
                StringBuilder cardInHandStr = new StringBuilder();
                for (Card card : cardInHand) {
                    cardInHandStr.append(card.toString()).append(" ");
                }
                writer.write(cardInHandStr.toString() + "\n");

                ArrayList<Card> removeCards = players[i].getRemoveCardInHand();
                StringBuilder removedCardInHandStr = new StringBuilder();
                for (Card card : removeCards) {
                    removedCardInHandStr.append(card.toString()).append(" ");
                }
                writer.write(removedCardInHandStr.toString() + "\n");
				writer.write(players[i].getScore() + "\n");
            }
            // write in the deck card
            for (int i = 0; i < deck.size(); i++) {
                writer.write(deck.getString(i) + " ");
            }
            // write in the center deck
            writer.write("\n");
            for (int i = 0; i < centerDeck.size(); i++) {
                writer.write(centerDeck.getString(i) + " ");
            }
            writer.write("\n");
            writer.write(currentPlayerIndex + "\n");
            writer.write(leadCard.getLeadCard().toString() + "\n");
            writer.write(Integer.toString(trick) + "\n");
			writer.write(Integer.toString(playerSequence));

            System.out.println("Successfully saved to " + filename);
			
			setRemainSamePlayer(true);
			
            if(writer != null) writer.close();

        } catch (IOException e) {
            System.err.println("An error occured when saving to file: " + filename + ". Please try again.");
            
        } 
    }

    private void loadGame(Scanner input) {
		String cardInHand = null;
		String removedCard = null;
		int playerIndex = 0;
		String filename = null;
        BufferedReader reader = null;
		
		try {
		    System.out.print("Enter filename > ");
			filename = input.next();
		
            reader = new BufferedReader(new FileReader(filename));
            // read the players card in hand and removed card
            for (int i = 0; i < players.length; i++) {
                playerIndex = Integer.parseInt(reader.readLine());
                cardInHand = reader.readLine();
                removedCard = reader.readLine();

                players[playerIndex].setCardInHand(Card.parseCardList(cardInHand));
                if (!removedCard.isEmpty()) {
                    players[playerIndex].setRemoveCardInHand(Card.parseCardList(removedCard));
                } else {
                    players[playerIndex].setRemoveCardInHand(new ArrayList<Card>());
                }
				players[playerIndex].setScore(Integer.parseInt(reader.readLine()));
            }
            // read the deck card
            deck.clearDeck();
            String deckCardString = reader.readLine();
            if (!deckCardString.isEmpty()) {
                ArrayList<Card> deckCards = Card.parseCardList(deckCardString);
                deck.setDeck(deckCards);
            }

            // read the center deck card
            centerDeck.clearDeck();
            String centerCardString = reader.readLine();
            if (!centerCardString.isEmpty()) {
                ArrayList<Card> centerDeckCards = Card.parseCardList(centerCardString);
                centerDeck.setDeck(centerDeckCards);
            }

            // Read the current player index and lead card
            currentPlayerIndex = Integer.parseInt(reader.readLine());

            String newleadcardString = reader.readLine();
            Card newleadCard = Card.parseCard(newleadcardString);
            leadCard.leadCard(newleadCard);

            this.trick = Integer.parseInt(reader.readLine());
			this.playerSequence = Integer.parseInt(reader.readLine());
			
			setRemainSamePlayer(true);
			
            if(reader != null) reader.close();

        } catch (FileNotFoundException f){
             System.err.println("The specified file was not found: " + f.getMessage());
             
        } catch (IOException e) {
            System.err.println("An error occurred when loading from file: " + filename + ". Please try again.");
        } 
    }

    private boolean verifyCardsInHand(Card inputCard, ArrayList<Card> hand, ArrayList<Card> removedCard) 
	{
		boolean isValidInput = false;
        if (centerDeck.getDeck().size() != 0) {
            if (hand.contains(inputCard)) {
                if (currentPlayerIndex == trickWinnerIndex - 1) {
                    leadCard.leadCard(inputCard);
                }
                if (inputCard.getSuit().equals(leadCard.getLeadCard().getSuit()) || inputCard.getRank().equals(leadCard.getLeadCard().getRank())) 
				{
                    removeCard(hand, inputCard, removedCard);
                    isValidInput = true;
                   
                    setRemainSamePlayer(false);
                } else {
                    System.out.println("The input card does not match any of the suit or rank. Please re-enter.");
                }
            } else {
                System.out.println("Invalid entry. The card is not in the player " + (currentPlayerIndex + 1) + "'s hand.");
            }
        } else {
            if (hand.contains(inputCard)) {
                removeCard(hand, inputCard, removedCard);
                leadCard.leadCard(inputCard);
                isValidInput = true;
                setRemainSamePlayer(false);
            } else {
                System.out.println("Invalid entry. The card is not in the player " + (currentPlayerIndex + 1) + "'s hand.");
            }
        }
        return isValidInput;
    }


    //check make sure the inputstring contains only the fixed commands, other commands go to invalid entry
    private boolean isValidCommand(String inputString) 
	{
		return inputString != null && !inputString.trim().equals("") &&
                (inputString.equals("d") || inputString.equals("s") || inputString.equals("l") || inputString.equals("q") 
				|| inputString.equals("r") || inputString.equals("h") || leadCard.isValidCard(inputString.substring(0, 1), inputString.substring(1),
                                suitsSet, ranksSet));
	}

	private void removeCard(ArrayList<Card> hand, Card inputCard, ArrayList<Card> removedCard)
	{
		hand.remove(inputCard);
        centerDeck.addCard(inputCard);
        removedCard.add(inputCard);
	}

    private void handleDrawInput(ArrayList<Card> hand, Deck deck, Card leadCard) {
		boolean continueDraw = true;
		Card drawnCard = null;
		while(continueDraw)
		{
			drawnCard = deck.deal();
			if (drawnCard != null) {
				hand.add(drawnCard);
				System.out.println("Player " + (currentPlayerIndex + 1) + " drew a card: " + drawnCard);			
				setRemainSamePlayer(true);
				if(drawnCard.getRank().equals(leadCard.getLeadCard().getRank()) || drawnCard.getSuit().equals(leadCard.getLeadCard().getSuit()))
				{
					continueDraw = false;
				}
			}
			else
			{
                setRemainSamePlayer(false);
				System.out.println("Deck is empty. Player " + (currentPlayerIndex + 1) + " cannot draw a card. Skipping player " + (currentPlayerIndex + 1) + "'s turn.");
				continueDraw = false;
			}
		}
	
    }

    private void setRemainSamePlayer(boolean remainSamePlayer){
        this.remainSamePlayer=remainSamePlayer;
    }

    private boolean getRemainSamePlayer(){
        return remainSamePlayer;
    }
	
	private void setWinnerIndex(int winnerIndex){
        this.winnerIndex=winnerIndex;
    }

    private int getWinnerIndex(){
        return winnerIndex;
    }
	

    private void calculateTrickWinner() {
        // Calculate Trick Winner for every round
        int highestRank = -1;
        int tempWinnerIndex = -1;
		ArrayList<Card> removedCard = null;
		Integer rankValue = -1;
		String leadSuit = null;
		
        for (int i = 0; i < playerCount; i++) {
            // Get every player's RemoveCard value and compare the card value
            removedCard = players[i].getRemoveCardInHand();
            leadSuit = leadCard.getLeadCard().getSuit();
            for (Card card : removedCard) {
                if (card.getSuit().equals(leadSuit)) {
                    rankValue = cardValues.get(card.getRank());
                    if (rankValue > highestRank) {
                        highestRank = rankValue;
                        tempWinnerIndex = i + 1;
                    }
                }
            }
        }
        for (int j = 0; j < playerCount; j++) {
            players[j].clearRemoveCardInHand();
        }
        trickWinnerIndex = tempWinnerIndex;
        currentPlayerIndex = tempWinnerIndex - 1;
    }

	private void calculateScore() {
		int tempScore = 0;
		Integer rankScoreValue = -1;
		ArrayList<Card> inHandCards = null;
        for (int i = 0; i < playerCount; i++) {
            // Get every player's RemoveCard value and compare the card value
            inHandCards = players[i].getCardInHand();
			tempScore = players[i].getScore();
            for (Card card : inHandCards) {
                rankScoreValue = cardScoreValues.get(card.getRank());
                tempScore = tempScore + rankScoreValue;
            }
			players[i].setScore(tempScore);
			
			if (tempScore >= maxEndGameScore) 
			{
				isEndGame = true;
			}
        }
    }
	
    private void getPlayerTurn() {
        if (leadCard.getLeadCard().getRank().equals("A") || leadCard.getLeadCard().getRank().equals("5")
                || leadCard.getLeadCard().getRank().equals("9") || leadCard.getLeadCard().getRank().equals("K")) {
            turn = "Player 1";
            currentPlayerIndex = 0;
        } else if (leadCard.getLeadCard().getRank().equals("2") || leadCard.getLeadCard().getRank().equals("6")
                || leadCard.getLeadCard().getRank().equals("X")) {
            turn = "Player 2";
            currentPlayerIndex = 1;
        } else if (leadCard.getLeadCard().getRank().equals("3") || leadCard.getLeadCard().getRank().equals("7")
                || leadCard.getLeadCard().getRank().equals("J")) {
            turn = "Player 3";
            currentPlayerIndex = 2;
        } else {
            turn = "Player 4";
            currentPlayerIndex = 3;
        }
    }

    private void displayGameState(boolean trickWinnerFound, Scanner input) {
        displayTrick();
        displayPlayer();
        displayCenter();
        displayDeck();
        displayScore();
		displayTurn(trickWinnerFound, input);
		if(players[currentPlayerIndex].isHandEmpty())
		{
			calculateScore();
			if(!isEndGame) setWinnerIndex(currentPlayerIndex);
		}
    }
	
    public void start() {
		boolean trickWinnerFound = false;
		Scanner input = new Scanner(System.in);
		initializeNewGame = true;
		isEndGame = false;
		boolean isValidEndGameInput = false;
		String inputEndGameString = null;
	
		try
		{
			while (initializeNewGame && (!isEndGame || !isQuitGame)) {
				trick = 1;
				trickWinnerFound = false;
				initializeNewGame = false;
				
				dealCards();
				initializeVariables();
				getPlayerTurn();
					
				while (!initializeNewGame && !isEndGame) {
					for (playerSequence = 0; playerSequence < playerCount && !initializeNewGame && (!isEndGame || !isQuitGame); playerSequence++) {
						System.out.println();
						displayGameState(trickWinnerFound, input);
                  
						if (isEndGame && !isQuitGame) 
						{
                            String winnerPlayersIndexString = "";
							displayScore(true);
			
							int minValue = players[0].getScore(); 
							for(int i = 1; i < players.length; i++) { 
								if(players[i].getScore() < minValue){ 
									minValue = players[i].getScore() ; 
								} 
							} 
							for (int i = 0; i < players.length; i++) {
								if(players[i].getScore() == minValue)
								{
									winnerPlayersIndexString = winnerPlayersIndexString + String.format("%d,", i+1);
								}
							}
							System.out.print("\nThis game has ended as having players hitting highest score >= " + maxEndGameScore + ".\nPlayer ");
							System.out.print(winnerPlayersIndexString.substring(0, winnerPlayersIndexString.length()-1));
							System.out.print(" win the game with the lowest score.");
							System.out.println("\nDo you want to continue playing the game? [y/n]");
							

							while(!isValidEndGameInput)
							{
								System.out.print("> ");
								inputEndGameString = input.next();
								switch(inputEndGameString.toLowerCase()){
									case "y":
                                        System.out.println("Starting a new game...");
										initializeNewGame = true;
										isValidEndGameInput = true;
										isEndGame = false;
										clearScore = true;
										break;
									case "n":
										isValidEndGameInput = true;
										quitGame();
										break;
									default:
										System.out.print("Invalid input. Please enter [y/n] only.\n");
								}
							}
							isValidEndGameInput = false;
						}
						else
						{
							if(getWinnerIndex() > -1)
							{ 
								System.out.println("Player " + (getWinnerIndex() + 1) + " finishes his/her cards in hand and wins the round!");
								System.out.println("Calculating score for each player....");
								System.out.println("Starting a new game....");
								initializeNewGame = true;
							}
							else
							{
								trickWinnerFound = false;
								if (getRemainSamePlayer()) { // remain the same player
									playerSequence--;
									continue;
								}
							}
						}
					}

					if(!initializeNewGame && !isEndGame)
					{
						trick++;
						calculateTrickWinner();
						System.out.println("Player " + (trickWinnerIndex) + " wins the trick!\n");
						trickWinnerFound = true;
						centerDeck.clearDeck();
					}
				}
			 }
		}
		catch (Exception e)
		{
			System.out.println("An error is occured during the game is loaded.");
            e.printStackTrace();
		}
		finally
		{
			if(input != null) input.close();
            if(cardValues != null) cardValues.clear();
            if(cardScoreValues != null) cardScoreValues.clear();
            if(suitsSet != null) suitsSet.clear();
            if(ranksSet != null) ranksSet.clear();      
		}

    }

    private void resetGame() {
        System.out.println("Resetting the game...");
		initializeNewGame = true;
		clearScore = true;
    }

    private void quitGame() {
		
        System.out.println("Quitting the game...");
		isEndGame = true;
		initializeNewGame = false;
		isQuitGame = true;
    }
	
    private void displayHelpMenu() {
	    System.out.println("help: Display information about the game commands.");
		System.out.println("Options:");
		System.out.println("d      - Drawing cards");
		System.out.println("s      - Save the game");
		System.out.println("l      - Resume saved game");
		System.out.println("r      - Reset the game");
		System.out.println("q      - Exit the game");
		System.out.println("<card> - Card played by current player. E.g. s2 or sK");
    }
                
   
}
