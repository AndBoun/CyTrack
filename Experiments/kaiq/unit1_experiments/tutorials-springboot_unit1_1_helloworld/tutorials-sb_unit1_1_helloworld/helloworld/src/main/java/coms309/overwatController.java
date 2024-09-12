package coms309;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class overwatController {

    @GetMapping("/overwat/reaper")
    public String reaper(){
        return "Ethan's favorite character <3.";
    }
    @GetMapping("/overwat/zenyatta")
    public String zen(){
        return "Monk robot with orbs, he heals and got a cool kick.";
    }

    @GetMapping("/overwat/orisa")
    public String orisa(){
        return "Another robot, another of ethan's favorite character <3";
    }

    @GetMapping("/overwat/soldier")
    public String soldier(){
        return "I like playing soldier";
    }

}
