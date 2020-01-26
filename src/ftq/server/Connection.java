package ftq.server;

import java.net.*;

import ftq.utils.*;

import java.io.*;

/**
 * Class representing client connections in the FTQ server
 * 
 * @author <a href="mailto:rowaneatkinson@gmail.com">Rowan Atkinson</a>
 * @version 1.0
 */
public class Connection implements AutoCloseable {

    public Socket clientSocket;
    public PrintWriter out;
    public BufferedReader in;

    public String username;
    public String password;
    public boolean loggedIn = false;
    public int wins = 0;

    public Connection(Socket s) throws IOException {
        this.clientSocket = s;
        this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        login();
    }

    public Connection(Socket s, String name) throws IOException {
        this.clientSocket = s;
        this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        this.username = name;
        // login();
    }

    private void login() throws IOException {
        boolean cancelled = false;

        while(!loggedIn && !cancelled) {
            try {
                out.println("100-~Player Login~@Enter your username: ");
                // System.out.println("sent username prompt");
                username = in.readLine();
                
                out.println("101-Enter your password: ");
                // System.out.println("sent password prompt");
                password = in.readLine(); 
                
                loggedIn = UserAdmin.login(username, password, out);
                if(!loggedIn)
                    cancelled = !Validation.tryAgain(this);

            } catch(IOException e) {
                out.println("Error logging in to server. Restart client.");
            }
        }
            

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