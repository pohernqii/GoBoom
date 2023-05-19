import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private Deck deck;
    private Player[] players;
    private String turn;
    private int index;
    private Card leadCard;
    private int currentPlayerIndex;
    private int trickWinnerIndex;

    public Game() {
        deck = new Deck();
        players = new Player[4];
        for (int i = 0; i < 4; i++) {
            players[i] = new Player();
        }
    }

    public void dealCards() {   //deal cards to every player
        deck.shuffle();
        for (int i = 0; i < 7; i++) {
            for (Player player : players) {
                player.addCard(deck.deal());
            }
        }
    }

    public void displayPlayer(int trick) {
        System.out.println("Trick #" + trick);
        for (int i = 0; i < 4; i++) {
            System.out.print("Player " + (i + 1) + ": ");
            players[i].showHand();
            System.out.print("\n");
        }
    }

    public void displayCenter(boolean flag, boolean stopContinue) {
        System.out.print("Center: ");
        if (flag == true && stopContinue == false) {
            deck.addFirstCardcenterDeck();
        }

        deck.showcenterDeck();
    }

    public void displayDeck() {
        System.out.print("Deck: ");
        deck.showDeck();
    }

    public void displayScore() {
        System.out.print("Score: ");
        int i = 0;
        for (Player player : players) {
            System.out.print("Player " + (i + 1) + "= ");
            System.out.print(player.getScore() + " |  ");
            i++;
        }
    }

    private void initializeVariables() {   //initialize variables when start the game
        leadCard = new Card();
        ArrayList<Card> centerdeck = deck.getCenterdeck();
        leadCard.leadCard(centerdeck.get(0));   //set the first card in centerdeck as lead card
    }

    public boolean displayTurn(boolean flag, boolean stopContinue, boolean findwinner) {
        if (flag) {    
            initializeVariables();
            getPlayerTurn();
            index = currentPlayerIndex;
        } else {      
            if (!findwinner) {
                currentPlayerIndex = trickWinnerIndex - 1;
                turn = "player " + (currentPlayerIndex + 1);
                index = currentPlayerIndex;
            } else {
                currentPlayerIndex = (currentPlayerIndex + 1) % 4;
                turn = "player " + (currentPlayerIndex + 1);
                index = currentPlayerIndex;
            }
        }
        System.out.println("\nturn: " + turn);
        System.out.print("> ");
        Scanner input = new Scanner(System.in);
        String inputString = input.next();
        if (currentPlayerIndex == trickWinnerIndex - 1 && !flag) {
            // Set the new input as the lead card
            Card inputCard = new Card(inputString.substring(0, 1), inputString.substring(1));
            leadCard.leadCard(inputCard);
        }
        stopContinue = verifyCardsInHand(inputString);
        return stopContinue;
    }

    public int calculateTrickWinner() {
        // Calculate Trick Winner for every round
        int highestRank = -1;
        int winnerIndex = -1;
        for (int i = 0; i < 4; i++) {
            // Get every player's RemoveCard value and compare the card value
            ArrayList<Card> removedCard = players[i].getRemoveCardInHand(); 
            String leadSuit = leadCard.getLeadCard().getSuit();
            Card card = removedCard.get(0);
            if (card.getSuit().equals(leadSuit)) {
                int rankValue = card.getCardValue();
                if (rankValue > highestRank) {
                    highestRank = rankValue;
                    winnerIndex = i + 1;
                }
            }
        }
        for (int j = 0; j < 4; j++) {
            players[j].clearRemoveCardInHand();
        }
        return winnerIndex;
    }

    private void getPlayerTurn() {
        if (leadCard.getLeadCard().getRank().equals("A") || leadCard.getLeadCard().getRank().equals("5")
                || leadCard.getLeadCard().getRank().equals("9") || leadCard.getLeadCard().getRank().equals("K")) {
            turn = "player 1";
            currentPlayerIndex = 0;
        } else if (leadCard.getLeadCard().getRank().equals("2") || leadCard.getLeadCard().getRank().equals("6")
                || leadCard.getLeadCard().getRank().equals("X")) {
            turn = "player 2";
            currentPlayerIndex = 1;
        } else if (leadCard.getLeadCard().getRank().equals("3") || leadCard.getLeadCard().getRank().equals("7")
                || leadCard.getLeadCard().getRank().equals("J")) {
            turn = "player 3";
            currentPlayerIndex = 2;
        } else {
            turn = "player 4";
            currentPlayerIndex = 3;
        }
    }

    private boolean verifyCardsInHand(String inputString) {
        String suit = inputString.substring(0, 1);
        String rank = inputString.substring(1);
        Card inputCard = new Card(suit, rank);
        ArrayList<Card> hand = players[index].getCardInHand();
        ArrayList<Card> removedCard = players[index].getRemoveCardInHand();   //get the removed card of each player

        if (deck.getCenterdeck().size() != 0) {
            if (hand.contains(inputCard)) {       //if player's hand contains the card, remove the card and add to the center
                if (inputCard.getSuit().equals(leadCard.getLeadCard().getSuit())
                        || inputCard.getRank().equals(leadCard.getLeadCard().getRank())) {
                    hand.remove(inputCard);
                    deck.addToCenterDeck(inputCard);
                    removedCard.add(inputCard);
                    return false;      //stopcontinue=false
                } else {
                    System.out.println(      //if player inputs a card in hand but not following rank or suit, auto draws from the deck
                            "The input card does not match any of the suit or rank. Drawing a card from the deck...");
                    Card drawnCard = deck.deal();
                    if (drawnCard != null) {
                        hand.add(drawnCard);
                        System.out.println("Player drew a card: " + drawnCard);
                    } else {
                        System.out.println("Deck is empty. Player cannot draw a card.");
                    }
                    currentPlayerIndex--;
                    index--;
                    return true; //stopcontinue=true, so remains the same player
                }
            } else {    //if player inputs a card not in hand, auto draws from the deck
                System.out.println("\nThe input card is not in the player's hand. Drawing a card from the deck...");
                Card drawnCard = deck.deal();
                if (drawnCard != null) {
                    hand.add(drawnCard);
                    System.out.println("Player drew a card: " + drawnCard);
                } else {
                    System.out.println("Deck is empty. Player cannot draw a card.");
                }
                currentPlayerIndex--;
                index--;
                return true;
            }
        } else {
            if (hand.contains(inputCard)) {
                hand.remove(inputCard);
                deck.addToCenterDeck(inputCard);
                leadCard.leadCard(inputCard);
                removedCard.add(inputCard);
                return false;
            } else {
                System.out.println("\nThe input card is not in the player's hand. Drawing a card from the deck...");
                Card drawnCard = deck.deal();
                if (drawnCard != null) {
                    hand.add(drawnCard);
                    System.out.println("Player drew a card: " + drawnCard);
                } else {
                    System.out.println("Deck is empty. Player cannot draw a card.");
                }
                currentPlayerIndex--;
                index--;
                return true;
            }
        }
    }

    public void start() {
        int i;
        int trick = 1;
        boolean stopContinue = false; // flag that prevents from continuing, remains the same player
        dealCards();
        boolean flag = true; // indicates 1st round
        boolean findwinner = false; //the displayturn() will find winner for end of every round
        while (true) {
           
            for (i = 0; i < 4; i++) {
                System.out.println();
                if (i != 0) {
                    findwinner = true;
                }
                displayPlayer(trick);
                displayCenter(flag, stopContinue);
                displayDeck();
                displayScore();
                stopContinue = displayTurn(flag, stopContinue, findwinner);
                if (stopContinue) {   //remain the same player
                    i--;
                    continue;
                }
                flag = false;
                System.out.println();
            }

            trick++;
            trickWinnerIndex=calculateTrickWinner();
            System.out.println("Player " + (trickWinnerIndex) + " wins the trick!\n");
            deck.clearCenterDeck();
            findwinner = false;
        }

    }
}
