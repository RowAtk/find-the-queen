package ftq.utils;

import ftq.server.*;
import java.io.*;

public class Validation {

    public static int readInt(Connection c, String msg) throws IOException{
        int response = 0;
        boolean valid = false;

        while(!valid) {
            c.out.println(msg);
            try {
                response = Integer.parseInt(c.in.readLine());
                if(response < 1 || response > 3) {
                    throw new Exception();
                }
                return response;
            } catch (Exception e) {
                c.out.println("105-Invalid response. Guess must be a number between 1 and 3");
                valid = !tryAgain(c);
            }
        }
        return response;
    }

    public static boolean tryAgain(Connection c) throws IOException {
        String response;
        boolean valid = false;

        while(!valid) {
            c.out.println("104-Try again?(Y|N)");
            response = c.in.readLine();
            if(response.equals("Y") || response.equals("y"))
                return true;

            else if(response.equals("N") || response.equals("n"))
                return false;
            
            c.out.println("105-Response must be 'Y' or 'N'");
        }
        return valid;
    }
}