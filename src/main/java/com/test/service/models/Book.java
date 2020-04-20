package com.test.service.models;

import com.test.service.forms.BookForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "fix_books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titleName;
    private String authorName;

    private Integer pages;
    private String publishingOffice;

    public Book(String titleName, String authorName, Integer pages, String publishingOffice) {
        this.setTitleName(titleName);
        this.setAuthorName(authorName);
        this.setPages(pages);
        this.setPublishingOffice(publishingOffice);
    }


    public static Book from(BookForm form) {
        return Book.builder()
                .titleName(form.getTitleName())
                .authorName(form.getAuthorName())
                .build();
    }
}
