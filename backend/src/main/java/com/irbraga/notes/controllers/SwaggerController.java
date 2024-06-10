package com.irbraga.notes.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
    info=@Info(title="Notes API server.",
               contact = @Contact(name="igor.braga@outlook.com",
                                  url = "http://google.com",
                                  email = "igor.braga@outlook.com"),
                                  license = @License(name = "License: Apache 2.0", url = "https://apache.org/licenses/LICENSE-2.0")
            )
    )
@Controller
public class SwaggerController {
    
    @RequestMapping("/")
    public String home() {
        return "redirect:/swagger-ui.html";
    }

}
