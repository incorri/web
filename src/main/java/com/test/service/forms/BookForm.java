package com.test.service.forms;

import lombok.Data;

@Data
public class BookForm {
    private String titleName;
    private String authorName;
    private Integer pages;
    private String publishingOffice;
}
