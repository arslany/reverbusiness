package database.dao;

import com.gitlab.mvysny.jdbiorm.Dao;
import database.entity.Permissions;

public class PermissionsDao extends Dao<Permissions, Long> {

    public PermissionsDao() {
        super(Permissions.class);
    }
}
