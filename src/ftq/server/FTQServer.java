package ftq.server;

import ftq.utils.FTQUtils;
import java.net.*;
import java.io.*;
import java.util.List;

/**
 * Server for the Find the Queen CLI game
 * 
 * @author <a href="mailto:rowan.atkinson@gmail.com">Rowan Atkinson</a>
 * @version 1.0
 */
public class FTQServer {

    private static boolean debug = false;
    private static final int port = 7621;
    private boolean keepAlive = true;
    private Connection[] clients;

    public static void usage() {
        System.out.println("Usage: FTQServer <options> \nwhere options include:\t -d \t Debug mode");
    }

    public void endGame(String error_msg){
        // end game session
        for(Connection c : clients) {
            c.out.println(String.format("400-%s@Thanks for Playing", error_msg));
        }
    }

    public void run() {
        try(
            ServerSocket serverSocket = new ServerSocket(port);
            Connection clientSocket1 = new Connection(serverSocket.accept());
            Connection clientSocket2 = new Connection(serverSocket.accept());
        ) {
            // Both clients connected successfully or cancelled login
            if(clientSocket1.loggedIn && clientSocket2.loggedIn) {
                FTQUtils.print("Both clients connected");
                clients = new Connection[]{clientSocket1, clientSocket2};
            } else{
                // end game due o login failures
                System.out.println("login failure");
                endGame("1 or more players failed to login");
            }
            FTQUtils.print(clients);

            

        } catch (Exception e) {
            //TODO: handle exception
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