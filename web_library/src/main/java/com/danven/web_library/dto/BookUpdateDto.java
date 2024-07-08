package com.danven.web_library.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * Data Transfer Object for updating a book's details.
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class BookUpdateDto {

    private String name;

    private int yearOfPublishing;

    private String description;

    private String author;

    private String isbn;

    private int numberOfPages;

    private float durationInHours;

    private String diskFormat;

    private int numberOfCopies;

    private float price;

    private List<Long> categories;

    private MultipartFile file1;

    private MultipartFile file2;

    private MultipartFile file3;


}
