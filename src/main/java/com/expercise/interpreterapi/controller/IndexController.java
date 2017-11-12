package com.expercise.interpreterapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public RedirectView index() {
        return new RedirectView("/swagger-ui.html");
    }
}