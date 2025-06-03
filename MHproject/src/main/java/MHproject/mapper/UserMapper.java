package MHproject.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import MHproject.DTO.UserDTO;

@Mapper
public interface UserMapper {
	
	UserDTO findByUserid(@Param("userid") String userid);


    

}
