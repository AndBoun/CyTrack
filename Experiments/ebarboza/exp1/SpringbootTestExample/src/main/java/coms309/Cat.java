package coms309;

import java.util.Random;

/**
 * class representing the info of a particular silly cat :3
 */
public class Cat {

    /**
     * name of our cat
     */
    private String name;

    /**
     * breed of our cat
     */
    private String breed;

    /**
     * default constructor.
     * Just assigns name to "car" and breed to "calico"
     */
    public Cat ()
    {
        this.name = "car";
        this.breed = "calico";
    }

    /**
     * Cat constructor specifying name of cat only.
     * Randomly assigns this new cat to a certain breed using the pseudorandom output
     * from a Random object.
     * @param name
     */
    public Cat(String name)
    {
        Random rand = new Random();
        int type = rand.nextInt(4);
        String breed;

        if (type == 0) {
            breed = "calico";
        } else if (type == 1) {
            breed = "tabby";
        } else if (type == 2) {
            breed = "persian";
        } else {
            breed = "sphynx";
        }

        this.name = name;
        this.breed = breed;
    }

    /**
     * accesses name of cat
     * @return name of cat
     */
    public String getName()
    {
        return name;
    }

    /**
     * accesses cat breed
     * @return breed of cat
     */
    public String getBreed()
    {
        return breed;
    }

}
