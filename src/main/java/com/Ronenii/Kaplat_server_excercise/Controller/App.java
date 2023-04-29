package com.Ronenii.Kaplat_server_excercise.Controller;
import  com.Ronenii.Kaplat_server_excercise.Model.TODO;
import org.springframework.web.bind.annotation.*;

@RestController
public class App {
    public App(){
    }

    @GetMapping({"/todo/health"})
    public String health()
    {
        return "OK";
    }
}
