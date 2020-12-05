package com.loginauthorization.loginauthorization.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*" , maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/all")
    public String allAccess(){
        return "Public Contents";
    }

    @GetMapping("/student")
//    @PreAuthorize("hasRole('STUDENT')")
    public String studentAccess(){
        return "Student Content";
    }

    @GetMapping("/admin")
//    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess(){
        return "Admin Content";
    }

    @GetMapping("/faculty")
//    @PreAuthorize("hasRole('FACULTY')")
    public String facultyAccess(){
        return "Faculty Content";
    }


}
