package com.danven.web_library.repository;

import com.danven.web_library.domain.offer.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing Offer entities from the database.
 */
public interface OfferRepository extends JpaRepository<Offer, Long> {
}
