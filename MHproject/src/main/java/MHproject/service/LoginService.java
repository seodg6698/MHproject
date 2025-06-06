package MHproject.service;

import org.springframework.stereotype.Service;

import MHproject.DTO.UserDTO;


public interface LoginService {

	void getRegister(String userId, String password, String email);
	
	public int idCheck(String userId);

	boolean isEmailExists(String email);




	


}
