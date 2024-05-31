package org.wa55death405.quizhub.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/docs")
public class DocsController {

    @GetMapping
    public String getDocs() {
        return "forward:/docs/index.html";
    }
}
