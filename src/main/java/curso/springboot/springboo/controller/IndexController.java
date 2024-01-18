package curso.springboot.springboo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {

        System.out.println("_____________\n Testando 2 ");
        return "index";
    }
}
