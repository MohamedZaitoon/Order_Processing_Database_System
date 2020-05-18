package utils;

public class User {
	
	private String email;
	
	private String firstname;
	
	private String lastname;
	
	private String username;
	 
	private String birthdate;

    private String gender;
    
    private String password;
    
    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getFirstname () {
        return firstname;
    }

    public void setFirstname (String firstname) {
        this.firstname = firstname;
    }
    
    public String getLastname () {
        return lastname;
    }

    public void setLastname (String lastname) {
        this.lastname = lastname;
    }

    public String getUsername () {
        return username;
    }

    public void setUsername (String username) {
        this.username = username;
    }
    
	public String getBirthdate () {
        return birthdate;
    }

    public void setBirthdate (String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender () {
        return gender;
    }

    public void setGender (String gender) {
        this.gender = gender;
    }
    
    public String getPassword () {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }
    
    public void setUserInfo(String email, String firstname,
    		String lastname, String username, String birthdate,
    		String gender, String password) {
    	this.email = email;
    	this.firstname = firstname;
    	this.lastname = lastname;
    	this.username = username;
    	this.birthdate = birthdate;
    	this.gender = gender;
    	this.password = password;	
    }
}