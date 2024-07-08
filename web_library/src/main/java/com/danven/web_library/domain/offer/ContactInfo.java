package com.danven.web_library.domain.offer;

import com.danven.web_library.domain.config.custom_types.OptionalStringType;
import com.danven.web_library.domain.config.custom_validators.OptionalStringNotEmpty;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents the contact information for an offer in the web library system.
 */
@Entity
@Table(name = "CONTACT_INFO")
@TypeDefs({
        @TypeDef(name = "optionalString", typeClass = OptionalStringType.class)
})
public class ContactInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "contact_info_id")
    private Long id;

    @NotBlank
    @Column(name = "email", nullable = false)
    @Pattern(regexp = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$", message = "Email has invalid format")
    private String email;

    @OptionalStringNotEmpty
    @Type(type = "optionalString")
    @Column(name = "telephone_number")
    private Optional<String> telephoneNumber;

    @OptionalStringNotEmpty
    @Type(type = "optionalString")
    @Column(name = "social_media_link")
    private Optional<String> socialMediaLink;

    @OneToOne
    @JoinColumn(name = "offer_id", nullable = false, updatable = false)
    private Offer offer;

    /**
     * Default constructor for ContactInfo.
     */
    public ContactInfo() {
    }

    /**
     * Constructs a new ContactInfo with the specified details.
     *
     * @param email            the email address.
     * @param telephoneNumber  the telephone number.
     * @param socialMediaLink  the social media link.
     * @param offer            the associated offer.
     */
    public ContactInfo(String email, Optional<String> telephoneNumber, Optional<String> socialMediaLink, Offer offer) {
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.socialMediaLink = socialMediaLink;
        this.offer = offer;
        offer.setContactInfo(this);
    }

    /**
     * Gets the ID of the contact information.
     *
     * @return the contact information ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the email address.
     *
     * @return the email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the telephone number.
     *
     * @return the telephone number.
     */
    public Optional<String> getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * Gets the social media link.
     *
     * @return the social media link.
     */
    public Optional<String> getSocialMediaLink() {
        return socialMediaLink;
    }

    /**
     * Gets the associated offer.
     *
     * @return the offer.
     */
    public Offer getOffer() {
        return offer;
    }

    /**
     * Sets the email address.
     *
     * @param email the email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the telephone number.
     *
     * @param telephoneNumber the telephone number to set.
     */
    public void setTelephoneNumber(Optional<String> telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    /**
     * Sets the social media link.
     *
     * @param socialMediaLink the social media link to set.
     */
    public void setSocialMediaLink(Optional<String> socialMediaLink) {
        this.socialMediaLink = socialMediaLink;
    }

    /**
     * Sets the associated offer.
     *
     * @param offer the offer to set.
     */
    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    /**
     * Deletes the association with the offer.
     */
    void deleteOffer() {
        this.offer = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ContactInfo that = (ContactInfo) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
