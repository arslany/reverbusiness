package util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;

public class Util {

    private static HashMap<String, LocalTime> validUUid = new HashMap<String, LocalTime>();

    /**
     * this is token time to live in seconds
     */
    private static final long TTL = 60;

    public synchronized static String generateNewUUID() {
        String newUUID = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalTime localTime = localDateTime.toLocalTime();
        validUUid.put(newUUID,localTime);
        return newUUID;
    }

    public static boolean isValidUUID(String uuid){
        //let's see if we have the supplied UUID in the list
        LocalTime uuidTime = validUUid.get(uuid);
        if (null == uuidTime)
            return false;
        else {
            //check if the time have lapsed
            LocalTime currentTime = LocalDateTime.now().toLocalTime();

            long timeElapsed = SECONDS.between(uuidTime,currentTime);
            if (timeElapsed > TTL) {
                validUUid.remove(uuid);
                return false;
            }
            else {
                //update token access time. I.e add new life to token
                validUUid.put(uuid,currentTime);
                return true;
            }
        }

    }

}

