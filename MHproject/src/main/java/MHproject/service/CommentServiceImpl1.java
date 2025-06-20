package MHproject.service;

import MHproject.mapper.CommentMapper1;
import MHproject.service.CommentService1;

import MHproject.DTO.CommentDto1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentServiceImpl1 implements CommentService1 {
	
	private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl1.class);
    
    @Autowired
    private CommentMapper1 commentMapper;
    
    @Autowired
    private NotificationService notificationService;
    
    @Override
    public List<CommentDto1> selectCommentList(int boardIdx) {
        return commentMapper.selectCommentList(boardIdx);
    }
    
    @Override
    public CommentDto1 selectCommentDetail(int commentIdx) {
        return commentMapper.selectCommentDetail(commentIdx);
    }
    
    /**
     * 댓글 등록 (알림 기능 추가)
     * @return 
     */
    @Transactional
    public boolean insertComment(CommentDto1 comment) throws Exception {
        logger.info("댓글 등록 요청 - boardIdx: {}, creatorId: {}", 
                   comment.getBoardIdx(), comment.getCreatorId());
        
        try {
            // 답글인 경우 부모 댓글 존재 여부 확인
            if (comment.getParentIdx() > 0) {
                CommentDto1 parentComment = commentMapper.selectCommentDetail(comment.getParentIdx());
                if (parentComment == null) {
                	logger.warn("부모 댓글을 찾을 수 없음 - parentIdx: {}", comment.getParentIdx());
                    throw new IllegalArgumentException("부모 댓글을 찾을 수 없습니다. parentIdx: " + comment.getParentIdx());
                }
            }
            
            commentMapper.insertComment(comment);
            if (comment.getParentIdx() == 0) {
                // 일반 댓글인 경우 - 게시글 작성자에게 알림
                notificationService.createCommentNotification(
                    comment.getBoardIdx(), 
                    comment.getCommentIdx(), 
                    comment.getCreatorId()
                );
                logger.debug("댓글 알림 생성 요청 완료 - boardIdx: {}, senderId: {}", 
                           comment.getBoardIdx(), comment.getCreatorId());
            } else {
                // 답글인 경우 - 부모 댓글 작성자에게 알림
                notificationService.createReplyNotification(
                    comment.getBoardIdx(), 
                    comment.getCommentIdx(), 
                    comment.getParentIdx(),
                    comment.getCreatorId()
                );
                logger.debug("답글 알림 생성 요청 완료 - boardIdx: {}, commentIdx: {}, senderId: {}", 
                           comment.getBoardIdx(), comment.getCommentIdx(), comment.getCreatorId());
            }
            
            logger.info("댓글 등록 완료 - commentIdx: {}", comment.getCommentIdx());
            
        } catch (Exception e) {
            logger.error("댓글 등록 중 오류 발생 - boardIdx: {}, 오류: {}", 
                        comment.getBoardIdx(), e.getMessage(), e);
            throw e;
        }
        
        return true;
    }
    
    @Override
    public boolean updateComment(CommentDto1 comment, String userId) {
        try {
            // 기존 댓글 조회
            CommentDto1 existingComment = commentMapper.selectCommentDetail(comment.getCommentIdx());
            
            if (existingComment == null) {
                return false; // 댓글이 존재하지 않음
            }
            
            // 작성자 본인인지 확인
            if (existingComment.getCreatorId() != null && existingComment.getCreatorId().equals(userId)) {
                return false; // 권한 없음
            }
            
            commentMapper.updateComment(comment);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteComment(int commentIdx, String userId) {
        try {
            // 기존 댓글 조회
            CommentDto1 existingComment = commentMapper.selectCommentDetail(commentIdx);
            
            if (existingComment == null) {
                return false; // 댓글이 존재하지 않음
            }
            
            // 작성자 본인이거나 관리자인지 확인
            if (existingComment.getCreatorId() != null && 
            		existingComment.getCreatorId().equals(userId) && !"admin".equals(userId)) {
                return false; // 권한 없음
            }
            
            commentMapper.deleteComment(commentIdx);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public int selectCommentCount(int boardIdx) {
        return commentMapper.selectCommentCount(boardIdx);
    }
}