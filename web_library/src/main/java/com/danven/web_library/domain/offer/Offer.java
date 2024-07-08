package com.danven.web_library.domain.offer;

import com.danven.web_library.domain.book.Book;
import com.danven.web_library.domain.config.custom_types.EnumSetType;
import com.danven.web_library.domain.config.custom_types.OptionalDoubleType;
import com.danven.web_library.domain.config.custom_types.OptionalLocalDateTimeType;
import com.danven.web_library.domain.config.custom_validators.ValidOfferTypes;
import com.danven.web_library.domain.report.Report;

import com.danven.web_library.domain.user.Customer;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Represents an offer in the library system.
 */
@Entity(name = "Offer")
@TypeDefs({
        @TypeDef(name = "optionalLocalDateTime", typeClass = OptionalLocalDateTimeType.class),
        @TypeDef(name = "optionalDouble", typeClass = OptionalDoubleType.class),
        @TypeDef(name = "enumset", typeClass = EnumSetType.class,
                parameters = @org.hibernate.annotations.Parameter(name = "enumClass", value = "com.danven.web_library.domain.offer.OfferType"))
})
@ValidOfferTypes
public class Offer implements BasicOffer, DiscountOffer, LimitedTimeOffer, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "offer_id")
    private Long id;

    @Column(name = "price", nullable = false)
    @Min(value = 0, message = "Price cannot be lower or equal to zero")
    private double price;

    @Column(name = "number_of_copies", nullable = false)
    @Min(value = 0, message = "Number of copies cannot be lower or equal to zero")
    private int numberOfCopies;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "offer")
    private Book book;

    @OneToOne(mappedBy = "offer", optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ContactInfo contactInfo;

    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Report> reports = new HashSet<>();

    @Type(type = "optionalLocalDateTime")
    @Column(name = "end_date")
    private Optional<LocalDateTime> endDate = Optional.empty();

    @Type(type = "optionalLocalDateTime")
    @Column(name = "publishing_time", updatable = false)
    private Optional<LocalDateTime> publishingTime = Optional.empty();

    @Type(type = "optionalDouble")
    @Column(name = "discount")
    private Optional<Double> discount = Optional.empty();

    @Column(name = "publishing_state")
    @Enumerated(EnumType.STRING)
    private PublishState publishState;

    @Type(type = "enumset")
    @Column(name = "offer_type", nullable = false)
    private EnumSet<OfferType> offerTypes;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false, updatable = false)
    private Customer owner;

    @OneToMany(mappedBy = "offer", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<FavouriteOffer> favouriteOffers = new HashSet<>();

    /**
     * Default constructor for Offer.
     */
    public Offer() {
    }

    /**
     * Constructs a new Offer with the specified details.
     *
     * @param price          the price of the offer.
     * @param numberOfCopies the number of copies available.
     * @param book           the book associated with the offer.
     * @param endDate        the end date of the offer, if any.
     * @param publishingTime the publishing time of the offer, if any.
     * @param discount       the discount on the offer, if any.
     * @param publishState   the publish state of the offer.
     * @param offerTypes     the types of the offer.
     * @param owner          the owner of the offer.
     */
    public Offer(double price, int numberOfCopies, Book book,
                 Optional<LocalDateTime> endDate, Optional<LocalDateTime> publishingTime, Optional<Double> discount,
                 PublishState publishState, EnumSet<OfferType> offerTypes, Customer owner) {
        this.price = price;
        this.numberOfCopies = numberOfCopies;
        this.book = book;
        book.setOffer(this);
        this.publishState = publishState;
        this.owner = owner;
        owner.addOwnOffer(this);
        this.offerTypes = offerTypes;
        this.endDate = endDate;
        this.publishingTime = publishingTime;
        this.discount = discount;
    }

    /**
     * Adds a report to the offer.
     *
     * @param report the report to add.
     * @throws ValidationException if the report is invalid.
     */
    public void addReport(Report report) {
        validateReport(report);
        reports.add(report);
    }

    /**
     * Removes a report from the offer.
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
     * Validates the given report.
     *
     * @param report the report to validate.
     * @throws ValidationException if the report is null or attached to another offer.
     */
    private void validateReport(Report report) {
        if (report == null) {
            throw new ValidationException("Report can't be null");
        }
        if (report.getOffer() != this) {
            throw new ValidationException("Report is attached to another offer");
        }
    }

    /**
     * Adds a favourite offer to the offer.
     *
     * @param favouriteOffer the favourite offer to add.
     * @throws ValidationException if the favourite offer is invalid.
     */
    public void addFavouriteOffer(FavouriteOffer favouriteOffer) {
        validateFavouriteOffer(favouriteOffer);
        favouriteOffers.add(favouriteOffer);
    }

    /**
     * Removes a favourite offer from the offer.
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
     * Validates the given favourite offer.
     *
     * @param favouriteOffer the favourite offer to validate.
     * @throws ValidationException if the favourite offer is null or attached to another offer.
     */
    private void validateFavouriteOffer(FavouriteOffer favouriteOffer) {
        if (favouriteOffer == null) {
            throw new ValidationException("FavouriteOffer can't be null");
        }
        if (favouriteOffer.getOffer() != this) {
            throw new ValidationException("FavouriteOffer is attached to another offer");
        }
    }

    /**
     * Sets the owner of the offer.
     *
     * @param owner the owner to set.
     * @throws ValidationException if the owner is being changed.
     */
    public void setOwner(Customer owner) {
        if (owner != null && owner != this.owner) {
            throw new ValidationException("Owner can't be changed in the offer");
        }
        this.owner = owner;
    }

    /**
     * Gets the price with discount applied.
     *
     * @return the price with discount.
     * @throws ValidationException if the offer does not have a discount type.
     */
    @Override
    public double getPriceWithDiscount() {
        if (offerTypes.contains(OfferType.DISCOUNT_OFFER) && discount.isPresent()) {
            return price - ((price / 100.0) * discount.get());
        }
        throw new ValidationException("Role Discount Offer is not present");
    }

    /**
     * Gets the number of days remaining for the offer.
     *
     * @return the number of days remaining.
     * @throws ValidationException if the offer does not have a limited time type.
     */
    @Override
    public int getDaysRemaining() {
        if (offerTypes.contains(OfferType.LIMITED_TIME_OFFER) && endDate.isPresent()) {
            return (int) ChronoUnit.DAYS.between(LocalDateTime.now(), endDate.get());
        }
        throw new ValidationException("Role Limited Time Offer is not present");
    }

    /**
     * Gets the publishing time of the offer.
     *
     * @return the publishing time.
     * @throws ValidationException if the offer does not have a basic offer type.
     */
    @Override
    public LocalDateTime getPublishingTime() {
        if (offerTypes.contains(OfferType.BASIC_OFFER) && publishingTime.isPresent()) {
            return publishingTime.get();
        }
        throw new ValidationException("Role Basic Offer is not present");
    }

    /**
     * Sets the end date of the offer.
     *
     * @param endDate the end date to set.
     */
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = Optional.of(endDate);
    }

    /**
     * Sets the publishing time of the offer.
     *
     * @param publishingTime the publishing time to set.
     */
    public void setPublishingTime(LocalDateTime publishingTime) {
        this.publishingTime = Optional.of(publishingTime);
    }

    /**
     * Sets the discount of the offer.
     *
     * @param discount the discount to set.
     */
    public void setDiscount(double discount) {
        this.discount = Optional.of(discount);
    }

    /**
     * Gets the owner of the offer.
     *
     * @return the owner.
     */
    public Customer getOwner() {
        return owner;
    }

    /**
     * Gets the ID of the offer.
     *
     * @return the offer ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the offer.
     *
     * @param id the offer ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the price of the offer.
     *
     * @return the price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the offer.
     *
     * @param price the price to set.
     * @throws ValidationException if the new price exceeds the old price by more than 20%.
     */
    public void setPrice(double price) {
        if (price > this.price * 1.2) {
            throw new ValidationException("New price cannot be bigger than older one more than 20%");
        }
        this.price = price;
    }

    /**
     * Gets the number of copies available for the offer.
     *
     * @return the number of copies.
     */
    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    /**
     * Sets the number of copies available for the offer.
     *
     * @param numberOfCopies the number of copies to set.
     */
    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    /**
     * Gets the book associated with the offer.
     *
     * @return the book.
     */
    public Book getBook() {
        return book;
    }

    /**
     * Gets the contact information associated with the offer.
     *
     * @return the contact information.
     */
    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    /**
     * Sets the contact information associated with the offer.
     *
     * @param contactInfo the contact information to set.
     * @throws ValidationException if the contact information is invalid.
     */
    void setContactInfo(ContactInfo contactInfo) {
        validateContactInfo(contactInfo);
        if (this.contactInfo != null && this.contactInfo != contactInfo) {
            this.contactInfo.deleteOffer();
        }
        this.contactInfo = contactInfo;
    }

    /**
     * Validates the given contact information.
     *
     * @param contactInfo the contact information to validate.
     * @throws ValidationException if the contact information is null or attached to another offer.
     */
    private void validateContactInfo(ContactInfo contactInfo) {
        if (contactInfo == null || contactInfo.getOffer() != this) {
            throw new ValidationException("Contact info is attached to another offer");
        }
    }

    /**
     * Gets the reports associated with the offer.
     *
     * @return the reports.
     */
    public Set<Report> getReports() {
        return new HashSet<>(reports);
    }

    /**
     * Sets the reports associated with the offer.
     *
     * @param reports the reports to set.
     */
    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }

    /**
     * Gets the end date of the offer.
     *
     * @return the end date.
     */
    public Optional<LocalDateTime> getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the offer.
     *
     * @param endDate the end date to set.
     */
    public void setEndDate(Optional<LocalDateTime> endDate) {
        this.endDate = endDate;
    }

    /**
     * Sets the publishing time of the offer.
     *
     * @param publishingTime the publishing time to set.
     */
    public void setPublishingTime(Optional<LocalDateTime> publishingTime) {
        this.publishingTime = publishingTime;
    }

    /**
     * Gets the discount of the offer.
     *
     * @return the discount.
     */
    public Optional<Double> getDiscount() {
        return discount;
    }

    /**
     * Sets the discount of the offer.
     *
     * @param discount the discount to set.
     */
    public void setDiscount(Optional<Double> discount) {
        this.discount = discount;
    }

    /**
     * Gets the publish state of the offer.
     *
     * @return the publish state.
     */
    public PublishState getPublishState() {
        return publishState;
    }

    /**
     * Sets the publish state of the offer.
     *
     * @param publishState the publish state to set.
     */
    public void setPublishState(PublishState publishState) {
        this.publishState = publishState;
    }

    /**
     * Gets the types of the offer.
     *
     * @return the offer types.
     */
    public EnumSet<OfferType> getOfferTypes() {
        return EnumSet.copyOf(offerTypes);
    }

    /**
     * Sets the types of the offer.
     *
     * @param offerTypes the offer types to set.
     */
    public void setOfferTypes(EnumSet<OfferType> offerTypes) {
        this.offerTypes = offerTypes;
    }

    /**
     * Gets the favourite offers associated with the offer.
     *
     * @return the favourite offers.
     */
    public Set<FavouriteOffer> getFavouriteOffers() {
        return new HashSet<>(favouriteOffers);
    }

    /**
     * Sets the favourite offers associated with the offer.
     *
     * @param favouriteOffers the favourite offers to set.
     */
    public void setFavouriteOffers(Set<FavouriteOffer> favouriteOffers) {
        this.favouriteOffers = favouriteOffers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Offer)) return false;
        Offer offer = (Offer) o;
        return Double.compare(offer.price, price) == 0 && numberOfCopies == offer.numberOfCopies && Objects.equals(id, offer.id) && Objects.equals(book, offer.book) && Objects.equals(contactInfo, offer.contactInfo) && Objects.equals(reports, offer.reports) && Objects.equals(endDate, offer.endDate) && Objects.equals(publishingTime, offer.publishingTime) && Objects.equals(discount, offer.discount) && publishState == offer.publishState && Objects.equals(offerTypes, offer.offerTypes) && Objects.equals(owner, offer.owner) && Objects.equals(favouriteOffers, offer.favouriteOffers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, numberOfCopies, book, contactInfo, reports, endDate, publishingTime, discount, publishState, offerTypes, owner, favouriteOffers);
    }
}
