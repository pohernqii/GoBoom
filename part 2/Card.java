import java.util.HashMap; 
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.ArrayList;

//Card.java file is a class file that contain the necessary properties and methods to define 
//a card object within the game.
public class Card  {
    private String suit;
    private String rank;
    private Card leadCard;

    public Card(){

    }
    
    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    // Override the equals method to compare suit and rank values
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Card)) {
            return false;
        }
        Card otherCard = (Card) obj;
        return this.suit.equals(otherCard.suit) && this.rank.equals(otherCard.rank);
    }

    public void leadCard(Card leadCard){   //set the lead card
        this.leadCard=leadCard;
        
    }
    
    public Card getLeadCard(){
        return leadCard;
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    public String toString() {
        return suit + rank;
    }
   
	public Map<String, Integer> getCardValues() {   
	    String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "X", "J", "Q", "K", "A"};
		Integer[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        HashMap<String, Integer> cardValues = new HashMap<String, Integer>();
		
		for (int i = 0; i < ranks.length; i++) {
            cardValues.put(ranks[i], values[i]);
        }
		return cardValues;
	}
	
	public Map<String, Integer> getCardScoreValues() {   
	    String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "X", "J", "Q", "K", "A"};
		Integer[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 1};
        HashMap<String, Integer> cardScoreValues = new HashMap<String, Integer>();
		
		for (int i = 0; i < ranks.length; i++) {
            cardScoreValues.put(ranks[i], values[i]);
        }
		return cardScoreValues;
	}
	
	public Set<String> getSuitsSet() {   
		String[] suits = {"h", "c", "d", "s"};
		Set<String> suitsSet = new HashSet<String>();
		for (int i = 0; i < suits.length; i++) {
            suitsSet.add(suits[i]);
        }
		return suitsSet;
	}
	
	public Set<String> getRanksSet() {   
		String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "X", "J", "Q", "K", "A"};
		Set<String> ranksSet = new HashSet<String>();
		for (int i = 0; i < ranks.length; i++) {
            ranksSet.add(ranks[i]);
        }
		return ranksSet;
	}
	
	public boolean isValidCard(String inputSuit, String inputRank, Set<String> suitsSet, Set<String> ranksSet) {
		boolean isValidCard = false;
		if(suitsSet.contains(inputSuit) && ranksSet.contains(inputRank)) isValidCard = true;
		return isValidCard;
    }   
	
	public static Card parseCard(String cardString) {
        if (cardString.trim().length() >=2){
        String suit = cardString.substring(0, 1);
        String rank = cardString.substring(1);
        return new Card(suit, rank);
        }
        else{
            throw new IllegalArgumentException("Invalid card string: " + cardString);
        }
    }

    public static ArrayList<Card> parseCardList(String cardListLine) {
        ArrayList<Card> cards = new ArrayList<>();
        String[] cardStrings = cardListLine.split(" ");
        for (String scard : cardStrings) {
            Card card = parseCard(scard);
            if (card != null) {
                cards.add(card);
            }
        }
        return cards;
    }
 }
