package com.danven.web_library.domain.config.custom_validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Custom validation annotation to ensure the validity of offer types configuration in an Offer.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OfferTypeValidator.class)
@Documented
public @interface ValidOfferTypes {
    /**
     * Error message to be returned if the offer types configuration is invalid.
     *
     * @return the error message.
     */
    String message() default "Invalid offer types configuration";

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
