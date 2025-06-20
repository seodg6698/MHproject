package MHproject.service;


import MHproject.mapper.CommentMapper3;


import MHproject.DTO.CommentDto3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentServiceImpl3 implements CommentService3 {
    
    @Autowired
    private CommentMapper3 commentMapper;
    
    @Override
    public List<CommentDto3> selectCommentList(int boardIdx) {
        return commentMapper.selectCommentList(boardIdx);
    }
    
    @Override
    public CommentDto3 selectCommentDetail(int commentIdx) {
        return commentMapper.selectCommentDetail(commentIdx);
    }
    
    @Override
    public boolean insertComment(CommentDto3 comment) {
        try {
            // 답글인 경우 부모 댓글 존재 여부 확인
            if (comment.getParentIdx() > 0) {
                CommentDto3 parentComment = commentMapper.selectCommentDetail(comment.getParentIdx());
                if (parentComment == null) {
                    return false; // 부모 댓글이 존재하지 않음
                }
            }
            
            commentMapper.insertComment(comment);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean updateComment(CommentDto3 comment, String userId) {
        try {
            // 기존 댓글 조회
            CommentDto3 existingComment = commentMapper.selectCommentDetail(comment.getCommentIdx());
            
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
            CommentDto3 existingComment = commentMapper.selectCommentDetail(commentIdx);
            
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