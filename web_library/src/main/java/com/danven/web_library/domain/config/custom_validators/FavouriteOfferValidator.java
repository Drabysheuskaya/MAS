package com.danven.web_library.domain.config.custom_validators;

import com.danven.web_library.domain.offer.FavouriteOffer;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator for FavouriteOffer to ensure that users cannot add their own offers to favourites.
 */
public class FavouriteOfferValidator implements ConstraintValidator<ValidFavouriteOffer, FavouriteOffer> {

    /**
     * Initializes the validator in preparation for isValid calls.
     *
     * @param constraintAnnotation the annotation instance for a given constraint declaration.
     */
    @Override
    public void initialize(ValidFavouriteOffer constraintAnnotation) {
        // No initialization required for this validator
    }

    /**
     * Implements the validation logic for FavouriteOffer.
     *
     * @param favouriteOffer the FavouriteOffer instance to validate.
     * @param context context in which the constraint is evaluated.
     * @return false if the validation fails, true otherwise.
     */
    @Override
    public boolean isValid(FavouriteOffer favouriteOffer, ConstraintValidatorContext context) {
        if (favouriteOffer.getOffer().getOwner().equals(favouriteOffer.getCustomer())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Users cannot add their own offer to favourites")
                    .addPropertyNode("customer")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
