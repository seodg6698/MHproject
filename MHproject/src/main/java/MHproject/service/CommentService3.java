package MHproject.service;

import java.util.List;

import MHproject.DTO.CommentDto1;
import MHproject.DTO.CommentDto3;

public interface CommentService3 {
    
    /**
     * 댓글 목록 조회
     * @param boardIdx 게시글 ID
     * @return 댓글 목록
     */
    List<CommentDto3> selectCommentList(int boardIdx);
    
    /**
     * 댓글 상세 조회
     * @param commentIdx 댓글 ID
     * @return 댓글 정보
     */
    CommentDto3 selectCommentDetail(int commentIdx);
    
    /**
     * 댓글 등록
     * @param comment 댓글 정보
     * @return 성공 여부
     */
    boolean insertComment(CommentDto3 comment);
    
    /**
     * 댓글 수정
     * @param comment 댓글 정보
     * @param userId 사용자 ID
     * @return 성공 여부
     */
    boolean updateComment(CommentDto3 comment, String userId);
    
    /**
     * 댓글 삭제
     * @param commentIdx 댓글 ID
     * @param userId 사용자 ID
     * @return 성공 여부
     */
    boolean deleteComment(int commentIdx, String userId);
    
    /**
     * 댓글 개수 조회
     * @param boardIdx 게시글 ID
     * @return 댓글 개수
     */
    int selectCommentCount(int boardIdx);
}