package ftq.server;

import ftq.utils.FTQUtils;
import java.net.*;
import java.io.*;
import java.util.List;
import java.util.Random;

/**
 * Server for the Find the Queen CLI game
 * 
 * @author <a href="mailto:rowan.atkinson@gmail.com">Rowan Atkinson</a>
 * @version 1.0
 */
public class FTQServer {

    private static boolean debug = false;
    private static final int port = 7621;
    private Connection[] clients;

    private int dealer = 1;
    private int spotter = 0;
    private boolean keepAlive;

    public static void usage() {
        System.out.println("Usage: FTQServer <options> \nwhere options include:\t -d \t Debug mode");
    }

    public void run() {
        try(
            ServerSocket serverSocket = new ServerSocket(port);
            // Connection clientSocket1 = new Connection(serverSocket.accept(), "Danny");
            // Connection clientSocket2 = new Connection(serverSocket.accept(), "Matty");
            Connection clientSocket1 = new Connection(serverSocket.accept());
            Connection clientSocket2 = new Connection(serverSocket.accept());
        ) {
            // Both clients connected successfully or cancelled login
            clients = new Connection[]{clientSocket1, clientSocket2};
            if(clientSocket1.loggedIn && clientSocket2.loggedIn) {
                FTQUtils.print("Both clients connected");   

                // Start of game 
                FTQUtils.print(clients);
                int turns = 5; // max # of rounds
                dealer = new Random().nextInt(2);
                if(dealer == 0) spotter = 1;
                FTQProtocol protocol = new FTQProtocol(clients[dealer], clients[spotter], 5);
                for(int t = 0; t < turns; t++){
                    keepAlive = true;
                    while(keepAlive) {
                        keepAlive = protocol.processMsg();
                    }
                }
            } else{
                // end game due o login failures
                FTQUtils.print("login failure");
                new FTQProtocol(clientSocket1, clientSocket2, 0).error("500-1 or more players failed to login");
            }

        } catch (Exception e) {
            System.out.println("Server Error " + e.toString());
        }
    }

    public static void main(String[] args) throws IOException {

        // incorrect usage
        if(args.length > 1) {
            System.out.println("Server expected no more than 1 argument");
            usage();
            System.exit(1);
        }

        // debug config
        if(args.length == 1 && args[0].equals("-d"))
            debug = true;
        
        FTQUtils.debug = debug;

        FTQServer server = new FTQServer();
        System.out.println("Server is running...");
        server.run();
    }
    
}