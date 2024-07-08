package com.danven.web_library.repository;

import com.danven.web_library.domain.book.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * Repository interface for accessing Book entities from the database.
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Finds all books with their associated offers, images, and categories
     * where the offer's publish state is 'PUBLISHED'.
     *
     * @return a list of published books with their offers, images, and categories.
     */
    @EntityGraph(value = "book-with-offer-images-categories", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT b FROM Book b WHERE b.offer.publishState = 'PUBLISHED'")
    List<Book> findAllBooksWithOffersImagesAndCategories();

    /**
     * Finds all books owned by a specific owner with their associated offers, images, and categories.
     *
     * @param ownerId the ID of the owner.
     * @return a list of books owned by the specified owner.
     */
    @EntityGraph(value = "book-with-offer-images-categories", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT b FROM Book b WHERE b.offer.owner.id = :ownerId")
    List<Book> findBooksByOwnerId(@Param("ownerId") Long ownerId);

}
