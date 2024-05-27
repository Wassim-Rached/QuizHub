package org.wa55death405.quizhub.controllers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;

@Controller
@RequestMapping("/docs")
public class DocsController {

    @GetMapping
    public String getDocs() throws IOException {
        return "forward:/docs/index.html";
    }
}
