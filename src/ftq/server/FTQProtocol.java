package ftq.server;

import java.io.IOException;

import ftq.utils.Validation;

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
    private static final int ROUND_END = 4;
    private static final int GAME_END = 5;
    private static final int GAME_CANCELED = 6;

    private Connection dealer;
    private Connection spotter;
    private int round = 0;
    private final int max_rounds;
    private int hiding_spot;

    private int state = START;

    public FTQProtocol(Connection dealer, Connection spotter, int max_rounds){
        this.dealer = dealer;
        this.spotter = spotter;
        this.max_rounds = max_rounds;
    }

    public void switchRoles() {
        Connection swap;
        swap = dealer;
        dealer = spotter;
        spotter = swap;
    }

    public void error(String error_msg) {
        talk(error_msg + "@Please restart game server and clients");
    }

    public boolean endgame(String error_msg){
        talk(String.format("500-%s@Thanks for Playing", error_msg));
        return false;
    }

    public void talk(String msg) {
        dealer.out.println(msg);
        spotter.out.println(msg);
    }

    

    public boolean processMsg() throws IOException{
        boolean keepAlive = true;
        try {
            switch(state) {
                case START:
                if(round == 0)
                    talk("300-Find the Queen game has started!");
                talk(String.format("300-Round %d: %s is the Dealer and %s is the spotter",
                    round + 1, dealer.username, spotter.username));
                case HIDING:
                    // dealer.out.println("301-Choose a position to hide the queen (1 - 3)");
                    spotter.out.println("300-Waiting on dealer to hide the queen");
                    hiding_spot = Validation.readInt(dealer, "301-Choose a position to hide the queen (1 - 3)");
                    // response = dealer.in.readLine();
                    // System.out.println("Response: " + response);
                    if(hiding_spot == 0) {
                        // cancel game
                        state = GAME_CANCELED;
                        break;
                    }
                    state = GUESSING;
                    break;
                case GUESSING:
                    // spotter.out.println();
                    // response = spotter.in.readLine();
                    dealer.out.println("300-Waiting on spotter to find the queen");
                    int guess = Validation.readInt(spotter, String.format("301-Where did %s hide the queen? (1-3)", dealer.username));
                    if(guess == 0) {
                        // cancel game
                        state = GAME_CANCELED;
                        break;
                    } else if(guess == hiding_spot) {
                        talk(String.format("400-The Queen was hidden at %d@The Spotter guessed: %d@%s the Spotter wins round %d", 
                            hiding_spot, guess, spotter.username, round + 1));
                        spotter.wins++;
                    } else {
                        talk(String.format("400-The Queen was hidden at %d@The Spotter guessed: %d@%s the Dealer wins round %d", 
                            hiding_spot, guess, dealer.username, round + 1));
                        dealer.wins++;
                    }
                    state = ROUND_END;
                    break;
                case ROUND_END:
                    round++;
                    switchRoles();
                    if(round >= max_rounds) {
                        state = GAME_END;
                    } else {
                        state = START;
                    }
                    break;

                case GAME_END:
                    Connection winner, loser;
                    if(dealer.wins > spotter.wins) {
                        winner = dealer;
                        loser = spotter;
                    } else {
                        winner = spotter;
                        loser = winner;
                    }
                    talk(String.format("401-%s wins: %d@%s wins: %d", 
                        dealer.username, dealer.wins, spotter.username, spotter.wins));
                    winner.out.println("405-Victory");
                    loser.out.println("405-Defeat");
                    keepAlive = endgame("");
                    break;

                case GAME_CANCELED:
                    error("A client has quit");
                    keepAlive = false;
                    break;
                default:
                    System.out.println("Unknown current state");
                    keepAlive = false;
            } 
        } catch(Exception e) {
            System.out.println("Server Error: " + e.toString());
            keepAlive = false;
        }

        return keepAlive;
        
    }    
}