package com.danven.web_library.controller;

import com.danven.web_library.config.CustomUserDetailsService;
import com.danven.web_library.service.BookService;
import com.danven.web_library.domain.book.Book;
import com.danven.web_library.domain.book.Category;
import com.danven.web_library.domain.book.Image;
import com.danven.web_library.domain.user.User;
import com.danven.web_library.dto.BookUpdateDto;
import com.danven.web_library.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Base64;
import java.util.List;

/**
 * Controller class for handling user-related requests.
 */
@Controller
public class UserController {

    private final CategoryService categoryService;
    private final BookService bookService;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Constructor for UserController.
     *
     * @param categoryService the service for handling category data.
     * @param bookService     the service for book operations.
     * @param customUserDetailsService     the utility service for user security operations.
     */
    public UserController(CategoryService categoryService,
                          BookService bookService,
                          CustomUserDetailsService customUserDetailsService) {
        this.categoryService = categoryService;
        this.bookService = bookService;
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Handles GET requests to the login page.
     *
     * @param model the model to pass data to the view.
     * @param msg   an optional message to display on the login page.
     * @param error an optional error message to display on the login page.
     * @return the name of the login view.
     */
    @GetMapping("/login")
    public String loginPage(Model model, @RequestParam(name = "msg", required = false) String msg, @RequestParam(name = "error", required = false) String error) {
        model.addAttribute("msg", msg);
        model.addAttribute("error", error);
        return "login";
    }

    /**
     * Handles GET requests to the profile page.
     *
     * @param model the model to pass data to the view.
     * @return the name of the profile view.
     */
    @GetMapping("/profile")
    public String profilePage(Model model) {
        User user = customUserDetailsService.getLoggedInUser();
        model.addAttribute("user", user);
        return "profile";
    }

    /**
     * Handles GET requests to the user's own offers page.
     * Retrieves books owned by the logged-in user and adds them to the model.
     *
     * @param model   the model to pass data to the view.
     * @param session the HTTP session to store books.
     * @return the name of the profile offers view.
     */
    @GetMapping("/profile/offers")
    public String ownOfferPage(Model model, HttpSession session) {
        List<Book> books = bookService.getBooksByOwner(customUserDetailsService.getLoggedInUser().getId());
        for (Book book : books) {
            if (book.getImages() != null) {
                for (Image image : book.getImages()) {
                    image.setBase64Image(Base64.getEncoder().encodeToString(image.getImage()));
                }
            }
        }
        model.addAttribute("books", books);
        session.setAttribute("books", books);
        System.err.println("------------------------------------------------------");
        return "profile_offers";
    }

    /**
     * Handles GET requests to the book details page.
     * Retrieves the details of a specific book and adds them to the model.
     *
     * @param bookId  the ID of the book to retrieve details for.
     * @param model   the model to pass data to the view.
     * @param session the HTTP session to retrieve books from.
     * @return the name of the book details view or redirects to the profile offers page if the book is not found.
     */
    @GetMapping("/profile/offers/details")
    public String getBookDetails(@RequestParam("bookId") Long bookId, Model model, HttpSession session) {
        List<Book> books = (List<Book>) session.getAttribute("books");
        if (books != null) {
            Book book = books.stream()
                    .filter(b -> b.getId().equals(bookId))
                    .findFirst()
                    .orElse(null);
            if (book != null) {
                List<Category> categories = categoryService.getAllCategories();
                model.addAttribute("book", book);
                model.addAttribute("categories", categories);
                return "book_details";
            }
        }
        return "redirect:/profile/offers";
    }

    /**
     * Handles POST requests to update a book's details.
     *
     * @param bookId             the ID of the book to update.
     * @param bookUpdateDTO      the DTO containing updated book details.
     * @param redirectAttributes attributes for redirecting with messages.
     * @return the redirect URL to the profile offers page or the book details page if an error occurs.
     */
    @PostMapping("/profile/offers/details/update")
    public String updateBook(@RequestParam("bookId") Long bookId,
                             @ModelAttribute BookUpdateDto bookUpdateDTO, RedirectAttributes redirectAttributes) {
        try {
            bookService.updateBookWithOffer(bookId, bookUpdateDTO);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/profile/offers/details?bookId=" + bookId;
        }
        return "redirect:/profile/offers";
    }
}
