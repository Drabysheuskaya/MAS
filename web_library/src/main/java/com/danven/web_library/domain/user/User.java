package com.danven.web_library.domain.user;

import com.danven.web_library.domain.config.custom_types.OptionalStringType;
import com.danven.web_library.domain.config.custom_validators.OptionalStringNotEmpty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a user in the web library system.
 */
@Entity
@Table(name = "USERS")
@Inheritance(strategy = InheritanceType.JOINED)
@TypeDefs({
        @TypeDef(name = "optionalString", typeClass = OptionalStringType.class)
})
public abstract class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    @Column(name = "name", nullable = false)
    private String name;

    @OptionalStringNotEmpty(message = "Surname should not be blank")
    @Type(type = "optionalString")
    @Column(name = "surname")
    private Optional<String> surname;

    @CreationTimestamp
    @Column(name = "time_of_registration", nullable = false, updatable = false)
    private LocalDateTime timeOfRegistration;

    @Column(name = "enabled")
    private boolean enabled;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Size(min = 8, message = "The password must contain at least 8 characters.")
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Default constructor for User.
     */
    public User() {
    }

    /**
     * Constructs a new User with the specified details.
     *
     * @param name     the name of the user.
     * @param surname  the surname of the user.
     * @param enabled  the enabled status of the user.
     * @param email    the email address of the user.
     * @param password the password of the user.
     */
    public User(String name, Optional<String> surname, boolean enabled, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.enabled = enabled;
        this.email = email;
        this.password = password;
    }

    /**
     * Constructs a new User with the specified details, without surname.
     *
     * @param name     the name of the user.
     * @param enabled  the enabled status of the user.
     * @param email    the email address of the user.
     * @param password the password of the user.
     */
    public User(String name, boolean enabled, String email, String password) {
        this.name = name;
        this.enabled = enabled;
        this.email = email;
        this.password = password;
    }

    /**
     * Gets the ID of the user.
     *
     * @return the user ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the name of the user.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name the name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the surname of the user.
     *
     * @return the surname.
     */
    public Optional<String> getSurname() {
        return surname;
    }

    /**
     * Sets the surname of the user.
     *
     * @param surname the surname to set.
     */
    public void setSurname(Optional<String> surname) {
        this.surname = surname;
    }

    /**
     * Gets the time of registration of the user.
     *
     * @return the time of registration.
     */
    public LocalDateTime getTimeOfRegistration() {
        return timeOfRegistration;
    }

    /**
     * Gets the enabled status of the user.
     *
     * @return true if the user is enabled, false otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled status of the user.
     *
     * @param enabled the enabled status to set.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets the email address of the user.
     *
     * @return the email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return enabled == user.enabled && Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(timeOfRegistration, user.timeOfRegistration) && Objects.equals(email, user.email) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, timeOfRegistration, enabled, email, password);
    }
}
