package Controllers;
import java.util.*;
import Repositories.UserRepository;
import Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;


    @GetMapping(path = "/user")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }





}
