package com.danven.web_library.domain.book;

import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a disk book.
 */
@Entity
@Table(name = "DISK_BOOK")
public class DiskBook extends Book implements IDiskBook, Serializable {

    @Min(value = 0, message = "Duration in hours can't be negative or null")
    @Column(name = "duration_in_hours", nullable = false)
    private double durationInHours;

    @NotNull
    @Column(name = "disk_format", nullable = false)
    @Enumerated(EnumType.STRING)
    private DiskFormat diskFormat;

    /**
     * Default constructor for DiskBook.
     */
    public DiskBook() {
    }

    /**
     * Constructs a new DiskBook with the specified details.
     *
     * @param name             the name of the book.
     * @param yearOfPublishing the year the book was published.
     * @param description      the description of the book.
     * @param author           the author of the book.
     * @param isbn             the ISBN of the book.
     * @param categories       the categories the book belongs to.
     * @param durationInHours  the duration of the book in hours.
     * @param diskFormat       the format of the disk.
     */
    public DiskBook(String name, int yearOfPublishing, String description, String author, String isbn,
                    Set<Category> categories, double durationInHours, DiskFormat diskFormat) {
        super(name, yearOfPublishing, description, author, isbn, categories);
        this.durationInHours = durationInHours;
        this.diskFormat = diskFormat;
    }

    /**
     * Sets the duration of the book in hours.
     *
     * @param durationInHours the duration in hours.
     */
    @Override
    public void setDurationInHours(double durationInHours) {
        this.durationInHours = durationInHours;
    }

    /**
     * Gets the duration of the book in hours.
     *
     * @return the duration in hours.
     */
    @Override
    public double getDurationInHours() {
        return durationInHours;
    }

    /**
     * Sets the disk format of the book.
     *
     * @param diskFormat the disk format.
     */
    @Override
    public void setDiskFormat(DiskFormat diskFormat) {
        this.diskFormat = diskFormat;
    }

    /**
     * Gets the disk format of the book.
     *
     * @return the disk format.
     */
    @Override
    public DiskFormat getDiskFormat() {
        return diskFormat;
    }

    @Override
    public double calculateEstimatedTimeOfReading() {
        return durationInHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DiskBook diskBook = (DiskBook) o;
        return id != null && Objects.equals(id, diskBook.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
