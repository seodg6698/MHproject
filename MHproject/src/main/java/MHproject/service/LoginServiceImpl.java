package MHproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MHproject.DTO.UserDTO;
import MHproject.mapper.LoginMapper;
import MHproject.mapper.UserMapper;
import MHproject.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {
	
	@Autowired
    private LoginMapper loginMapper;

	
	
	@Override
	public int idCheck(String userId) {
		int checkValue = loginMapper.idCheck(userId);
		return checkValue;
	}


	@Override
	public void getRegister(String userId, String password, String email) {
		
		loginMapper.getRegister(userId,password,email);
		
	}


	@Override
	public boolean isEmailExists(String email) {
		// TODO Auto-generated method stub
		return false;
	}
		
		
	}
    
 


