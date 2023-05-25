package controller;

import database.dao.UserDao;
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

public class UserController implements CrudHandler {

    @org.jetbrains.annotations.NotNull
    public static final UserDao dao = new UserDao();

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public void login(Context context){

        UserCredentials user = context.bodyAsClass(UserCredentials.class);
        if (!Util.isNullOrEmpty(user.getUsername()) && (!Util.isNullOrEmpty(user.getPassword()))) {
            Optional<@Nullable User> result = Optional.ofNullable(dao.findByUserNameAndPassword(user.getUsername(), user.getPassword()));
            if (result.isPresent()) {
                //TODO
                //validate it i.e. if user is active, not already logged in, and its password is not already expired

                //before generating a token check if user is already logged in
                if (isUserAlreadyLoggedIn(user.getUsername()))
                    throw new CustomException(CustomException.ErrorCode.USER_ALREADY_LOGGED_IN);

                String token = Util.generateNewUUID(user.getUsername());
                Optional<@Nullable User> userPrinciple = Optional.ofNullable(dao.findByUserName(user.getUsername()));
                if (userPrinciple.isPresent()) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("Token", token);
                    data.put("user", userPrinciple.get());
                    context.json(data);
                    context.status(HttpStatus.OK);
                    context.status(201);
                }
            }
            else{
                throw new CustomException(CustomException.ErrorCode.INVALID_USER_NAME_OR_PASSWORD);
            }
        }
        else{
            throw new CustomException(CustomException.ErrorCode.INVALID_USER_NAME_OR_PASSWORD);
        }

    }

    @Override
    public void create(@NotNull Context context) {
        User newUser = context.bodyAsClass(User.class);
        try {
            newUser.setCreated(Instant.now());
            newUser.setActive(true);
            newUser.save(true);
            context.status(201);
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

    public void findByUserName(@NotNull Context context, String userName) {
        Optional<@Nullable User> result = Optional.ofNullable(dao.findByUserName(userName));
        if (result.isPresent()) {
            context.json(result.get());
            context.status(HttpStatus.OK);
        } else {
            throw new CustomException(CustomException.ErrorCode.USER_NOT_FOUND, userName, true);
        }
    }


}
