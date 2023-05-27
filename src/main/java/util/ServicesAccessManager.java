package util;

import exception.CustomException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.security.AccessManager;
import io.javalin.security.RouteRole;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * This clas is responsible to validate the roles of a user who have requested to acces an
 * end point. If a roll contain AccessPointRoles.ANYONE (so far login service) then that end point
 * is access to every caller. For all the other rest points, header will be checked for a valid
 * token and only then access will be granted. Otherwise exception will be thrown
 */
public class ServicesAccessManager implements AccessManager {
    @Override
    public void manage(@NotNull Handler handler, @NotNull Context context, @NotNull Set<? extends RouteRole> set) throws Exception {

        //For login service
        if (set.contains(AccessPointRoles.ANYONE)){
            handler.handle(context);
            return;
        }

        String token = context.header("Authorization");
        if (Util.isNullOrEmpty(token))
            throw new CustomException(CustomException.ErrorCode.AUTHENTICATION_REQUIRED);
        if (Util.validateToken(token))
            handler.handle(context);
        else
            throw new CustomException(CustomException.ErrorCode.AUTHENTICATION_REQUIRED);
    }


}
