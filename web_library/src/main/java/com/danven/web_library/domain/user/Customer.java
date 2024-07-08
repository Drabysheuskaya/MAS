package com.danven.web_library.domain.user;

import com.danven.web_library.domain.offer.FavouriteOffer;
import com.danven.web_library.domain.report.Report;
import com.danven.web_library.domain.offer.Offer;

import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Represents a customer in the library system.
 */
@Entity
@Table(name = "CUSTOMER")
public class Customer extends User {

    @Pattern(regexp = "^\\+?\\d{1,15}$", message = "Invalid phone number")
    @Column(name = "telephone_number")
    private String telephoneNumber;

    @NotNull(message = "Date of birth can't be null")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull
    @Embedded
    private Address address;

    @OneToMany(mappedBy = "customer", cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Set<Report> reports = new HashSet<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Offer> ownedOffers = new HashSet<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<FavouriteOffer> favouriteOffers = new HashSet<>();

    /**
     * Default constructor for Customer.
     */
    public Customer() {
    }

    /**
     * Constructs a new Customer with the specified details.
     *
     * @param name           the name of the customer.
     * @param surname        the surname of the customer.
     * @param enabled        the enabled status of the customer.
     * @param email          the email address of the customer.
     * @param password       the password of the customer.
     * @param telephoneNumber the telephone number of the customer.
     * @param dateOfBirth    the date of birth of the customer.
     * @param address        the address of the customer.
     */
    public Customer(String name, Optional<String> surname, boolean enabled, String email, String password,
                    String telephoneNumber, LocalDate dateOfBirth, Address address) {
        super(name, surname, enabled, email, password);
        this.telephoneNumber = telephoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    /**
     * Constructs a new Customer with the specified details, without surname.
     *
     * @param name           the name of the customer.
     * @param enabled        the enabled status of the customer.
     * @param email          the email address of the customer.
     * @param password       the password of the customer.
     * @param telephoneNumber the telephone number of the customer.
     * @param dateOfBirth    the date of birth of the customer.
     * @param address        the address of the customer.
     */
    public Customer(String name, boolean enabled, String email, String password, String telephoneNumber,
                    LocalDate dateOfBirth, Address address) {
        super(name, enabled, email, password);
        this.telephoneNumber = telephoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    /**
     * Adds an offer to the customer's owned offers.
     *
     * @param offer the offer to add.
     * @throws ConstraintViolationException if the offer is invalid.
     */
    public void addOwnOffer(Offer offer) {
        validateOffer(offer);
        ownedOffers.add(offer);
    }

    /**
     * Removes an offer from the customer's owned offers.
     *
     * @param offer the offer to remove.
     */
    public void removeOwnOffer(Offer offer) {
        if (ownedOffers.contains(offer)) {
            ownedOffers.remove(offer);
            offer.setOwner(null);
        }
    }

    /**
     * Adds a favourite offer to the customer's favourite offers.
     *
     * @param favouriteOffer the favourite offer to add.
     * @throws ConstraintViolationException if the favourite offer is invalid.
     */
    public void addFavouriteOffer(FavouriteOffer favouriteOffer) {
        validateFavouriteOffer(favouriteOffer);
        favouriteOffers.add(favouriteOffer);
    }

    /**
     * Removes a favourite offer from the customer's favourite offers.
     *
     * @param favouriteOffer the favourite offer to remove.
     */
    public void removeFavouriteOffer(FavouriteOffer favouriteOffer) {
        if (favouriteOffers.contains(favouriteOffer)) {
            favouriteOffers.remove(favouriteOffer);
            favouriteOffer.removeFavouriteOffer();
        }
    }

    /**
     * Adds a report to the customer's reports.
     *
     * @param report the report to add.
     * @throws ConstraintViolationException if the report is invalid.
     */
    public void addReport(Report report) {
        validateReport(report);
        reports.add(report);
    }

    /**
     * Removes a report from the customer's reports.
     *
     * @param report the report to remove.
     */
    public void removeReport(Report report) {
        if (reports.contains(report)) {
            reports.remove(report);
            report.setCustomer(null);
        }
    }

    /**
     * Validates the given offer.
     *
     * @param offer the offer to validate.
     * @throws ConstraintViolationException if the offer is null or attached to another customer.
     */
    private void validateOffer(Offer offer) {
        if (offer == null) {
            throw new ConstraintViolationException("Offer can't be null", Collections.emptySet());
        }
        if (offer.getOwner() != this) {
            throw new ConstraintViolationException("Offer is attached to another customer", Collections.emptySet());
        }
    }

    /**
     * Validates the given favourite offer.
     *
     * @param favouriteOffer the favourite offer to validate.
     * @throws ConstraintViolationException if the favourite offer is null or attached to another customer.
     */
    private void validateFavouriteOffer(FavouriteOffer favouriteOffer) {
        if (favouriteOffer == null) {
            throw new ConstraintViolationException("FavouriteOffer can't be null", Collections.emptySet());
        }
        if (favouriteOffer.getCustomer() != this) {
            throw new ConstraintViolationException("FavouriteOffer is attached to another customer", Collections.emptySet());
        }
    }

    /**
     * Validates the given report.
     *
     * @param report the report to validate.
     * @throws ConstraintViolationException if the report is null or attached to another customer.
     */
    private void validateReport(Report report) {
        if (report == null) {
            throw new ConstraintViolationException("Report can't be null", Collections.emptySet());
        }
        if (report.getCustomer() != this) {
            throw new ConstraintViolationException("Report is attached to another customer", Collections.emptySet());
        }
    }

    /**
     * Gets the telephone number of the customer.
     *
     * @return the telephone number.
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * Sets the telephone number of the customer.
     *
     * @param telephoneNumber the telephone number to set.
     */
    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    /**
     * Gets the date of birth of the customer.
     *
     * @return the date of birth.
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth of the customer.
     *
     * @param dateOfBirth the date of birth to set.
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets the address of the customer.
     *
     * @return the address.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Sets the address of the customer.
     *
     * @param address the address to set.
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Gets the reports made by the customer.
     *
     * @return the reports.
     */
    public Set<Report> getReports() {
        return new HashSet<>(reports);
    }

    /**
     * Gets the offers owned by the customer.
     *
     * @return the owned offers.
     */
    public Set<Offer> getOwnedOffers() {
        return new HashSet<>(ownedOffers);
    }

    /**
     * Gets the favourite offers of the customer.
     *
     * @return the favourite offers.
     */
    public Set<FavouriteOffer> getFavouriteOffers() {
        return new HashSet<>(favouriteOffers);
    }

    /**
     * Gets the age of the customer.
     *
     * @return the age.
     */
    public int getAge() {
        return (int) ChronoUnit.YEARS.between(dateOfBirth, LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(telephoneNumber, customer.telephoneNumber) && Objects.equals(dateOfBirth, customer.dateOfBirth) && Objects.equals(address, customer.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), telephoneNumber, dateOfBirth, address);
    }
}
