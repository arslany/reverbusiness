package util;

import io.javalin.security.RouteRole;

import java.util.function.Predicate;

public enum AccessPointRoles implements RouteRole, Predicate<RouteRole> {
    ADMIN, USER, GUEST, ANYONE;

    @Override
    public boolean test(RouteRole routeRole) {
        return routeRole.equals(ANYONE) ;
    }
}


