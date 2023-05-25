package util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Util {

    /**
     * This map will contain the user name as key and will have token and the time it was created.
     */
    private static Map<String, Map<String,LocalTime>> validUUid = new HashMap<String, Map<String, LocalTime>>();

    /**
     * this is token time to live in seconds
     */
    private static final long TTL = 60;

    public synchronized static String generateNewUUID(String userName) {
        String newUUID = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalTime localTime = localDateTime.toLocalTime();
        validUUid.computeIfAbsent(newUUID, k -> new HashMap<>()).put(userName, localTime);
        return newUUID;
    }

    public static boolean validateToken(String uuid){

        //strip out Bearer
        String token = uuid.substring(7, uuid.length());

        Map<String, LocalTime> uuidInfo = validUUid.get(token);
        if (null == uuidInfo || uuidInfo.isEmpty())
            return false;
        else {
            String userName = uuidInfo.entrySet().stream().findFirst().get().getKey();
            LocalTime uuidTime = uuidInfo.entrySet().stream().findFirst().get().getValue();
            //check if the time have lapsed
            LocalTime currentTime = LocalDateTime.now().toLocalTime();

            long timeElapsed = SECONDS.between(uuidTime,currentTime);
            if (timeElapsed > TTL) {
                validUUid.remove(token);
                return false;
            }
            else {
                //update token access time. I.e add new life to token
                uuidInfo.put(userName,currentTime);
                validUUid.put(token, uuidInfo);
                return true;
            }
        }

    }

    public static boolean isUserAlreadyLoggedIn(String userName) {

        boolean found = validUUid.values().stream()
                .anyMatch(innerMap -> innerMap.containsKey(userName));
        return found;

    }


    public static boolean isNullOrEmpty(Object o) {
        if (o instanceof String) {
            String value = (String) o;
            return value.trim().isEmpty();
        } else if (o instanceof Number) {
            return isZero(o);
        } else if (o instanceof Instant) {
            return false;
        } else {
            return Optional.ofNullable(o).isEmpty();
        }
    }

    public static boolean isZero(Object o) {
        if (o instanceof Number) {
            Number value = (Number) o;
            return value.doubleValue() == 0.0;
        } else {
            return false;
        }
    }

}

