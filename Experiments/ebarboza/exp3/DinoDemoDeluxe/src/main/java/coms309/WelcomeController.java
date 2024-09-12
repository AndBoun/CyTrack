package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple Hello World Controller to display the string returned
 *
 * @author Vivek Bengre
 */

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "RAAAAHHHH. WELCOME TO DINO DEMO D E L U X E. TYPE A COMMAND AND STUFF";
    }

    //silly hello message
    @GetMapping("/hello")
    public String welcome2() {
        return "Hello hello world";
    }
}
