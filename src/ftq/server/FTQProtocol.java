package ftq.server;

import java.io.IOException;

/**
 * Protocol of communications between Client and Server Endpoints in the Find the Queen CLI game.
 * 
 * @author <a href="mailto:rowan.atkinson@gmail.com">Rowan Atkinson</a>
 * @version 1.0
 */
public class FTQProtocol {

    // 100 -
    
    private static final int START = 1;
    private static final int HIDING = 2;
    private static final int GUESSING = 3;

    private boolean keepAlive = true;
    private Connection dealer;
    private Connection spotter;
    private int round;
    private int hiding_spot;

    private int state = START;

    public FTQProtocol(Connection dealer, Connection spotter, int round) {
        this.dealer = dealer;
        this.spotter = spotter;
        this.round = round;
    }

    public void endGame(String error_msg){
        // end game session
        talk(String.format("400-%s@Thanks for Playing", error_msg));
        this.keepAlive = false;
    }

    public void talk(String msg) {
        dealer.out.println(msg);
        spotter.out.println(msg);
    }

    public boolean processMsg() throws IOException{
        boolean keepAlive = true;
        String response;
        switch(state) {
            case START:
                talk(String.format("300-Find the Queen game has started!@%s is the Dealer and %s is the spotter",
                    dealer.username, spotter.username));
            case HIDING:
                dealer.out.println("301-Choose a position to hide the queen (1 - 3)");
                response = dealer.in.readLine();
                // System.out.println("Response: " + response);
                hiding_spot = Integer.parseInt(response);
                state = GUESSING;
                break;
            case GUESSING:
                spotter.out.println(String.format("302-Where did %s hide the queen? (1-3)", dealer.username));
                response = spotter.in.readLine();
                int guess = Integer.parseInt(response);
                if(guess == hiding_spot) {
                    talk(String.format("400-%s the Spotter wins round %d", spotter.username, round + 1));
                    spotter.wins++;
                } else {
                    talk(String.format("400-%s the Dealer wins round %d", dealer.username, round + 1));
                    dealer.wins++;
                }
                break;
            default:
                System.out.println("Unknown current state");
                keepAlive = false;
        }

        return keepAlive;
        
    }    
}