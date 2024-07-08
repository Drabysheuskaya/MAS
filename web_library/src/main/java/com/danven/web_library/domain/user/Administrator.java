package com.danven.web_library.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents an administrator in the library system.
 */
@Entity
@Table(name = "ADMINISTRATOR")
public class Administrator extends User {

    @NotEmpty
    @Column(name = "unique_key", nullable = false, updatable = false, unique = true)
    private String uniqueKey;

    /**
     * Default constructor for Administrator.
     */
    public Administrator() {
    }

    /**
     * Constructs a new Administrator with the specified details.
     *
     * @param name      the name of the administrator.
     * @param surname   the surname of the administrator.
     * @param enabled   the enabled status of the administrator.
     * @param email     the email address of the administrator.
     * @param password  the password of the administrator.
     * @param uniqueKey the unique key of the administrator.
     */
    public Administrator(String name, Optional<String> surname, boolean enabled, String email,
                         String password, String uniqueKey) {
        super(name, surname, enabled, email, password);
        this.uniqueKey = uniqueKey;
    }

    /**
     * Constructs a new Administrator with the specified details, without surname.
     *
     * @param name      the name of the administrator.
     * @param enabled   the enabled status of the administrator.
     * @param email     the email address of the administrator.
     * @param password  the password of the administrator.
     * @param uniqueKey the unique key of the administrator.
     */
    public Administrator(String name, boolean enabled, String email, String password, String uniqueKey) {
        super(name, enabled, email, password);
        this.uniqueKey = uniqueKey;
    }

    /**
     * Gets the unique key of the administrator.
     *
     * @return the unique key.
     */
    public String getUniqueKey() {
        return uniqueKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Administrator)) return false;
        if (!super.equals(o)) return false;
        Administrator that = (Administrator) o;
        return Objects.equals(uniqueKey, that.uniqueKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), uniqueKey);
    }
}
