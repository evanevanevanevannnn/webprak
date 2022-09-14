package ru.msu.cmc.webprak.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AllController {

    @RequestMapping(value = { "/", "/index"})
    public String index() {
        return "index";
    }

}