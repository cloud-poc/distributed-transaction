package org.akj.springboot.order.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/home")
public class HomeResource {

    @GetMapping("")
    public String get() {
        return "Welcome!";
    }
}
