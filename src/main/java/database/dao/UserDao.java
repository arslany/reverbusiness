package database.dao;

import com.gitlab.mvysny.jdbiorm.Dao;
import database.entity.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for the all the user related CRUD tasks.
 */
public class UserDao extends Dao<User, Long> {

    public UserDao() {
        super(User.class);
    }

    @Nullable
    public User findByUserName(@org.jetbrains.annotations.NotNull String userName) {
        return findSingleBy("userName=:userName", q -> q.bind("userName", userName));
    }

    public User findByUserNameAndPassword(@org.jetbrains.annotations.NotNull String userName, @NotNull String password) {
        Map<String, String> params = new HashMap<>();
        params.put("userName", userName);
        params.put("password", password);
        return findSingleBy("userName=:userName and password=:password", q -> q.bindMap(params));
    }
}
