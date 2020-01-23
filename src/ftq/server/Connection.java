package ftq.server;

import java.net.*;

import ftq.utils.FTQUtils;

import java.io.*;

/**
 * Class representing client connections in the FTQ server
 * 
 * @author <a href="mailto:rowaneatkinson@gmail.com">Rowan Atkinson</a>
 * @version 1.0
 */
public class Connection implements AutoCloseable {

    Socket clientSocket;
    PrintWriter out;
    BufferedReader in;

    String username;
    String password;
    boolean loggedIn = false;

    public Connection(Socket s) throws IOException {
        this.clientSocket = s;
        this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        System.out.println("Client connected");
        login();
    }

    private void login() throws IOException {
        boolean cancelled = false;

        while(!loggedIn && !cancelled) {
            System.out.println("logging in client");
            try {
                out.println("100-~Player Login~@Enter your username: ");
                System.out.println("sent username prompt");
                username = in.readLine();
                
                out.println("101-Enter your password: ");
                System.out.println("sent password prompt");
                password = in.readLine(); 
                
                loggedIn = UserAdmin.login(username, password, out);
                if(!loggedIn)
                    cancelled = !yesOrNo();

            } catch(IOException e) {
                out.println("Error logging in to server. Restart client.");
            }
        }
            

    }

    public boolean yesOrNo() throws IOException {
        String response;
        boolean valid = false;

        while(!valid) {
            response = in.readLine();
            if(response.equals("Y") || response.equals("y"))
                return true;

            else if(response.equals("N") || response.equals("n"))
                return false;
            
            out.println("104-Try again?(Y|N)");
        }
        return valid;
    }

    @Override
    public void close() throws Exception {
        this.clientSocket.close();
        this.out.close();
        this.in.close();
    }

    @Override
    public String toString() {
        return String.format("Username: %s\nPassword: %s\nSocket: %s", this.username, this.password, this.clientSocket);
    }

}