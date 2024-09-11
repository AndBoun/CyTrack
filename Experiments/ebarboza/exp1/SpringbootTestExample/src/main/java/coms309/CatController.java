package coms309;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;


@RestController
class CatController {

    /**
     * list of our cats
     */
    private ArrayList<Cat> catList = new ArrayList<Cat>();

    /**
     * Welcome message when you first load the page
     * @return welcome message
     */
    @GetMapping("/")
    public String welcome() {
        return "Welcome to this CatGenerator experiment thingy! Type a command in the search bar!";
    }

    /**
     * Basic method that was present when downloading the tutorial.
     * Displays "Hello ... lol" along with whatever the user types after '/'.
     * @param name
     * @return output string based on input to search bar
     */
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
    public String printCats() {
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

    /**
     * Method for adding new Cat objects to our catlist.
     * Specifies the name of the cat to be added.
     * @param name
     */
    @GetMapping("/add {name}")
    public void add (@PathVariable String name) {
        catList.add(new Cat(name));
    }

    /**
     * Displays info of the Cat at specified index within our catlist.
     * @param index
     * @return cat info
     */
    @GetMapping("/info/{index}")
    public String getCatInfo(@PathVariable int index)
    {
        if (catList == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (index < 0 || index >= catList.size()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return catList.get(index).getName() + " " + catList.get(index).getBreed();
    }
}
