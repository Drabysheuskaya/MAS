package com.danven.web_library;

import com.danven.web_library.domain.book.*;
import com.danven.web_library.domain.offer.*;
import com.danven.web_library.domain.report.Report;
import com.danven.web_library.domain.user.Address;
import com.danven.web_library.domain.user.Customer;
import com.danven.web_library.domain.user.User;
import com.danven.web_library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SpringBootApplication
public class WebLibraryApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(WebLibraryApplication.class, args);
    }

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Category firstCategory = new Category("Fantasy");
        Category secondCategory = new Category("Documentary");
        Category thirdCategory = new Category("Science");

        categoryRepository.save(firstCategory);
        categoryRepository.save(secondCategory);
        categoryRepository.save(thirdCategory);


        Address firstAddress = new Address(
                "Poland",
                Optional.of("Warsaw"),
                Optional.of("Lisa Kuli"),
                Optional.of("103"),
                "05270"
        );

        User firstUser = new Customer(
                "First name",
                Optional.of("First surname"),
                true,
                "danven2018@gmail.com",
                passwordEncoder.encode("12345"),
                "123445678990",
                LocalDate.of(2003, 10, 10),
                firstAddress
        );

        User secondUser = new Customer(
                "Second name",
                Optional.of("Second surname"),
                true,
                "secondUser@gmail.com",
                passwordEncoder.encode("12345"),
                "123445678990",
                LocalDate.of(2003, 10, 10),
                firstAddress
        );


        Book firstBook = new PaperBook(
                "First Book Name",
                2004,
                "Description  for book one",
                "Author 1",
                "12-3456789",
                new HashSet<>(Set.of(secondCategory, thirdCategory)),
                3
        );

        Book secondBook = new DiskBook(
                "First Book Name",
                2004,
                "Description  for book one",
                "Author 1",
                "12-345678885",
                new HashSet<>(Set.of(firstCategory, secondCategory)),
                3.0,
                DiskFormat.CD
        );

        Book thirdBook = new PaperBookWithDisk(
                "First Book Name",
                2004,
                "Description  for book one",
                "Author 1",
                "12-34567888999",
                new HashSet<>(Set.of(firstCategory, secondCategory)),
                560,
                true,
                3.0,
                DiskFormat.CD
        );

        Image firstImage = new Image(
                Files.readAllBytes(Path.of("src/main/resources/static/images/book1(jpeg).jpeg")),
                ImageFormat.JPEG,
                true,
                firstBook
        );

        Image secondImage = new Image(
                Files.readAllBytes(Path.of("src/main/resources/static/images/book2(jpeg).jpeg")),
                ImageFormat.JPEG,
                false,
                firstBook
        );

        Image thirdImage = new Image(
                Files.readAllBytes(Path.of("src/main/resources/static/images/book3(jpeg).jpeg")),
                ImageFormat.JPEG,
                false,
                firstBook
        );

        Image forthImage = new Image(
                Files.readAllBytes(Path.of("src/main/resources/static/images/R.png")),
                ImageFormat.PNG,
                true,
                secondBook
        );

        Image fifthImage = new Image(
                Files.readAllBytes(Path.of("src/main/resources/static/images/R.png")),
                ImageFormat.PNG,
                false,
                secondBook
        );

        Image sixthImage = new Image(
                Files.readAllBytes(Path.of("src/main/resources/static/images/R.png")),
                ImageFormat.PNG,
                true,
                thirdBook
        );

        Image seventhImage = new Image(
                Files.readAllBytes(Path.of("src/main/resources/static/images/R.png")),
                ImageFormat.PNG,
                false,
                thirdBook
        );


        Offer firstOffer = new Offer(
                45.6,
                2,
                firstBook,
                Optional.empty(),
                Optional.empty(),
                Optional.of(34.7),
                PublishState.PUBLISHED,
                EnumSet.of(OfferType.DISCOUNT_OFFER),
                (Customer) firstUser
        );

        ContactInfo contactInfo = new ContactInfo(
                "first_email@gmail.com",
                Optional.of("362565364456"),
                Optional.of("https://546456"),
                firstOffer
        );

        Offer secondOffer = new Offer(
                47.4,
                3,
                secondBook,
                Optional.of(LocalDateTime.now().plusDays(9)),
                Optional.empty(),
                Optional.empty(),
                PublishState.PUBLISHED,
                EnumSet.of(OfferType.LIMITED_TIME_OFFER),
                (Customer) firstUser
        );

        ContactInfo secondContactInfo = new ContactInfo(
                "second_email@gmail.com",
                Optional.of("5677547546"),
                Optional.of("https://ewwe"),
                secondOffer
        );


        Offer thirdOffer = new Offer(
                47.4,
                1,
                thirdBook,
                Optional.of(LocalDateTime.now().plusDays(9)),
                Optional.of(LocalDateTime.now().minusDays(2)),
                Optional.empty(),
                PublishState.PUBLISHED,
                EnumSet.of(OfferType.LIMITED_TIME_OFFER, OfferType.BASIC_OFFER),
                (Customer) firstUser
        );

        ContactInfo thirdContactInfo = new ContactInfo(
                "third_email@gmail.com",
                Optional.of("4674567457"),
                Optional.of("https://ewwe"),
                thirdOffer
        );

        customerRepository.save((Customer) firstUser);

        offerRepository.save(firstOffer);
        offerRepository.save(secondOffer);
        offerRepository.save(thirdOffer);

        customerRepository.save((Customer) secondUser);

        FavouriteOffer firstFavouriteOffer = new FavouriteOffer(
                firstOffer,
                (Customer) secondUser,
                "For present"
        );

        Report firstReport = new Report(
                "illegal contect",
                secondOffer,
                (Customer) secondUser
        );
    }
}
