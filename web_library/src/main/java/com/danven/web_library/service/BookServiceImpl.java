package com.danven.web_library.service;

import com.danven.web_library.domain.book.*;
import com.danven.web_library.domain.offer.Offer;
import com.danven.web_library.dto.BookUpdateDto;
import com.danven.web_library.exceptions.ValidationException;
import com.danven.web_library.repository.BookRepository;
import com.danven.web_library.repository.CategoryRepository;
import com.danven.web_library.repository.ImageRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service class that handles operations related to Book entities.
 * Provides methods for updating book information and managing associated entities like Offer and Image.
 */
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final Validator validator;

    /**
     * Constructs a new instance of BookService.
     *
     * @param bookRepository     The repository for Book entities.
     * @param categoryRepository The repository for Category entities.
     * @param imageRepository    The repository for Image entities.
     * @param validator          The validator for validating entities.
     */
    public BookServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository,
                           ImageRepository imageRepository, Validator validator) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.validator = validator;
    }


    /**
     * Updates a book entity along with its associated offer and images.
     *
     * @param bookId        The ID of the book to update.
     * @param bookUpdateDto DTO containing updated information for the book.
     * @throws IOException         If there is an error reading or processing image files.
     * @throws ValidationException If validation constraints are violated.
     */
    @Override
    @Transactional
    public void updateBookWithOffer(Long bookId, BookUpdateDto bookUpdateDto) throws IOException {
        try {
            Book book = bookRepository.findById(bookId).orElseThrow(() -> new ValidationException("No such book"));

            book.setName(bookUpdateDto.getName());
            book.setYearOfPublishing(bookUpdateDto.getYearOfPublishing());
            book.setDescription(bookUpdateDto.getDescription());
            book.setAuthor(bookUpdateDto.getAuthor());
            book.setIsbn(bookUpdateDto.getIsbn());

            book.setCategories(new HashSet<>(categoryRepository.findAllById(bookUpdateDto.getCategories())));

            if (book instanceof PaperBookWithDisk) {
                ((PaperBookWithDisk) book).setNumberOfPages(bookUpdateDto.getNumberOfPages());
                ((PaperBookWithDisk) book).setDurationInHours(bookUpdateDto.getDurationInHours());
                ((PaperBookWithDisk) book).setDiskFormat(DiskFormat.valueOf(bookUpdateDto.getDiskFormat()));
            } else if (book instanceof DiskBook) {
                ((DiskBook) book).setDurationInHours(bookUpdateDto.getDurationInHours());
                ((DiskBook) book).setDiskFormat(DiskFormat.valueOf(bookUpdateDto.getDiskFormat()));
            } else if (book instanceof PaperBook) {
                ((PaperBook) book).setNumberOfPages(bookUpdateDto.getNumberOfPages());
            }

            Offer offer = book.getOffer();
            offer.setNumberOfCopies(bookUpdateDto.getNumberOfCopies());
            offer.setPrice(bookUpdateDto.getPrice());

            Image imageFirst = processImageUpload(book, bookUpdateDto.getFile1(), true);
            Image imageSecond = processImageUpload(book, bookUpdateDto.getFile2(), false);
            Image imageThird = processImageUpload(book, bookUpdateDto.getFile3(), false);


            validateEntities(book, offer, imageFirst, imageSecond, imageThird);

            if (imageFirst != null || imageSecond != null || imageThird != null) {
                imageRepository.deleteAll(book.getImages());
                book.getImages().forEach(book::removeImage);
                if (imageFirst != null) {
                    book.addImage(imageFirst);
                }
                if (imageSecond != null) {
                    book.addImage(imageSecond);
                }
                if (imageThird != null) {
                    book.addImage(imageThird);
                }
            }
            bookRepository.save(book);
        } catch (DataIntegrityViolationException e) {
            String errorMessage = getConstraintViolationMessage(e);
            throw new ValidationException(errorMessage);
        }
    }

    @Override
    public List<Book> getBooksByOwner(Long id) {
        return bookRepository.findBooksByOwnerId(id);
    }

    /**
     * Retrieves the message from the ConstraintViolationException within a DataIntegrityViolationException.
     *
     * @param e The DataIntegrityViolationException that occurred.
     * @return The error message extracted from the ConstraintViolationException.
     */
    private String getConstraintViolationMessage(DataIntegrityViolationException e) {
        Throwable cause = e.getCause();
        while (cause != null) {
            if (cause instanceof ConstraintViolationException) {
                return cause.getMessage();
            }
            cause = cause.getCause();
        }
        return "Data integrity violation";
    }

    /**
     * Validates the entities (Book, Offer, Images) using the Validator.
     *
     * @param book        The Book entity to validate.
     * @param offer       The Offer entity to validate.
     * @param firstImage  The first Image entity to validate.
     * @param secondImage The second Image entity to validate.
     * @param thirdImage  The third Image entity to validate.
     * @throws ConstraintViolationException If any validation constraints are violated.
     */
    private void validateEntities(Book book, Offer offer, Image firstImage, Image secondImage, Image thirdImage) {
        Set<ConstraintViolation<Book>> bookViolations = validator.validate(book);
        Set<ConstraintViolation<Offer>> offerViolations = validator.validate(offer);
        Set<ConstraintViolation<Image>> firstImageViolations = firstImage != null ? validator.validate(firstImage) : Set.of();
        Set<ConstraintViolation<Image>> secondImageViolations = secondImage != null ? validator.validate(secondImage) : Set.of();
        Set<ConstraintViolation<Image>> thirdImageViolations = thirdImage != null ? validator.validate(thirdImage) : Set.of();

        Set<ConstraintViolation<?>> allViolations = Stream.of(bookViolations, offerViolations, firstImageViolations, secondImageViolations, thirdImageViolations)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        if (!allViolations.isEmpty()) {
            String errorMessage = allViolations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new ConstraintViolationException(errorMessage, allViolations);
        }
    }

    /**
     * Processes the uploaded image file and creates an Image entity.
     *
     * @param book      The Book entity associated with the image.
     * @param file      The uploaded file to process.
     * @param isPreview Flag indicating if the image is a preview image.
     * @return The created Image entity, or null if no file was uploaded.
     * @throws IOException If there is an error reading the image file.
     */
    private Image processImageUpload(Book book, MultipartFile file, boolean isPreview) throws IOException {
        if (file != null && !file.isEmpty()) {
            return new Image(
                    file.getBytes(),
                    getImageFormat(file),
                    isPreview,
                    book
            );
        }
        return null;
    }

    /**
     * Determines the image format based on the content type of the uploaded file.
     *
     * @param file The uploaded image file.
     * @return The ImageFormat corresponding to the uploaded file.
     * @throws RuntimeException If the image format is unsupported.
     */
    private ImageFormat getImageFormat(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null) {
            switch (contentType.toLowerCase()) {
                case "image/jpeg":
                    return ImageFormat.JPEG;
                case "image/png":
                    return ImageFormat.PNG;
                default:
                    throw new RuntimeException("Unsupported image format: " + contentType);
            }
        }
        throw new RuntimeException("Unsupported image format: " + contentType);
    }
}
