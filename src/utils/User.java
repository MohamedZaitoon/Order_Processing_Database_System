package utils;

public class User {
	
	private String email;
	
	private String firstname;
	
	private String lastname;
	
	private String username;
	 
	private String birthdate;
	
	private String user_role;

    private String gender;
    
    private String password;
    
    public final static String MANAGER = "Manager";
    
    public final static String CUSTOMER = "Customer";
    
    public String getEmail() {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername (String username) {
        this.username = username;
    }
    
	public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
    
    public String getUserRole() {
        return user_role;
    }

    public void setUserRole(String user_role) {
    	this.user_role = user_role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setUserInfo(String email, String firstname,
    		String lastname, String username, String birthdate,
    		String user_role, String gender, String password) {
    	this.email = email;
    	this.firstname = firstname;
    	this.lastname = lastname;
    	this.username = username;
    	this.birthdate = birthdate;
    	this.user_role = user_role;
    	this.gender = gender;
    	this.password = password;	
    }
    
    public static String mapGender(String gender) {
    	String res = "";
    	switch (gender) {
    		case "Male":    return res = "M";
    		case "Female" : return res = "F";
    		case "Other"  : return res = "U";
    	}
    	return res;
    }
}
