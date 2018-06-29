package com.devchallenge.cssminification.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("integration-test")
public class IntegrationController {

    @RequestMapping
    public String index() {
        return "integration-test";
    }
}
