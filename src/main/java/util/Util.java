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

    /**
     * This will generate a new token for a user.
     * @param userName
     * @return
     */
    public synchronized static String generateNewUUID(String userName) {
        String newUUID = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalTime localTime = localDateTime.toLocalTime();
        validUUid.computeIfAbsent(newUUID, k -> new HashMap<>()).put(userName, localTime);
        return newUUID;
    }

    /**
     * This method will check if the time to live for supplied token is expired. if Yes then
     * this token is removed from the map and then a false is returned. Otherwise a new expiry
     * time is allocated to this token.
     * token is elapsed.
     * @param uuid
     * @return
     */

    public static boolean validateToken(String uuid){
        String token = uuid;
        //check if token have Bearer in it.
        if (uuid.contains("Bearer"))
            token = uuid.substring(7, uuid.length());       //strip out Bearer

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

    /**
     * This method will check if a token is already present against the user
     * @param userName
     * @return
     */
    public static String isUserAlreadyLoggedIn(String userName) {

        String token = null;

        Optional<String> key = validUUid.entrySet().stream()
                .filter(entry -> entry.getValue().containsKey(userName))
                .map(entry -> entry.getKey())
                .findFirst();

        if (key.isPresent()) {
            if(validateToken(key.get()))
                token = key.get();
        }
        return token;
    }

    public static void logout(String userName) {
        // Assuming validUUid is a Map<String, Map<String, String>>
        validUUid.entrySet().stream()
                .filter(entry -> entry.getValue().containsKey(userName)) // This will filter the outer map entries that have the inner map with userName
                .map(entry -> entry.getKey()) // This will get the keys of those entries
                .forEach(key -> validUUid.remove(key)); // This will remove those keys from the outer map

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

