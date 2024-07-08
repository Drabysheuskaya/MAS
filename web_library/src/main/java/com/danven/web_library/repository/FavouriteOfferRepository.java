package com.danven.web_library.repository;

import com.danven.web_library.domain.offer.FavouriteOffer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing FavouriteOffer entities from the database.
 */
public interface FavouriteOfferRepository extends JpaRepository<FavouriteOffer, Long> {
}
