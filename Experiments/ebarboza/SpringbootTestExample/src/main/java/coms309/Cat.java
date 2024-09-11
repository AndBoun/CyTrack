package coms309;

import java.util.Random;

public class Cat {

    private String name;
    private String breed;

    public Cat ()
    {
        this.name = "car";
        this.breed = "calico";
    }

    public Cat(String name)
    {
        Random rand = new Random();
        int type = rand.nextInt(4);;
        String breed;

        if (type == 0) {
            breed = "calico";
        }

        this.name = name;
        this.breed = "calico";
    }

    public String getName()
    {
        return name;
    }

    public String getBreed()
    {
        return breed;
    }

}
