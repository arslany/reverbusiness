package controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

import java.time.Instant;
import java.util.Optional;

public class UserController implements CrudHandler {

    @org.jetbrains.annotations.NotNull
    public static final UserDao dao = new UserDao();

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public void login(Context context){

        UserCredentials user = context.bodyAsClass(UserCredentials.class);
        context.json(user);
        context.status(201);
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
