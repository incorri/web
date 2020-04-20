package com.test.service.controllers;

import com.test.service.repositories.BatchConfiguration;
import com.test.service.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@Controller
public class BooksController {
    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private BatchConfiguration batchConfiguration;

    @GetMapping("/books")
    public String getBooksPage(ModelMap model) throws SQLException {
        model.addAttribute("booksFromServer", booksRepository.findAll());
        return "books";
    }

    @PostMapping("/books")
    public String add(@RequestParam("fileName") MultipartFile file, ModelMap model) throws Exception {
        String extName = "";
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File("d:\\"+uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String orgName = file.getOriginalFilename();
            if (".csv".equals(orgName.substring(orgName.lastIndexOf(".")))) {
                String newFilename = UUID.randomUUID().toString() + "." + orgName;
                file.transferTo(new File(uploadDir + File.separator + newFilename).getAbsoluteFile());
                extName = orgName;
                batchConfiguration.perform(newFilename);
            }
        }
        model.addAttribute("booksFromServer", booksRepository.findAll());
        model.addAttribute("newFilename", extName);
        return "books";
    }
}
