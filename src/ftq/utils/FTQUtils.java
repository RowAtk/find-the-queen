package ftq.utils;

import java.util.List;

public class FTQUtils {

    public static void print(Object o) {
        System.out.println("<" + o.getClass() + "> " + o.toString());
    }

    public static void print(List<Object> list) {
        for (Object o : list) {
            FTQUtils.print(o);
        }
    }
}