package com.danven.web_library.domain.config.custom_validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Custom validation annotation to ensure the validity of a FavouriteOffer.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FavouriteOfferValidator.class)
@Documented
public @interface ValidFavouriteOffer {
    /**
     * Error message to be returned if the FavouriteOffer is invalid.
     *
     * @return the error message.
     */
    String message() default "Invalid favourite offer";

    /**
     * Groups to which this constraint belongs.
     *
     * @return the groups.
     */
    Class<?>[] groups() default {};

    /**
     * Payload for clients to specify additional information about the validation failure.
     *
     * @return the payload.
     */
    Class<? extends Payload>[] payload() default {};
}
