package ftq.utils;

import java.util.List;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;

public class FTQUtils {

    public static boolean debug = false;

    public static void print(Object o) {
        if(debug)
            // System.out.println("<" + o.getClass() + "> " + o.toString());
            System.out.println(o.toString());
    }

    public static void print(List<Object> list) {
        for (Object o : list) {
            print(o);
        }
    }

    public static void print(Object[] list) {
        for (Object o : list) {
            print(o);
        }
    }

    public static void time() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        print(dtf.format(now)); 
    }
   
}