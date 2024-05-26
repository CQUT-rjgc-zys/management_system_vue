package com.example.system.exception;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ExceptionController {

    @RequestMapping("/exception")
    public void handleException(HttpServletRequest request){
        throw (RuntimeException) request.getAttribute("exception");
    }
}
