package MHproject.controller;

import MHproject.service.CommentService1;
import MHproject.DTO.CommentDto1;
import MHproject.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/comment2")
public class CommentController2 {
    
    @Autowired
    private CommentService1 commentService;
    
    /**
     * 댓글 등록
     */
    @PostMapping("/insertComment2")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> insertComment(
            @RequestParam int boardIdx,
            @RequestParam int parentIdx,
            @RequestParam String content,
            HttpSession session) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 세션에서 사용자 정보 가져오기
            UserDTO user = (UserDTO) session.getAttribute("user");
            if (user == null) {
                result.put("success", false);
                result.put("message", "로그인이 필요합니다.");
                return ResponseEntity.ok(result);
            }
            
            // 댓글 DTO 생성
            CommentDto1 comment = new CommentDto1(boardIdx, parentIdx, content, user.getUserid());
            
            // 댓글 등록
            boolean success = commentService.insertComment(comment);
            
            if (success) {
                result.put("success", true);
                result.put("message", "댓글이 등록되었습니다.");
            } else {
                result.put("success", false);
                result.put("message", "댓글 등록에 실패했습니다.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "시스템 오류가 발생했습니다.");
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 댓글 수정
     */
    @PostMapping("/updateComment2")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateComment(
            @RequestParam int commentIdx,
            @RequestParam String content,
            HttpSession session) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 세션에서 사용자 정보 가져오기
        	UserDTO user = (UserDTO) session.getAttribute("user");
            if (user == null) {
                result.put("success", false);
                result.put("message", "로그인이 필요합니다.");
                return ResponseEntity.ok(result);
            }
            
            // 댓글 DTO 생성
            CommentDto1 comment = new CommentDto1();
            comment.setCommentIdx(commentIdx);
            comment.setContent(content);
            
            // 댓글 수정
            boolean success = commentService.updateComment(comment, user.getUserid());
            
            if (success) {
                result.put("success", true);
                result.put("message", "댓글이 수정되었습니다.");
            } else {
                result.put("success", false);
                result.put("message", "댓글 수정 권한이 없거나 댓글을 찾을 수 없습니다.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "시스템 오류가 발생했습니다.");
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 댓글 삭제
     */
    @PostMapping("/deleteComment2")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteComment(
            @RequestParam int commentIdx,
            HttpSession session) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 세션에서 사용자 정보 가져오기
        	UserDTO user = (UserDTO) session.getAttribute("user");
            if (user == null) {
                result.put("success", false);
                result.put("message", "로그인이 필요합니다.");
                return ResponseEntity.ok(result);
            }
            
            // 댓글 삭제
            boolean success = commentService.deleteComment(commentIdx, user.getUserid());
            
            if (success) {
                result.put("success", true);
                result.put("message", "댓글이 삭제되었습니다.");
            } else {
                result.put("success", false);
                result.put("message", "댓글 삭제 권한이 없거나 댓글을 찾을 수 없습니다.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "시스템 오류가 발생했습니다.");
        }
        
        return ResponseEntity.ok(result);
    }
}
