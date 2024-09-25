package coms309.dinos;


/**
 * Class representing a Dino
 *
 * @author Eduardo Barboza-Campos
 */

public class Dino {

    private String dinoType;

    private String name;
    
    private int hp;

    //private int dinoAtk;

    //totalAtk = dinoAtk + weaponAtk
    //private int totalAtk;

    //used for dodge checks
    private int speed;

    private Weapon weapon;

    public Dino(){

    }

    public Dino (String species, String name, int hp, int speed, Weapon weapon) {
        this.dinoType = species;
        this.name = name;
        this.hp = hp;
        //this.totalAtk = totalAtk;
        this.speed = speed;
        this.weapon = weapon;

    }

    public String getDinoType() {
        return this.dinoType;
    }

    public void setType(String type) {
        this.dinoType = type;
    }

    public void grantWeapon() {
        weapon = new Weapon();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return this.hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void decreaseHp(int damage) {
        this.hp -= damage;
    }

    /*
    public int getAtk() {
        return this.totalAtk;
    }
    */


    public int getSpeed() {
        return speed;
    }

    /*
    public int getTotalAtk() {
        return totalAtk;
    }*/

    public Weapon getWeapon() {
        return weapon;
    }
    @Override
    public String toString() {
        return dinoType + " "
               + name + " "
               + hp + " "

               + speed + "weapon: "
               + weapon.toString();
    }
}
