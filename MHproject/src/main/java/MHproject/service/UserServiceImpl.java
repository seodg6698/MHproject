package MHproject.service;

import MHproject.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MHproject.DTO.UserDTO;
import MHproject.mapper.UserMapper;


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	 public UserDTO login(String userid, String password) {
	        UserDTO user = userMapper.findByUserid(userid);
	        
	        if (user != null && password.equals(user.getPassword())) {
	            // 비밀번호 제거
	            user.setPassword(null);
	            return user;
	        }
	        return null;
	    }

	
	}
	
	


