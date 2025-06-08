package MHproject.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import MHproject.DTO.UserDTO;

@Mapper
public interface UserMapper {
	
	UserDTO findByUserid(@Param("userid") String userid) throws Exception;

	String findUserIdByEmail(@Param("email") String email) throws Exception;


	/**
	 * 이메일로 사용자 정보 조회 (아이디와 이메일 모두 확인)
	 */
	public UserDTO findUserByEmail(@Param("email") String email) throws Exception;


	/* String getPasswordByUserId(@Param("userid") String userid); */

	int updatePassword(@Param("userid") String userid, @Param("password") String password);


    

}
