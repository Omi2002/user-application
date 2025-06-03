package user.com.user.utils;

import java.util.UUID;

public class Util {
    public static String id32(){
         return  UUID.randomUUID().toString().replace("-", "");
    }
}
