package MHproject.mapper;

import MHproject.DTO.CommentDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CommentMapper {
    
    /**
     * 댓글 목록 조회
     * @param boardIdx 게시글 ID
     * @return 댓글 목록
     */
    List<CommentDto> selectCommentList(int boardIdx);
    
    /**
     * 댓글 상세 조회
     * @param commentIdx 댓글 ID
     * @return 댓글 정보
     */
    CommentDto selectCommentDetail(int commentIdx);
    
    /**
     * 댓글 등록
     * @param comment 댓글 정보
     */
    void insertComment(CommentDto comment);
    
    /**
     * 댓글 수정
     * @param comment 댓글 정보
     */
    void updateComment(CommentDto comment);
    
    /**
     * 댓글 삭제 (논리삭제)
     * @param commentIdx 댓글 ID
     */
    void deleteComment(int commentIdx);
    
    /**
     * 댓글 개수 조회
     * @param boardIdx 게시글 ID
     * @return 댓글 개수
     */
    int selectCommentCount(int boardIdx);
    
    /**
     * 정렬 순서 업데이트
     * @param commentIdx 댓글 ID
     * @param sortOrder 정렬 순서
     */
    void updateSortOrder(int commentIdx, int sortOrder);
    
    /**
     * 최대 정렬 순서 조회
     * @param boardIdx 게시글 ID
     * @param parentIdx 부모 댓글 ID
     * @return 최대 정렬 순서
     */
    int selectMaxSortOrder(int boardIdx, int parentIdx);
}
