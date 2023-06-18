import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
//place where the code starts running
public class Main {
    private static int currentCardIndex = 0;
    public static void main(String[] args) {
         
        Timer timer = new Timer();
        int delay = 60; // Delay in milliseconds 
    
        // schedules a task, 2000 represent the delay (in milliseconds) before the task is to be executed for the first time
        // delay =60 represent fixed rate or interval (in milliseconds) to be repeatedly executed.
        timer.schedule(new CardDisplayTask(), 2000, delay); 
       
        System.out.println("\nWELCOME TO GO BOOM GAME!\n");
        
        displayMessage();
        Game game = new Game();
        game.start();
        
    }

    //list of all the cards
    private static final String[] cards = {
        "sA", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "sX", "sJ", "sQ", "sK",
        "hA", "h2", "h3", "h4", "h5", "h6", "h7", "h8", "h9", "hX", "hJ", "hQ", "hK",
        "cA", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "cX", "cJ", "cQ", "cK",
        "dA", "d2", "d3", "d4", "d5", "d6", "d7", "d8", "d9", "dX", "dJ", "dQ", "dK"
    };


static class CardDisplayTask extends TimerTask {
    private int cardsPerRow = 13;
    private int cardsInCurrentRow = 0;

    @Override
    public void run() {
        if (currentCardIndex < cards.length) {
            System.out.print(cards[currentCardIndex] + " ");
            currentCardIndex++;
            cardsInCurrentRow++;

            if (cardsInCurrentRow == cardsPerRow) {   //if cards in current row is already 13, make it to a new line
                System.out.println();
                cardsInCurrentRow = 0;    //reset cards in new row to 0
            }
        } else {
            cancel(); // Stop the timer once all cards are displayed
            
        }
    }
}
    
    private static void displayMessage() 
    {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      
        try {
          Thread.sleep(900);   //pause for 900 milliseconds
          System.out.println("Sorting out cards...");
          Thread.sleep(5500);
          System.out.print("\nPress enter to start the game...");
          input.readLine(); //read the input

        } catch (Exception e) {
          e.printStackTrace();
        }
    }
    
    
}
