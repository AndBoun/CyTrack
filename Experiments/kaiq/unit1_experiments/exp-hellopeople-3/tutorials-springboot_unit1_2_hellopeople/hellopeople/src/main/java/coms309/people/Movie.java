package coms309.people;

/**
 * Provides the structure for the Movie class.
 *
 * @author Kai Quach
 */
public class Movie {

    private String title;
    private String genre;
    private String director;

    // Default constructor
    public Movie() {
    }

    // Parameterized constructor
    public Movie(String title, String genre, String director) {
        this.title = title;
        this.genre = genre;
        this.director = director;
    }

    // Getters and setters for Movie attributes
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return this.director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    // Override toString method to display movie information
    @Override
    public String toString() {
        return title + " (" + genre + "), directed by " + director;
    }
}
