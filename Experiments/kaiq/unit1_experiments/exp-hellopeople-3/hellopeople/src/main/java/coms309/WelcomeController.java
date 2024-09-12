package coms309;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * Simple Hello People Controller to display the string returned
 *
 * @author Kai Quach
 */


@RestController
public class WelcomeController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/")
    public String welcome(){
        return "Welcome to this test!";
    }
    @GetMapping("/welcome")
    public Welcome welcome(@RequestParam(value = "name", defaultValue = "User") String name) {
        return new Welcome(counter.incrementAndGet(), String.format(template, name));
    }
}
