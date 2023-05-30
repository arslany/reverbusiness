package controller;

import database.dao.PermissionsDao;
import database.entity.Permissions;
import exception.CustomException;
import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;

public class PermissionsController implements CrudHandler {

    private static final PermissionsDao dao = new PermissionsDao();
    private static final Logger log = LoggerFactory.getLogger(PermissionsController.class);

    @Override
    public void delete(@NotNull Context context, @NotNull String s) {

    }

    @Override
    public void getAll(@NotNull Context context) {
        context.json(dao.findAll());
        context.status(HttpStatus.OK);
    }

    @Override
    public void getOne(@NotNull Context context, @NotNull String s) {

    }

    @Override
    public void update(@NotNull Context context, @NotNull String s) {

    }

    @Override
    public void create(@NotNull Context context) {
        Permissions permission = context.bodyAsClass(Permissions.class);
        if (Util.isNullOrEmpty(permission.getCode()))
            throw new CustomException(CustomException.ErrorCode.PERMISSIONS_CODE_MANDATORY);
        else if (Util.isNullOrEmpty(permission.getName()))
            throw new CustomException(CustomException.ErrorCode.PERMISSIONS_NAME_MANDATORY);
        else if (Util.isNullOrEmpty(permission.getDescription()))
            throw new CustomException(CustomException.ErrorCode.PERMISSIONS_DESCRIPTION_MANDATORY);
        else if (Util.isNullOrEmpty(permission.isActive()))
            throw new CustomException(CustomException.ErrorCode.PERMISSIONS_ACTIVE_MANDATORY);

        try{
            permission.save(true);
            context.status(HttpStatus.OK);
        }
        catch (Exception e){
            throw new CustomException(CustomException.ErrorCode.INTERNAL_SERVER_ERROR, e);
        }
    }
}
