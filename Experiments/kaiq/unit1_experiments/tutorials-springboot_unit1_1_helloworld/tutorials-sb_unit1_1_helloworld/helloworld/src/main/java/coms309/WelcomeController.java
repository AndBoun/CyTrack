package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome(){
        return "Hello world! If you'd like to explore more do /overwat";
    }

    @GetMapping("/overwat")
    public String overwat() {
        return "Hello, I am testing by displaying information about overwatch characters. Please" +
                "I have information on 'reaper', 'zenyatta', 'orisa', and 'soldier'. Please enter their" +
                "names after the /.";
    }


}
