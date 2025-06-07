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
	 * 사용자 비밀번호 업데이트
	 */
	public int updatePassword(@Param("userId") Map<String, Object> userId, @Param("newPassword") Map<String, Object> newPassword) throws Exception;

	/**
	 * 이메일로 사용자 정보 조회 (아이디와 이메일 모두 확인)
	 */
	public UserDTO findUserByEmail(@Param("email") String email) throws Exception;


    

}
