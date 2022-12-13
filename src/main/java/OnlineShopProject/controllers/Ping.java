package OnlineShopProject.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Ping {

    @GetMapping(path = "/ping")
    public String ping(){
        return "Ok";

    }
}
