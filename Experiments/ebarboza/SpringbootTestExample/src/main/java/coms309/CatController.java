package coms309;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
class CatController {

    private ArrayList<Cat> catList = new ArrayList<Cat>();

    @GetMapping("/")
    public String welcome() {
        return "Welcome to this CatGenerator experiment thingy! Type '/cmd' to get a list of commands";
    }

    //when typing into searchbar, page will "react" to, in this case, anything typed
    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello wacky test for COMS 309 lol" + name;
    }

    /**
     * outputs ":3" for each cat in our catlist.
     * if we have 3 cats, we'll have three ":3"s, so our output would be:
     * :3 :3 :3
     * @return
     */
    @GetMapping("/cats")
    public String regCat() {
        String output = "";
        int catcount = catList.size();

        for (int i = 0; i < catcount; i++) {
            output += ":3";
            if (i != catcount - 1) {
                output += " ";
            }
        }

        return output;
    }

    @GetMapping("/add {name}")
    public void add (@PathVariable String name) {
        catList.add(new Cat(name));
    }

    @GetMapping("/info/{index}")
    public String getCatInfo(@PathVariable int index)
    {
        if (catList == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return catList.get(index).getName() + " " + catList.get(index).getBreed();
    }
}
