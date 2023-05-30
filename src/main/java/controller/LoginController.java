package controller;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import util.Util;

import static util.Util.isNullOrEmpty;

/**
 * This controller exposes user login, logout, reset password end points.
 *
 */
public class LoginController implements  CrudHandler {
    @Override
    public void create(@NotNull Context context) {

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

    public void logout(@NotNull Context context, String userName){
        //TODO need to do a proper update in user table. Last login time, logout time, logged out event. I.e. loged out by user or
        //by time out.
        if (isNullOrEmpty(userName))
            context.status(HttpStatus.OK);
        else {
            Util.logout(userName);
            context.status(HttpStatus.OK);
        }
    }
}
