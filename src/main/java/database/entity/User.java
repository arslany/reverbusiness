package database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gitlab.mvysny.jdbiorm.Entity;
import com.gitlab.mvysny.jdbiorm.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.time.Instant;
import java.util.Objects;

@Table("Users")
public class User implements Entity<Long> {

    private Long id;

    @NotNull
    @Size(min = 5, max = 50)
    private String userName;

    @NotNull
    @JsonIgnore
    @Size(min = 8, max = 12)
    private String password;

    @NotNull
    @ColumnName("created_at")
    private Instant created;

    @NotNull
    @Size(min = 5, max = 20)
    private String firstname;

    @NotNull
    @Size(min = 5, max = 20)
    private String surname;

    @ColumnName("active")
    private boolean isActive;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long aLong) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", created=" + created +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public void save(boolean validate) {
        // always override the save(boolean) method, and not save().
        // If you only override save(), your code is not going to be called when
        // somebody calls save(boolean).
        if (id == null) {
            created = Instant.now();
        }
        Entity.super.save(validate);
    }
}
