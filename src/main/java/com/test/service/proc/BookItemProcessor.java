package com.test.service.proc;

import com.test.service.models.Book;
import org.springframework.batch.item.ItemProcessor;

public class BookItemProcessor implements ItemProcessor<Book,Book> {
    @Override
    public Book process(Book book) throws Exception {
        final String titleName = book.getTitleName();
        final String authorName = book.getAuthorName();
        final Integer pages = book.getPages();
        final String publishingOffice = book.getPublishingOffice();
        final Book transformBook = new Book(titleName, authorName, pages, publishingOffice);
        return transformBook;
    }
}
