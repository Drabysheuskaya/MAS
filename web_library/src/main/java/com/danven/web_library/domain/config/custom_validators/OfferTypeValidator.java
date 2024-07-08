package com.danven.web_library.domain.config.custom_validators;

import com.danven.web_library.domain.offer.Offer;
import com.danven.web_library.domain.offer.OfferType;

import javax.validation.*;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Optional;

/**
 * Validator to ensure the consistency and validity of an Offer based on its offer types.
 */
public class OfferTypeValidator implements ConstraintValidator<ValidOfferTypes, Offer> {

    /**
     * Initializes the validator in preparation for isValid calls.
     *
     * @param constraintAnnotation the annotation instance for a given constraint declaration.
     */
    @Override
    public void initialize(ValidOfferTypes constraintAnnotation) {
        // No initialization required for this validator
    }

    /**
     * Implements the validation logic for Offer.
     *
     * @param offer the Offer instance to validate.
     * @param context context in which the constraint is evaluated.
     * @return false if the validation fails, true otherwise.
     */
    @Override
    public boolean isValid(Offer offer, ConstraintValidatorContext context) {
        EnumSet<OfferType> offerTypes = offer.getOfferTypes();
        Optional<LocalDateTime> endDate = offer.getEndDate();
        LocalDateTime publishingTime = null;
        try {
            publishingTime = offer.getPublishingTime();
        } catch (ValidationException ignored) {
        }
        Optional<Double> discount = offer.getDiscount();

        boolean valid = true;

        if (offerTypes.contains(OfferType.BASIC_OFFER) && publishingTime == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Publishing time can't be null for BASIC_OFFER")
                    .addPropertyNode("publishingTime")
                    .addConstraintViolation();
            valid = false;
        }
        if (offerTypes.contains(OfferType.DISCOUNT_OFFER) && discount.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Discount can't be null for DISCOUNT_OFFER")
                    .addPropertyNode("discount")
                    .addConstraintViolation();
            valid = false;
        }
        if (offerTypes.contains(OfferType.LIMITED_TIME_OFFER) && endDate.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("End date can't be null for LIMITED_TIME_OFFER")
                    .addPropertyNode("endDate")
                    .addConstraintViolation();
            valid = false;
        }

        if (!offerTypes.contains(OfferType.BASIC_OFFER) && publishingTime != null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Publishing time should not be set if BASIC_OFFER is not present")
                    .addPropertyNode("publishingTime")
                    .addConstraintViolation();
            valid = false;
        }
        if (!offerTypes.contains(OfferType.DISCOUNT_OFFER) && discount.isPresent()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Discount should not be set if DISCOUNT_OFFER is not present")
                    .addPropertyNode("discount")
                    .addConstraintViolation();
            valid = false;
        }
        if (!offerTypes.contains(OfferType.LIMITED_TIME_OFFER) && endDate.isPresent()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("End date should not be set if LIMITED_TIME_OFFER is not present")
                    .addPropertyNode("endDate")
                    .addConstraintViolation();
            valid = false;
        }

        if (!offerTypes.contains(OfferType.BASIC_OFFER)) {
            try {
                offer.getPublishingTime();
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Role Basic Offer is not present, but getPublishingTime() was called")
                        .addConstraintViolation();
                valid = false;
            } catch (ValidationException e) {
                // Ignore exception
            }
        }

        if (!offerTypes.contains(OfferType.DISCOUNT_OFFER)) {
            try {
                offer.getPriceWithDiscount();
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Role Discount Offer is not present, but getPriceWithDiscount() was called")
                        .addConstraintViolation();
                valid = false;
            } catch (ValidationException e) {
                // Ignore exception
            }
        }

        if (!offerTypes.contains(OfferType.LIMITED_TIME_OFFER)) {
            try {
                offer.getDaysRemaining();
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Role Limited Time Offer is not present, but getDaysRemaining() was called")
                        .addConstraintViolation();
                valid = false;
            } catch (ValidationException e) {
                // Ignore exception
            }
        }

        return valid;
    }
}
