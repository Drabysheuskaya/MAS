package com.danven.web_library.domain.book;

import com.danven.web_library.domain.offer.Offer;
import com.danven.web_library.exceptions.ValidationException;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Abstract class representing a book in the web library system.
 * This class serves as the base class for different types of books.
 */
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "book-with-offer-images-categories",
                attributeNodes = {
                        @NamedAttributeNode("offer"),
                        @NamedAttributeNode("images"),
                        @NamedAttributeNode("categories")
                }
        )
})
@Entity
@Table(name = "BOOK")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "book_id")
    protected Long id;

    @NotBlank(message = "Name can't be blank")
    @Column(name = "name", nullable = false)
    protected String name;

    @Min(value = 1, message = "The year of publishing must be greater than zero")
    @Column(name = "year_of_publishing", nullable = false)
    protected int yearOfPublishing;

    @NotBlank(message = "Description can't be blank")
    @Column(name = "description", nullable = false)
    protected String description;

    @NotBlank(message = "Author can't be blank")
    @Column(name = "author", nullable = false)
    protected String author;

    @Column(name = "isbn", unique = true, nullable = false)
    protected String isbn;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "book_category",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    protected Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    protected Set<Image> images = new HashSet<>();

    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id")
    protected Offer offer;

    /**
     * Default constructor for the Book class.
     */
    public Book() {
    }

    /**
     * Constructs a new Book with the specified details.
     *
     * @param name              the name of the book.
     * @param yearOfPublishing  the year the book was published.
     * @param description       the description of the book.
     * @param author            the author of the book.
     * @param isbn              the ISBN of the book.
     * @param categories        the categories the book belongs to.
     */
    public Book(String name, int yearOfPublishing, String description,
                String author, String isbn, Set<Category> categories) {
        this.name = name;
        this.yearOfPublishing = yearOfPublishing;
        this.description = description;
        this.author = author;
        this.isbn = isbn;
        this.categories = categories;
        categories.forEach(category -> category.addBook(this));
    }

    /**
     * Adds a category to the book.
     *
     * @param category the category to add.
     */
    public void addCategory(Category category) {
        if (category != null && !this.categories.contains(category)) {
            this.categories.add(category);
            if (!category.getBooks().contains(this)) {
                category.addBook(this);
            }
        }
    }

    /**
     * Removes a category from the book.
     *
     * @param category the category to remove.
     */
    public void removeCategory(Category category) {
        if (category != null && this.categories.contains(category)) {
            this.categories.remove(category);
            if (category.getBooks().contains(this)) {
                category.removeBook(this);
            }
        }
    }

    /**
     * Adds an image to the book.
     *
     * @param image the image to add.
     */
    public void addImage(Image image) {
        validateImage(image);
        images.add(image);
    }

    /**
     * Removes an image from the book.
     *
     * @param image the image to remove.
     */
    public void removeImage(Image image) {
        if (images.contains(image)) {
            images.remove(image);
            image.setBook(null);
        }
    }

    /**
     * Validates the given image.
     *
     * @param image the image to validate.
     */
    private void validateImage(Image image) {
        if (image == null) {
            throw new ValidationException("Image can't be null");
        }
        if (image.getBook() != this) {
            throw new ValidationException("Image is attached to another book");
        }
    }

    /**
     * Calculates the estimated time of reading the book.
     *
     * @return the estimated time of reading.
     */
    public abstract double calculateEstimatedTimeOfReading();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearOfPublishing() {
        return yearOfPublishing;
    }

    public void setYearOfPublishing(int yearOfPublishing) {
        this.yearOfPublishing = yearOfPublishing;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Set<Category> getCategories() {
        return new HashSet<>(categories);
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<Image> getImages() {
        return new HashSet<>(images);
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        if (offer.getBook() != this) {
            throw new ValidationException("Offer is attached to another book");
        }
        this.offer = offer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Book book = (Book) o;
        return id != null && Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
