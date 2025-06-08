package MHproject.service;



import MHproject.DTO.UserDTO;


public interface UserService {
	
	public UserDTO login(String userid, String password) throws Exception;

	public String findUserIdByEmail(String email);
	
	public String findUserByEmail(String email);

	
	boolean updatePassword(String userId, String newPassword);
	

}
