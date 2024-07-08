package com.danven.web_library.domain.offer;

import com.danven.web_library.domain.config.custom_validators.ValidFavouriteOffer;
import com.danven.web_library.domain.user.Customer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Represents a favourite offer in the library system.
 */
@Entity
@Table(name = "FAVOURITE_OFFER")
@ValidFavouriteOffer
public class FavouriteOffer {

    private static String defaultDescription = "No description";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "favourite_offer_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "offer_id", nullable = false, updatable = false)
    private Offer offer;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false, updatable = false)
    private Customer customer;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description = defaultDescription;

    /**
     * Default constructor for FavouriteOffer.
     */
    public FavouriteOffer() {
    }

    /**
     * Constructs a new FavouriteOffer with the specified details.
     *
     * @param offer      the offer to be marked as favourite.
     * @param customer   the customer who marks the offer as favourite.
     * @param description the description of the favourite offer.
     */
    public FavouriteOffer(Offer offer, Customer customer, String description) {
        this.offer = offer;
        offer.addFavouriteOffer(this);
        this.customer = customer;
        customer.addFavouriteOffer(this);
        this.description = description;
    }

    /**
     * Removes this favourite offer.
     */
    public void removeFavouriteOffer() {
        if (this.offer != null) {
            offer.removeFavouriteOffer(this);
        }
        if (this.customer != null) {
            customer.removeFavouriteOffer(this);
        }
        this.offer = null;
        this.customer = null;
    }

    /**
     * Gets the offer marked as favourite.
     *
     * @return the offer.
     */
    public Offer getOffer() {
        return offer;
    }

    /**
     * Gets the customer who marked the offer as favourite.
     *
     * @return the customer.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Gets the description of the favourite offer.
     *
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the default description for favourite offers.
     *
     * @return the default description.
     */
    public static String getDefaultDescription() {
        return defaultDescription;
    }

    /**
     * Sets the default description for favourite offers.
     *
     * @param defaultDescription the default description to set.
     */
    public static void setDefaultDescription(String defaultDescription) {
        FavouriteOffer.defaultDescription = defaultDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavouriteOffer)) return false;
        FavouriteOffer that = (FavouriteOffer) o;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description);
    }
}
