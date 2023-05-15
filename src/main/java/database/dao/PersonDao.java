package database.dao;

import com.gitlab.mvysny.jdbiorm.Dao;
import database.entity.Person;
import org.jetbrains.annotations.Nullable;

public class PersonDao extends Dao<Person, Long> {

    /**
     * protected so that PersonDao won't pop up in the IDE auto-completion
     * dialog when you type in {@code new Person}.
     */
    protected PersonDao() {
        super(Person.class);
    }

    @Nullable
    public Person findByName(@org.jetbrains.annotations.NotNull String name) {
        return findSingleBy("name=:name", q -> q.bind("name", name));
    }

}
