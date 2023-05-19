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
    
    public int getCardValue() {   //get all the card value
        int rankValue = 0;
        switch (getRank()) {
            case "A":
                rankValue = 14;
                break;
            case "K":
                rankValue = 13;
                break;
            case "Q":
                rankValue = 12;
                break;
            case "J":
                rankValue = 11;
                break;
            case "X":
                rankValue = 10;
                break;
            case "9":
                rankValue = 9;
                break;
            case "8":
                rankValue = 8;
                break;
            case "7":
                rankValue = 7;
                break;
            case "6":
                rankValue = 6;
                break;
            case "5":
                rankValue = 5;
                break;
            case "4":
                rankValue = 4;
                break;
            case "3":
                rankValue = 3;
                break;
            case "2":
                rankValue = 2;
                break;
            default:
                rankValue = 0;
                break;

        }
        return rankValue;
    }
    
 }
