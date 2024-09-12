package coms309.people;

/**
 * Provides the Definition/Structure for the people row
 *
 * @author Kai Quach
 */
public class Person {

    private String firstName;
    private String lastName;
    private String address;
    private String telephone;
    private Movie favoriteMovie; // Now using Movie object instead of String
    private Game favoriteGame;
    // Default constructor
    public Person() {
    }

    // Parameterized constructor
    public Person(String firstName, String lastName, String address, String telephone, Movie favoriteMovie, Game favoriteGame) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.telephone = telephone;
        this.favoriteMovie = favoriteMovie;
        this.favoriteGame = favoriteGame;
    }

    // Getters and setters for Person attributes
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    // Getter and setter for favoriteMovie
    public Movie getFavoriteMovie() {
        return this.favoriteMovie;
    }

    public void setFavoriteMovie(Movie favoriteMovie) {
        this.favoriteMovie = favoriteMovie;
    }

    public Game getFavoriteGame(){return this.favoriteGame;}
    public void setFavoriteGame(Game favoriteGame){this.favoriteGame = favoriteGame;}

    // Override toString method to display person information
    @Override
    public String toString() {
        return firstName + " "
                + lastName + " "
                + address + " "
                + telephone + " "
                + "Favorite Movie: " + favoriteMovie.toString()
                + "Favorite Game: " + favoriteGame.toString();

    }
}
