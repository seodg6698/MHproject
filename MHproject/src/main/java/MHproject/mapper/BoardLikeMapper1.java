package MHproject.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import MHproject.DTO.BoardLikeDTO1;

@Mapper
public interface BoardLikeMapper1 {
    
    /**
     * 좋아요 추가
     * @param boardLike 좋아요 정보
     * @return 삽입된 행 수
     */
    int insertLike(BoardLikeDTO1 boardLike) throws Exception;
    
    /**
     * 좋아요 삭제
     * @param boardIdx 게시글 번호
     * @param userId 사용자 ID
     * @return 삭제된 행 수
     */
    int deleteLike(@Param("boardIdx") int boardIdx, @Param("userId") String userId) throws Exception;
    
    /**
     * 사용자의 특정 게시글 좋아요 여부 확인
     * @param boardIdx 게시글 번호
     * @param userId 사용자 ID
     * @return 좋아요 개수 (0 또는 1)
     */
    int checkUserLike(@Param("boardIdx") int boardIdx, @Param("userId") String userId) throws Exception;
    
    /**
     * 특정 게시글의 총 좋아요 개수 조회
     * @param boardIdx 게시글 번호
     * @return 좋아요 개수
     */
    int getLikeCount(@Param("boardIdx") int boardIdx) throws Exception;
    
    /**
     * 게시글의 좋아요 개수 업데이트 (t_board1 테이블)
     * @param boardIdx 게시글 번호
     * @param likeCnt 좋아요 개수
     */
    void updateBoardLikeCount(@Param("boardIdx") int boardIdx, @Param("likeCnt") int likeCnt) throws Exception;
}