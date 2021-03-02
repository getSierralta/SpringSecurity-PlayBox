package com.security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
//@Controller is typically used in combination with a @RequestMapping annotation used on request handling methods.
//RequestMapping annotation we can apply on class level and as well as on method level
@RequestMapping("/")
//All the mappings after this have the "api/v1/students" before them
public class TemplateController {

    @GetMapping("login")
    public String getLoginView(){
        return "login";
    }
    @GetMapping("courses")
    public String getCourses(){
        return "courses";
    }
}
