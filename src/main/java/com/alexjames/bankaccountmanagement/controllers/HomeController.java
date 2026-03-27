package com.alexjames.bankaccountmanagement.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Name: Alex James
 * Date: 2026-03-26
 * Purpose: Handles requests for the welcome page.
 */
@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String showHomePage() {
        return "welcome";
    }
}
