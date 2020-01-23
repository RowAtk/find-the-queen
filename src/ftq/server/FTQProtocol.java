package ftq.server;
/**
 * Protocol of communications between Client and Server Endpoints in the Find the Queen CLI game.
 * 
 * @author <a href="mailto:rowan.atkinson@gmail.com">Rowan Atkinson</a>
 * @version 1.0
 */
public class FTQProtocol {

    public String processMsg(String msg) {
        String response = "";
        try {
            int code = Integer.parseInt(msg);

            // switch
        } catch(NumberFormatException e) {
            System.out.println();
        }
        return response;
        
    }    
}