package com.danven.web_library.domain.book;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a paper book that includes a disk.
 */
@Entity
@Table(name = "PAPER_BOOK_WITH_DISK")
public class PaperBookWithDisk extends PaperBook implements IDiskBook {

    @Column(name = "disk_is_glued")
    private boolean diskIsGlued;

    @Min(value = 0, message = "Duration in hours can't be negative or equal to zero")
    @Column(name = "duration_in_hours", nullable = false)
    private double durationInHours;

    @NotNull
    @Column(name = "disk_format", nullable = false)
    @Enumerated(EnumType.STRING)
    private DiskFormat diskFormat;

    /**
     * Default constructor for PaperBookWithDisk.
     */
    public PaperBookWithDisk() {
    }

    /**
     * Constructs a new PaperBookWithDisk with the specified details.
     *
     * @param name             the name of the book.
     * @param yearOfPublishing the year the book was published.
     * @param description      the description of the book.
     * @param author           the author of the book.
     * @param isbn             the ISBN of the book.
     * @param categories       the categories the book belongs to.
     * @param numberOfPages    the number of pages in the book.
     * @param diskIsGlued      whether the disk is glued to the book.
     * @param durationInHours  the duration of the disk in hours.
     * @param diskFormat       the format of the disk.
     */
    public PaperBookWithDisk(String name, int yearOfPublishing, String description, String author, String isbn,
                             Set<Category> categories, int numberOfPages, boolean diskIsGlued, double durationInHours,
                             DiskFormat diskFormat) {
        super(name, yearOfPublishing, description, author, isbn, categories, numberOfPages);
        this.diskIsGlued = diskIsGlued;
        this.durationInHours = durationInHours;
        this.diskFormat = diskFormat;
    }

    /**
     * Sets the duration of the disk in hours.
     *
     * @param durationInHours the duration in hours.
     */
    @Override
    public void setDurationInHours(double durationInHours) {
        this.durationInHours = durationInHours;
    }

    /**
     * Gets the duration of the disk in hours.
     *
     * @return the duration in hours.
     */
    @Override
    public double getDurationInHours() {
        return durationInHours;
    }

    /**
     * Sets the disk format.
     *
     * @param diskFormat the disk format.
     */
    @Override
    public void setDiskFormat(DiskFormat diskFormat) {
        this.diskFormat = diskFormat;
    }

    /**
     * Gets the disk format.
     *
     * @return the disk format.
     */
    @Override
    public DiskFormat getDiskFormat() {
        return diskFormat;
    }

    /**
     * Calculates the estimated time of reading the book.
     *
     * @return the estimated time of reading in hours.
     */
    @Override
    public double calculateEstimatedTimeOfReading() {
        return Math.min(super.calculateEstimatedTimeOfReading(), durationInHours);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaperBookWithDisk)) return false;
        if (!super.equals(o)) return false;
        PaperBookWithDisk that = (PaperBookWithDisk) o;
        return diskIsGlued == that.diskIsGlued && Double.compare(that.durationInHours, durationInHours) == 0 && diskFormat == that.diskFormat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), diskIsGlued, durationInHours, diskFormat);
    }
}
