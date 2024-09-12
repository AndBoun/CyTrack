package coms309.dinos;

/**
 * @author Eduardo Barboza-Campos
 *
 * super simple class representing some weapon attributes
 */
public class Weapon {

    private int atk;


    private int critMult;

    public Weapon (int atk, int mult) {
        this.atk = atk;
       // this.range = range;
        this.critMult = mult;
    }

    public Weapon () {

    }

    public int getAtk () {
        return atk;
    }

    public int getCritMult() {
        return critMult;
    }


    public void broken () {
        atk = 0;
        //range = 0;
        critMult = 0;
    }

    @Override
    public String toString() {
        return atk + " " + critMult;
    }


}
