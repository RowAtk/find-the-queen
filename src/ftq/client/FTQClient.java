package ftq.client;

import java.net.*;

import ftq.utils.FTQUtils;

import java.io.*;

/**
 * Client for the Find the Queen CLI game
 * 
 * @author <a href="mailto:rowan.atkinson@gmail.com">Rowan Atkinson</a>
 * @version 1.0
 */
public class FTQClient {

    private static boolean debug = false;

    final String hostname = "localhost";
    final int server_port = 7621;

    public String processMsg(String status_code) throws IOException {
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String response = "";
        switch(status_code){
            case "100": // username
                response = stdIn.readLine();
                break;
            case "101":
                response = new String(System.console().readPassword());
                break;
            case "300":
                break;
            default:
                response = stdIn.readLine();
                
        }
        return response;
    }

    public void printMsg(String[] msgs) {
        for(String msg : msgs) {
            System.out.println(msg);
        }
    }

    public void run() {
        try(
            // init resources
            Socket connection = new Socket(hostname, server_port);
            PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        ){
            FTQUtils.print("client is running");
            boolean connected = true;
            String[] server_msg = new String[2]; 
            String user_msg, raw_server_msg;
            FTQUtils.print(connected);
            while(((raw_server_msg = in.readLine()) != null) && connected){
                FTQUtils.print("in loop");
                server_msg = raw_server_msg.split("-", 2);
                printMsg((server_msg[1].split("@", 0)));

                // server has ended game session
                if(server_msg[0].equals("404"))
                    connected = false;
                
                // server sends instructions
                /*
                if(server_msg[0].equals("101")) // password entry
                    user_msg = new String(System.console().readPassword());
                else
                    user_msg = stdIn.readLine();
                    */

                user_msg = processMsg(server_msg[0]);

                if(user_msg != null)
                    FTQUtils.print("sent response");
                    out.println(user_msg);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void usage() {
        System.out.println("Usage: FTQSClient <options> \nwhere options include:\t -d \t Debug mode");
    }

    public static void main(String[] args) throws IOException {       
        // incorrect usage
        if(args.length > 1) {
            System.out.println("Client expected no more than 1 argument");
            usage();
            System.exit(1);
        }

        // debug config
        if(args.length == 1 && args[0].equals("-d"))
            debug = true;
        
        FTQUtils.debug = debug;

        FTQClient client = new FTQClient();
        client.run();
    }
    
}