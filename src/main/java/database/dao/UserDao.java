package database.dao;

import com.gitlab.mvysny.jdbiorm.Dao;
import database.entity.User;
import org.jetbrains.annotations.Nullable;

public class UserDao extends Dao<User, Long> {

    public UserDao() {
        super(User.class);
    }

    @Nullable
    public User findByUserName(@org.jetbrains.annotations.NotNull String userName) {
        return findSingleBy("userName=:userName", q -> q.bind("userName", userName));
    }
}
