package com.danven.web_library.domain.report;

import com.danven.web_library.domain.offer.Offer;
import com.danven.web_library.domain.user.Customer;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;

/**
 * Represents a report in the web library system.
 */
@Entity
@Table(name = "REPORT")
public class Report implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "report_id")
    private Long id;

    @Column(name = "description", nullable = false, updatable = false)
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "offer_id", nullable = false, updatable = false)
    private Offer offer;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false)
    private Customer customer;

    /**
     * Default constructor for Report.
     */
    public Report() {
    }

    /**
     * Constructs a new Report with the specified details.
     *
     * @param description the description of the report.
     * @param offer       the offer associated with the report.
     * @param customer    the customer who made the report.
     */
    public Report(String description, Offer offer, Customer customer) {
        this.description = description;
        this.offer = offer;
        offer.addReport(this);
        this.customer = customer;
        customer.addReport(this);
    }

    /**
     * Gets the description of the report.
     *
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the report.
     *
     * @param description the description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the offer associated with the report.
     *
     * @return the offer.
     */
    public Offer getOffer() {
        return offer;
    }

    /**
     * Sets the offer associated with the report.
     *
     * @param offer the offer to set.
     * @throws ConstraintViolationException if the offer is being changed.
     */
    public void setOffer(Offer offer) {
        if (this.offer != offer && offer != null) {
            throw new ConstraintViolationException("Offer of report can't be changed", Collections.emptySet());
        }
        if (offer == null && this.offer != null) {
            this.offer.removeReport(this);
            this.offer = null;
        }
    }

    /**
     * Sets the customer who made the report.
     *
     * @param customer the customer to set.
     * @throws ConstraintViolationException if the customer is being changed.
     */
    public void setCustomer(Customer customer) {
        if (customer != this.customer && customer != null) {
            throw new ConstraintViolationException("Customer of report can't be changed", Collections.emptySet());
        }
        if (customer == null && this.customer != null) {
            this.customer.removeReport(this);
            this.customer = null;
        }
    }

    /**
     * Gets the customer who made the report.
     *
     * @return the customer.
     */
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Report report = (Report) o;
        return id != null && Objects.equals(id, report.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
