package ftq.server;
/**
 * Server for the Find the Queen CLI game
 * 
 * @author <a href="mailto:rowan.atkinson@gmail.com">Rowan Atkinson</a>
 * @version 1.0
 */
public class FTQServer {

    public static void usage() {
        System.out.println("FTQServer <options> \nwhere options include:\t -d \t Debug mode");
    }

    public static void main(String[] args) {
        boolean debug = false;

        if(args.length > 1) {
            System.out.println("Server expected no more than 1 argument");
            usage();
            System.exit(1);
        }
    }
    
}