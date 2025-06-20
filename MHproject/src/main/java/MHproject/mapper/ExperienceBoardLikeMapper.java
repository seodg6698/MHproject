package MHproject.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExperienceBoardLikeMapper {

int insertLike(@Param("boardIdx") int boardIdx, @Param("userId") String userId) throws Exception;
	
    int deleteLike(@Param("boardIdx") int boardIdx, @Param("userId") String userId) throws Exception;
    
    int checkUserLike(@Param("boardIdx") int boardIdx, @Param("userId") String userId) throws Exception;
    
    int getLikeCount(@Param("boardIdx") int boardIdx) throws Exception;
    
    void updateBoardLikeCount(@Param("boardIdx") int boardIdx, @Param("likeCnt") int likeCnt) throws Exception;

	

}
