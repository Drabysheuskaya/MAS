package com.danven.web_library.domain.book;

import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a category of books.
 */
@Entity
@Table(name = "CATEGORY")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    private Long id;

    @NotBlank(message = "Category name can't be blank")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<Book> books = new HashSet<>();

    /**
     * Default constructor for Category.
     */
    public Category() {
    }

    /**
     * Constructs a new Category with the specified name.
     *
     * @param name the name of the category.
     */
    public Category(String name) {
        this.name = name;
    }

    /**
     * Adds a book to the category.
     *
     * @param book the book to add.
     */
    public void addBook(Book book) {
        if (book != null && !this.books.contains(book)) {
            this.books.add(book);
            if (!book.getCategories().contains(this)) {
                book.addCategory(this);
            }
        }
    }

    /**
     * Removes a book from the category.
     *
     * @param book the book to remove.
     */
    public void removeBook(Book book) {
        if (book != null && this.books.contains(book)) {
            this.books.remove(book);
            if (book.getCategories().contains(this)) {
                book.removeCategory(this);
            }
        }
    }

    /**
     * Gets the ID of the category.
     *
     * @return the category ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the name of the category.
     *
     * @return the category name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the category.
     *
     * @param name the category name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the set of books in the category.
     *
     * @return the set of books.
     */
    public Set<Book> getBooks() {
        return new HashSet<>(books);
    }

    /**
     * Sets the set of books in the category.
     *
     * @param books the set of books to set.
     */
    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Category category = (Category) o;
        return id != null && Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
