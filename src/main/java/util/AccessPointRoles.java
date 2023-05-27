package util;

import io.javalin.security.RouteRole;

import java.util.function.Predicate;

/**
 * This enum is created so that rest end points are secured with the roles of a user.
 */

public enum AccessPointRoles implements RouteRole, Predicate<RouteRole> {
    ADMIN, USER, GUEST, ANYONE;

    @Override
    public boolean test(RouteRole routeRole) {
        return routeRole.equals(ANYONE) ;
    }
}


