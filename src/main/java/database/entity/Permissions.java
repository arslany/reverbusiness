package database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gitlab.mvysny.jdbiorm.Entity;
import com.gitlab.mvysny.jdbiorm.Table;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Table("PERMISSIONS")
public class Permissions implements Entity<Long> {

    @JsonIgnore
    private Long id;
    private String code;
    private String name;
    private String description;
    private boolean active;

    public Permissions() {
    }

    @Override
    public @Nullable Long getId() {
        return id;
    }

    @Override
    public void setId(@Nullable Long aLong) {
        id = aLong;
    }
    @Override
    public void save(boolean validate) {
        Entity.super.save(validate);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Code will be unique
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permissions that = (Permissions) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "Permissions{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", active=" + active +
                '}';
    }
}
