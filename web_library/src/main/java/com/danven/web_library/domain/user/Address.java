package com.danven.web_library.domain.user;

import com.danven.web_library.domain.config.custom_types.OptionalStringType;
import com.danven.web_library.domain.config.custom_validators.OptionalStringNotEmpty;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents an address in the library system.
 */
@Embeddable
@TypeDefs({
        @TypeDef(name = "optionalString", typeClass = OptionalStringType.class)
})
public class Address implements Serializable {

    @NotEmpty
    @Column(name = "country", nullable = false)
    private String country;

    @OptionalStringNotEmpty
    @Type(type = "optionalString")
    @Column(name = "city")
    private Optional<String> city;

    @OptionalStringNotEmpty
    @Type(type = "optionalString")
    @Column(name = "street")
    private Optional<String> street;

    @OptionalStringNotEmpty
    @Type(type = "optionalString")
    @Column(name = "house_number")
    private Optional<String> houseNumber;

    @NotEmpty
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    /**
     * Default constructor for Address.
     */
    public Address() {
    }

    /**
     * Constructs a new Address with the specified details.
     *
     * @param country     the country of the address.
     * @param city        the city of the address.
     * @param street      the street of the address.
     * @param houseNumber the house number of the address.
     * @param postalCode  the postal code of the address.
     */
    public Address(String country, Optional<String> city, Optional<String> street,
                   Optional<String> houseNumber, String postalCode) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
    }

    /**
     * Gets the country of the address.
     *
     * @return the country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country of the address.
     *
     * @param country the country to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the city of the address.
     *
     * @return the city.
     */
    public Optional<String> getCity() {
        return city;
    }

    /**
     * Sets the city of the address.
     *
     * @param city the city to set.
     */
    public void setCity(Optional<String> city) {
        this.city = city;
    }

    /**
     * Gets the street of the address.
     *
     * @return the street.
     */
    public Optional<String> getStreet() {
        return street;
    }

    /**
     * Sets the street of the address.
     *
     * @param street the street to set.
     */
    public void setStreet(Optional<String> street) {
        this.street = street;
    }

    /**
     * Gets the house number of the address.
     *
     * @return the house number.
     */
    public Optional<String> getHouseNumber() {
        return houseNumber;
    }

    /**
     * Sets the house number of the address.
     *
     * @param houseNumber the house number to set.
     */
    public void setHouseNumber(Optional<String> houseNumber) {
        this.houseNumber = houseNumber;
    }

    /**
     * Gets the postal code of the address.
     *
     * @return the postal code.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the postal code of the address.
     *
     * @param postalCode the postal code to set.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return Objects.equals(country, address.country) &&
                Objects.equals(city, address.city) &&
                Objects.equals(street, address.street) &&
                Objects.equals(houseNumber, address.houseNumber) &&
                Objects.equals(postalCode, address.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, street, houseNumber, postalCode);
    }
}
