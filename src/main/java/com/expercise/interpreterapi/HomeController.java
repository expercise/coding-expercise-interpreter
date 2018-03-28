package com.expercise.interpreterapi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.annotations.ApiIgnore;

@Controller
public class HomeController {

    @ApiIgnore
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public RedirectView home() {
        return new RedirectView("/swagger-ui.html");
    }
}