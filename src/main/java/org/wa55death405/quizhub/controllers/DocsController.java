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
    public ResponseEntity<byte[]> getDocs() throws IOException {
        // Load the docs.html file from the resources directory
        Resource resource = new ClassPathResource("static/docs/index.html");

        // Read the file content into a byte array
        byte[] content = Files.readAllBytes(resource.getFile().toPath());

        // Set the appropriate content type for the response
        MediaType mediaType = MediaType.TEXT_HTML;

        // Return the file content as a ResponseEntity
        return ResponseEntity.ok().contentType(mediaType).body(content);
    }
}
