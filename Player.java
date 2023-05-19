import java.util.ArrayList;

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

    public ArrayList<Card> getRemoveCardInHand(){
        return removedCard;
    }
    
    public void clearRemoveCardInHand(){
        removedCard.clear();
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore(){
        return score;
    }
}
