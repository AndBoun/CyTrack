package Users;

public class Users {
    private Long id;
    private String username;
    private String password;
    public Users(){

    }
    public Users(String username, String password){
        super();
        this.username = username;
        this.password = password;

    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getPassword(){ return password; }
    public void setPassword(String password){this.password = password; }
}

