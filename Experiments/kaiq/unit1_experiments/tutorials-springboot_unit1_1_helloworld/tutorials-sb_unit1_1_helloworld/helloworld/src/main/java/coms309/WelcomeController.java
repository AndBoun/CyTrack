package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello, this is for unit 1 testing - Kai Q:";
    }

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello, testing boop boop - kaiq: " + name;
    }
}
