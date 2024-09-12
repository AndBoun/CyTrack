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

    private int atk;

    public Dino(){

    }

    /**
     * constructs a new Dino
     * @param species
     * @param name
     * @param hp
     * @param atk
     */
    public Dino(String species, String name, int hp, int atk){
        this.dinoType = species;
        this.name = name;
        this.hp = hp;
        this.atk = atk;
    }

    public String getDinoType() {
        return this.dinoType;
    }

    public void setType(String type) {
        this.dinoType = type;
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

    public int getAtk() {
        return this.atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    @Override
    public String toString() {
        return dinoType + " "
               + name + " "
               + hp + " "
               + atk;
    }
}
