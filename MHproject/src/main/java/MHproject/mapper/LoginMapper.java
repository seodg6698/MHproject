package MHproject.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import MHproject.DTO.UserDTO;

@Mapper
public interface LoginMapper {

	void getRegister(@Param("userId")String userId,@Param("password") String password,
			 @Param("email") String email);
	
	
	int idCheck(@Param("userid") String userId);


	
}
