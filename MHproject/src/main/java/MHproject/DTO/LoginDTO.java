package MHproject.DTO;

import java.time.LocalDateTime;

public class LoginDTO {
    private String userid;
    private String password;
    
    public LoginDTO() {}
    
    public String getUserid() { 
    	return userid; 
    	}
    
    public void setUserid(String userid) { 
    	this.userid = userid; 
    	}
    
    public String getPassword() { 
    	return password; 
    	}
    
    public void setPassword(String password) { 
    	this.password = password; 
    	}
}