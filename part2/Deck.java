import java.util.*;

abstract class Deck {
    protected ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<Card>();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card deal() {
        return cards.size() > 0 ? cards.remove(0) : null;
    }

    public void showDeck() {
        System.out.println(cards.toString());
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void setDeck(ArrayList<Card> newDeck){
        this.cards = newDeck;
    }
    public ArrayList<Card> getDeck() {
        return cards;
    }

    public void clearDeck() {
        cards.clear();
    }

    public int size(){
        return cards.size();
    }

    public String getString(int i){
        return cards.get(i).toString();
    }
}

class CenterDeck extends Deck {
}

class CloseDeck extends Deck implements DeckInitialization {
    @Override
    public void initDeck() {
        String[] suits = { "h", "c", "d", "s" };
        String[] ranks = { "2", "3", "4", "5", "6", "7", "8", "9", "X", "J", "Q", "K", "A" };
        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(suit, rank)); // set first then add
            }
        }
    }
}
