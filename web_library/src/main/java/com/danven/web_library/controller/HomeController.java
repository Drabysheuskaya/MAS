package com.danven.web_library.controller;

import com.danven.web_library.domain.book.Book;
import com.danven.web_library.domain.book.Image;
import com.danven.web_library.repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Base64;
import java.util.List;

/**
 * Controller class for handling requests to the home page.
 */
@Controller
public class HomeController {

    private final BookRepository bookRepository;

    /**
     * Constructor for HomeController.
     *
     * @param bookRepository the repository for accessing book data.
     */
    public HomeController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Handles GET requests to the root URL ("/").
     * Retrieves all books with their offers, images, and categories, encodes the images in Base64, and adds them to the model.
     *
     * @param model the model to which the books will be added.
     * @return the name of the view to render.
     */
    @GetMapping("/")
    public String home(Model model) {
        List<Book> books = bookRepository.findAllBooksWithOffersImagesAndCategories();
        System.err.println(books.size());
        for (Book book : books) {
            if (book.getImages() != null) {
                for (Image image : book.getImages()) {
                    image.setBase64Image(Base64.getEncoder().encodeToString(image.getImage()));
                }
            }
        }
        model.addAttribute("books", books);
        return "home";
    }
}
