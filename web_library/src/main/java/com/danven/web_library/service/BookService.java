package com.danven.web_library.service;

import com.danven.web_library.domain.book.Book;
import com.danven.web_library.dto.BookUpdateDto;

import java.io.IOException;
import java.util.List;

public interface BookService {

    void updateBookWithOffer(Long id, BookUpdateDto bookUpdateDto)  throws IOException;

    List<Book> getBooksByOwner(Long id);
}
