package controller;

import database.dao.UserDao;
import database.dto.UserPrinciple;
import database.entity.User;
import exception.CustomException;
import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static util.Util.isUserAlreadyLoggedIn;

/**
 * This class is responsible to handle all the calls related to a user. It implements CrudHandler so that javalin can handle
 * the default end points itself.
 */
public class UserController implements CrudHandler {

    @org.jetbrains.annotations.NotNull
    public static final UserDao dao = new UserDao();

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    /**
     * This method will generate a token after authenticating the user with a supplied password. If no user exists in database then
     * exception will be thrown. If a user is validated, a new token is generated only after it verifies that a token already does not
     * exists against that user.
     * @param context
     *
     *
     * Question: Should there be an indicator in users table which should be updated as logged in and logged out?
     */
    public void login(Context context){
        UserCredentials user = context.bodyAsClass(UserCredentials.class);

        //before generating a token check if user is already logged in
        String previousToken = isUserAlreadyLoggedIn(user.getUsername());
        if (null != previousToken) {
            Map<String, Object> data = new HashMap<>();
            data.put("Token", previousToken);
            data.put("user", CustomException.ErrorCode.USER_ALREADY_LOGGED_IN.getDescription());
            context.json(data);
            context.status(HttpStatus.OK);
            return;
        }

        if (!Util.isNullOrEmpty(user.getUsername()) && (!Util.isNullOrEmpty(user.getPassword()))) {
            Optional<@Nullable User> result = Optional.ofNullable(dao.findByUserNameAndPassword(user.getUsername(), user.getPassword()));
            if (result.isPresent()) {
                //TODO
                //validation
                // 1. user is active,
                // 2. not already logged in,
                // 3. password is not expired
                //END TODO
                String token = Util.generateNewUUID(user.getUsername());
                Optional<@Nullable User> userPrinciple = Optional.ofNullable(dao.findByUserName(user.getUsername()));
                if (userPrinciple.isPresent()) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("Token", token);
                    data.put("user", userPrinciple.get());
                    context.json(data);
                    context.status(HttpStatus.OK);
                }
            }
            else{
                context.result(CustomException.ErrorCode.INVALID_USER_NAME_OR_PASSWORD.getDescription());
                context.status(HttpStatus.UNAUTHORIZED);
            }
        }
        else{
            context.result(CustomException.ErrorCode.INVALID_USER_NAME_OR_PASSWORD.getDescription());
            context.status(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * This method will create a new user in database.
     * @param context
     */
    @Override
    public void create(@NotNull Context context) {
        User newUser = context.bodyAsClass(User.class);
        try {
            newUser.setCreated(Instant.now());
            newUser.setActive(true);
            newUser.save(true);
            context.status(HttpStatus.OK);
        } catch (UnableToExecuteStatementException unableToExecuteStatementException){
            throw new CustomException(CustomException.ErrorCode.UNIQUE_CONSTRAINT_VIOLATION, "User " + newUser.getUserName() + " already exists", unableToExecuteStatementException);
        } catch (Exception e){
            throw new CustomException(CustomException.ErrorCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public void delete(@NotNull Context context, @NotNull String s) {

    }

    @Override
    public void getAll(@NotNull Context context) {

    }

    @Override
    public void getOne(@NotNull Context context, @NotNull String s) {

    }

    @Override
    public void update(@NotNull Context context, @NotNull String s) {

    }

    /**
     * This method will find a user in database against user name / login. It will return the user principle object which will contain
     * user details, role and access permissions granted to the user.
     * @param context
     * @param userName
     */
    public void findByUserName(@NotNull Context context, String userName) {
        Optional<@Nullable User> result = Optional.ofNullable(dao.findByUserName(userName));
        // TODO
        // Currently user entity is directly returned. Instead a user principle class needs to be created and its should be returned.
        // END TODO
        if (result.isPresent()) {
            UserPrinciple userPrinciple = new UserPrinciple();
            User user = result.get();
            userPrinciple.setUserFirstName(user.getUserName());
            userPrinciple.setUserLastName(user.getSurname());

            context.json(result.get());
            context.status(HttpStatus.OK);
        } else {
            throw new CustomException(CustomException.ErrorCode.USER_NOT_FOUND, userName, true);
        }
    }


}
