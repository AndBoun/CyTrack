package coms309.people;

/**
 * Provides the structure for the Movie class.
 *
 * @author Kai Quach
 */
public class Game {

    private String game;

    // Default constructor
    public Game() {
    }

    // Parameterized constructor
    public Game(String game) {
        this.game = game;
    }

    // Getters and setters for Movie attributes
    public String getGame() {
        return this.game;
    }

    public void setGame(String title) {
        this.game = game;
    }



    // Override toString method to display movie information
    @Override
    public String toString() {
        return game;
    }
}
