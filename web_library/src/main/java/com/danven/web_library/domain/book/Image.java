package com.danven.web_library.domain.book;

import com.danven.web_library.exceptions.ValidationException;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents an image associated with a book.
 */
@Entity
@Table(name = "IMAGE")
public class Image implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "image_id")
    private Long id;

    @NotNull(message = "Image can't be null")
    @Lob
    @Column(name = "image", nullable = false)
    protected byte[] image;

    @NotNull(message = "Format can't be null")
    @Column(name = "image_format")
    @Enumerated(EnumType.STRING)
    private ImageFormat format;

    @Column(name = "is_preview")
    private boolean isPreview;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false, updatable = false)
    private Book book;

    @Transient
    private String base64Image;

    /**
     * Default constructor for Image.
     */
    public Image() {
    }

    /**
     * Constructs a new Image with the specified details.
     *
     * @param image     the byte array representing the image.
     * @param format    the format of the image.
     * @param isPreview whether the image is a preview image.
     * @param book      the book to which the image is associated.
     */
    public Image(byte[] image, ImageFormat format, boolean isPreview, Book book) {
        this.image = image;
        this.format = format;
        this.isPreview = isPreview;
        setBook(book);
    }

    /**
     * Sets the book associated with the image.
     *
     * @param book the book to set.
     * @throws ValidationException if the owner of the image is being changed.
     */
    public void setBook(Book book) {
        if (book != this.book && book != null && this.book != null) {
            throw new ValidationException("Owner of image can't be changed");
        }
        if (this.book == null) {
            this.book = book;
            book.addImage(this);
        }
    }

    /**
     * Gets the ID of the image.
     *
     * @return the image ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the byte array representing the image.
     *
     * @return the image byte array.
     */
    public byte[] getImage() {
        return image;
    }

    /**
     * Sets the byte array representing the image.
     *
     * @param image the image byte array to set.
     */
    public void setImage(byte[] image) {
        this.image = image;
    }

    /**
     * Gets the format of the image.
     *
     * @return the image format.
     */
    public ImageFormat getFormat() {
        return format;
    }

    /**
     * Sets the format of the image.
     *
     * @param format the image format to set.
     */
    public void setFormat(ImageFormat format) {
        this.format = format;
    }

    /**
     * Checks if the image is a preview image.
     *
     * @return true if the image is a preview image, false otherwise.
     */
    public boolean isPreview() {
        return isPreview;
    }

    /**
     * Sets whether the image is a preview image.
     *
     * @param preview true to set the image as a preview image, false otherwise.
     */
    public void setPreview(boolean preview) {
        isPreview = preview;
    }

    /**
     * Gets the book associated with the image.
     *
     * @return the associated book.
     */
    public Book getBook() {
        return book;
    }

    /**
     * Gets the Base64 representation of the image.
     *
     * @return the Base64 image string.
     */
    public String getBase64Image() {
        return base64Image;
    }

    /**
     * Sets the Base64 representation of the image.
     *
     * @param base64Image the Base64 image string to set.
     */
    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image)) return false;
        Image image1 = (Image) o;
        return isPreview == image1.isPreview && Objects.equals(id, image1.id) && Arrays.equals(image, image1.image) && format == image1.format && Objects.equals(book, image1.book);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, format, isPreview, book);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", imageFormat=" + format +
                ", isPreview=" + isPreview +
                '}';
    }
}
