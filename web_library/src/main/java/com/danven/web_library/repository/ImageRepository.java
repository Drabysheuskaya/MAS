package com.danven.web_library.repository;

import com.danven.web_library.domain.book.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository interface for accessing Image entities from the database.
 */
public interface ImageRepository extends JpaRepository<Image, Long> {
    /**
     * Deletes all images associated with the given book ID.
     *
     * @param bookId The ID of the book whose images should be deleted.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Image i WHERE i.book.id = :bookId")
    void deleteImagesByBookId(@Param("bookId") Long bookId);
}
