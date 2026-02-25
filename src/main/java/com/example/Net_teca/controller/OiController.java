package com.example.Net_teca.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")  // ou sem isso
public class OiController {
    @GetMapping("/ola")
    public String ola() {
        return "Olá! A aplicação está no ar 🚀";
    }
}