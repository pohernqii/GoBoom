import java.util.ArrayList;
//Player.java file would typically contain a class that represents a player in the game. 
//and properties and methods related to the player's hand, actions, and interactions with the game.
public class Player {
    private ArrayList<Card> hand;
    private ArrayList<Card> removedCard;
    private int score;
    
    public Player() {
        hand = new ArrayList<Card>();
        removedCard= new ArrayList<Card>();
    }

    public void addCard(Card card) {  //adding cards in player's hand
        hand.add(card);
    }

    public void showHand() {  
        System.out.print(hand.toString());
    }
    
    public ArrayList<Card> getCardInHand() {
        return hand;   //return the list of player's card in hand
    }
    
    public void setCardInHand(ArrayList<Card> cardInhand){
        this.hand=cardInhand;
    }
    public ArrayList<Card> getRemoveCardInHand(){
        return removedCard;
    }
    
    public void setRemoveCardInHand(ArrayList<Card> removedCardInHand){
        this.removedCard=removedCardInHand;
    }

    public void clearRemoveCardInHand(){
        removedCard.clear();
    }

    public void clearCardInHand(){
        hand.clear();
    }
	
    public void setScore(int score) {
        this.score = score;
    }

    public int getScore(){
        return score;
    }
	
	public boolean isHandEmpty(){
		
        return (hand == null || hand.size() == 0);
    }
/* 
    public static String stringifyCardList(ArrayList<Card> cardList) {
        StringBuilder sb = new StringBuilder();
        for (Card card : cardList) {
            sb.append(card.toString()).append(" ");
        }
        return sb.toString().trim();
    }
    */
}
