package com.danven.web_library.domain.book;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * Represents usual paper book.
 */
@Entity
@Table(name = "PAPER_BOOK")
public class PaperBook extends Book implements Serializable {

    protected static final int AVERAGE_WORDS_PER_PAGE = 300;

    protected static final int READING_SPEED_WPM = 200;

    @Min(value = 1, message = "Number of pages can't be negative or zero")
    @Column(name = "number_of_pages", nullable = false)
    protected int numberOfPages;

    /**
     * Default constructor for PaperBook.
     */
    public PaperBook() {
    }

    /**
     * Constructs a new PaperBook with the specified details.
     *
     * @param name             the name of the book.
     * @param yearOfPublishing the year the book was published.
     * @param description      the description of the book.
     * @param author           the author of the book.
     * @param isbn             the ISBN of the book.
     * @param categories       the categories the book belongs to.
     * @param numberOfPages    the number of pages in the book.
     */
    public PaperBook(String name, int yearOfPublishing, String description, String author, String isbn,
                     Set<Category> categories, int numberOfPages) {
        super(name, yearOfPublishing, description, author, isbn, categories);
        this.numberOfPages = numberOfPages;
    }

    /**
     * Gets the number of pages in the book.
     *
     * @return the number of pages.
     */
    public int getNumberOfPages() {
        return numberOfPages;
    }

    /**
     * Sets the number of pages in the book.
     *
     * @param numberOfPages the number of pages.
     */
    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    /**
     * Calculates the estimated time of reading the book.
     *
     * @return the estimated time of reading in hours.
     */
    @Override
    public double calculateEstimatedTimeOfReading() {
        return (numberOfPages * AVERAGE_WORDS_PER_PAGE) / (double) READING_SPEED_WPM / 60.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PaperBook paperBook = (PaperBook) o;
        return id != null && Objects.equals(id, paperBook.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
