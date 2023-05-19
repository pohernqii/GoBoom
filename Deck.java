 import java.util.*;
 
 public class Deck {
    private ArrayList<Card> cards;
    private ArrayList<Card> centerdeck;

    public Deck() {
        String[] suits = {"h", "c", "d", "s"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "X", "J", "Q", "K", "A"};

        cards = new ArrayList<Card>();
        centerdeck= new ArrayList<Card>();

        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(suit, rank)); //set first then add
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card deal() {   
        return cards.remove(0);
    }

    public void showDeck() {
       System.out.println( cards.toString());
    }

    public void clearCenterDeck(){
       centerdeck.clear();
    }

    public void addFirstCardcenterDeck(){   //adding first card from deck to the centerdeck
        centerdeck.add(cards.remove(0));
    }

    public void addToCenterDeck(Card inputCard ){
        centerdeck.add(inputCard);
    }

    public ArrayList<Card> getCenterdeck() {
        return centerdeck;
    }

    public void showcenterDeck(){
        System.out.println(centerdeck.toString());
    }



    

}

