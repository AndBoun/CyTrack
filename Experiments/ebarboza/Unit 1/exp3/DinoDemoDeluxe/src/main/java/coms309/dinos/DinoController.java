package coms309.dinos;

import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Random;

/**
 * Controller used to showcase CRUD from a LIST
 *
 * @author Eduardo Barboza-Campos
 */

@RestController
public class DinoController {

    // Note that there is only ONE instance of Dino Controller in 
    // Springboot system.
    HashMap<String, Dino> dinoList = new  HashMap<>();

    //CRUDL (create/read/update/delete/list)
    // use POST, GET, PUT, DELETE, GET methods for CRUDL

    // THIS IS THE LIST OPERATION
    // gets all the dino in the list and returns it in JSON format
    // This controller takes no input. 
    // Springboot automatically converts the list to JSON format 
    // in this case because of @ResponseBody
    // Note: To LIST, we use the GET method
    @GetMapping("/dinos")
    public  HashMap<String,Dino> getAllDinos() {
        return dinoList;
    }

    // THIS IS THE CREATE OPERATION
    // springboot automatically converts JSON input into a Dino object and 
    // the method below enters it into the list.
    // It returns a string message in THIS example.
    // in this case because of @ResponseBody
    // Note: To CREATE we use POST method
    @PostMapping("/new")
    public  String createDino(@RequestBody Dino Dino) {
        System.out.println(Dino);
        dinoList.put(Dino.getName(), Dino);
        return Dino.getName() + " JOINS THE BATTLE";
    }

    // THIS IS THE READ OPERATION
    // Springboot gets the PATHVARIABLE from the URL
    // We extract the Dino from the HashMap.
    // springboot automatically converts Dino to JSON format when we return it
    // in this case because of @ResponseBody
    // Note: To READ we use GET method
    @GetMapping("/dino/{dinoType}")
    public Dino getDino(@PathVariable String dinoType) {
        Dino d = dinoList.get(dinoType);
        return d;
    }

    // THIS IS THE UPDATE OPERATION
    // We extract the Dino from the HashMap and modify it.
    // Springboot automatically converts the Dino to JSON format
    // Springboot gets the PATHVARIABLE from the URL
    // Here we are returning what we sent to the method
    // in this case because of @ResponseBody
    // Note: To UPDATE we use PUT method
    @PutMapping("/dino/{name}")
    public Dino updateDino(@PathVariable String name, @RequestBody Dino d) {
        dinoList.replace(name, d);
        return dinoList.get(name);
    }

    // THIS IS THE DELETE OPERATION
    // Springboot gets the PATHVARIABLE from the URL
    // We return the entire list -- converted to JSON
    // in this case because of @ResponseBody
    // Note: To DELETE we use delete method
    
    @DeleteMapping("/dino/{name}")
    public HashMap<String, Dino> deleteDino(@PathVariable String name) {
        dinoList.remove(name);
        return dinoList;
    }

    // UPDATE OPERATION
    // Decreases hp of defending dino by the atk of the attacking dino
    @PutMapping("/atk/{attacker}/{defender}")
    public String attack (@PathVariable String attacker, @PathVariable String defender){
        Dino d = dinoList.get(defender);
        Dino a = dinoList.get(attacker);

        //defender is faster! dodges attacker!
        if (d.getSpeed() > a.getSpeed()) {
            return defender + " evaded " + attacker + "'s attack! (0 damage)";
        }

        //if hit and can crit, 2x damage!
        Random rand = new Random();
        if (rand.nextInt(2) == 1) {
            dinoList.get(defender).decreaseHp(dinoList.get(attacker).getWeapon().getAtk() * 2);
            return "CRITICAL HIT! " + attacker + " attacks " + defender + "for " + dinoList.get(attacker).getWeapon().getAtk() + " damage! " + defender + " is now left with " + dinoList.get(defender).getHp() + "hp";
        }
        dinoList.get(defender).decreaseHp(dinoList.get(attacker).getWeapon().getAtk());
        return attacker + " attacks " + defender + "for " + dinoList.get(attacker).getWeapon().getAtk() + " damage! " + defender + " is now left with " + dinoList.get(defender).getHp() + "hp";
    }

    //UPDATE OPERATION
    // renames the dinosaur specified by its "old" name to "newName".
    // uses a PATCH request to ONLY modify the name and no other contents
    @PatchMapping("/rename/{old}/{newName}")
    public HashMap<String, Dino> renameDino(@PathVariable String old, @PathVariable String newName) {
        dinoList.get(old).setName(newName);
        return dinoList;
    }

    //DELETE OPERATION
    //summons the Chicxulub impactor to impact the earth and cause a mass extinction
    @DeleteMapping("/meteor")
    public String meteor() {
        dinoList.clear();
        return "a meteor has struck the earth, causing a great cataclysm...the field is cleared";
    }

    @PutMapping("/break/{name}")
    public String breakWeapon(@PathVariable String name) {
        dinoList.get(name).getWeapon().broken();
        return name + "'s weapon has broken! They're a sitting duck!";
    }



}

