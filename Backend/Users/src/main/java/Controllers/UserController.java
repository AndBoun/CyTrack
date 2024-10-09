package Controllers;
import java.util.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import Entities.User;
import Respositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Autowired
    UserRepository userRepository;

    @GetMapping(path = "/{id}")
    public User getUserById(@PathVariable Long id){
        User result = userRepository.findById(id).get();

        return result;
    }

    @GetMapping(path = "/login")
    public Object UserAndPassword(@RequestParam("userid") Optional<String> userid,
                                  @RequestParam("password") Optional<String> password){
        if(userid.isPresent() && password.isPresent()){
            Optional<User> user = userRepository.findByUsername(userid.get());

            if (user.isPresent()){
                if (user.get().getPassword().equals(password.get())){
                    res.se
                }
            }
        }
    }


}
