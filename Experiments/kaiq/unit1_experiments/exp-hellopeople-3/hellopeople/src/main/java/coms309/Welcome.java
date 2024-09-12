package coms309;

/**
 * Simple Hello World Controller to display the string returned
 *
 * @author Kai Quach
 */
public class Welcome {
    private long id;
    private String content;

    public Welcome(Long id, String content){
        this.id = id;
        this.content = content;
    }
    public Long getId(){
        return id;
    }
    public String getContent(){
        return content;
    }
}
